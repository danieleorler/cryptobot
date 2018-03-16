package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoAnalysis;
import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Position;
import com.dalendev.finance.cryptobot.model.PositionAnalysis;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

import static com.dalendev.finance.cryptobot.util.MovingAveragesCalculator.exponential;

/**
 * @author daniele.orler
 */
@Component
public class ExponentialMovingAverageIndicator implements Indicator<ExponentialMovingAverageIndicator.EmaCryptoAnalysis, ExponentialMovingAverageIndicator.EmaPositionAnalysis> {

    private final ConfigService configService;

    public ExponentialMovingAverageIndicator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void init(CryptoCurrency currency) {
        currency.setAnalysis(new EmaCryptoAnalysis(10));
    }

    @Override
    public void init(Position position) {
        EmaPositionAnalysis positionAnalysis = new EmaPositionAnalysis();
        EmaCryptoAnalysis cryptoAnalysis = cast(position.getCurrency().getAnalysis());
        positionAnalysis.setThresholdMADiff(cryptoAnalysis.getMovingAverageDiff() / 2);
        position.setAnalysis(positionAnalysis);
    }

    @Override
    public void addSample(Double price, CryptoCurrency currency) {
        EmaCryptoAnalysis analysis = cast(currency.getAnalysis());
        analysis.getPrices24().add(price);

        Object[] array24 = analysis.getPrices24().toArray();

        int periodSpan = 10;

        Object[] slice6 = getLastNPrices(7*periodSpan, array24);
        Object[] slice24 = getLastNPrices(25*periodSpan, array24);

        double currentEMA6h = exponential(periodSpan, slice6);
        double currentEMA24h = exponential(periodSpan, slice24);

        analysis.getEmaShort().add(currentEMA6h);
        analysis.getEmaLong().add(currentEMA24h);

        if(analysis.getEmaLong().remainingCapacity() < 1) {
            analysis.setMovingAverageDiff(PriceUtil.getPercentage(currentEMA6h, currentEMA24h));
            analysis.getLastPrices().add(price);
            analysis.setLastPricesSlope(PriceUtil.slope(analysis.getLastPrices()));
        }
    }

    @Override
    public void updatePosition(Position position) {
        EmaPositionAnalysis positionAnalysis = new EmaPositionAnalysis();
        EmaCryptoAnalysis cryptoAnalysis = cast(position.getCurrency().getAnalysis());
        double thresholdMADiff = cryptoAnalysis.getMovingAverageDiff() / 2;
        if(thresholdMADiff > positionAnalysis.getThresholdMADiff()) {
            positionAnalysis.setThresholdMADiff(thresholdMADiff);
        }
    }

    @Override
    public Boolean shouldOpen(CryptoCurrency currency) {
        EmaCryptoAnalysis analysis = cast(currency.getAnalysis());
        return analysis.getMovingAverageDiff() > 1 && analysis.getLastPricesSlope() > 0;
    }

    @Override
    public Boolean shouldClose(Position position) {
        EmaCryptoAnalysis cryptoAnalysis = cast(position.getCurrency().getAnalysis());
        EmaPositionAnalysis positionAnalysis = cast(position.getAnalysis());

        return position.getChange() > configService.getTakeProfit() ||
                positionAnalysis.getThresholdMADiff() > cryptoAnalysis.getMovingAverageDiff();
    }

    private Object[] getLastNPrices(int n, Object[] prices) {
        if(prices.length >= n) {
            return Arrays.copyOfRange(prices, prices.length-n, prices.length);
        }
        return Arrays.copyOfRange(prices, 0, prices.length);
    }


    public static class EmaCryptoAnalysis implements CryptoAnalysis {

        private final EvictingQueue<Double> prices24;
        private final EvictingQueue<Double> emaLong;
        private final EvictingQueue<Double> emaShort;
        private final EvictingQueue<Double> lastPrices;
        private double lastPricesSlope = 0.0;
        private Double movingAverageDiff = 0.0;

        public EmaCryptoAnalysis(int nLastPrices) {
            emaShort = EvictingQueue.create(24 * 60);
            emaLong = EvictingQueue.create(24 * 60);
            prices24 = EvictingQueue.create(24 * 60);
            lastPrices = EvictingQueue.create(nLastPrices);
        }

        EvictingQueue<Double> getEmaLong() {
            return emaLong;
        }

        EvictingQueue<Double> getEmaShort() {
            return emaShort;
        }

        EvictingQueue<Double> getLastPrices() {
            return lastPrices;
        }

        Double getMovingAverageDiff() {
            return movingAverageDiff;
        }

        EvictingQueue<Double> getPrices24() {
            return prices24;
        }

        void setMovingAverageDiff(Double movingAverageDiff) {
            this.movingAverageDiff = movingAverageDiff;
        }

        double getLastPricesSlope() {
            return lastPricesSlope;
        }

        void setLastPricesSlope(double lastPricesSlope) {
            this.lastPricesSlope = lastPricesSlope;
        }

        @Override
        public Map<String, Object> toJson() {
            return ImmutableMap.<String, Object>builder()
                    .put("ema6h", emaShort)
                    .put("ema24h", emaLong)
                    .put("prices24", prices24)
                    .build();
        }
    }

    static class EmaPositionAnalysis implements PositionAnalysis {
        private Double thresholdMADiff;

        EmaPositionAnalysis() {
            thresholdMADiff = 0.0;
        }

        Double getThresholdMADiff() {
            return thresholdMADiff;
        }

        void setThresholdMADiff(Double thresholdMADiff) {
            this.thresholdMADiff = thresholdMADiff;
        }
    }
}
