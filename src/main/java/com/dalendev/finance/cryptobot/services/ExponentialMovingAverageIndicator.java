package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Position;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import com.google.common.collect.EvictingQueue;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

import static com.dalendev.finance.cryptobot.util.MovingAveragesCalculator.exponential;

/**
 * @author daniele.orler
 */
@Component
public class ExponentialMovingAverageIndicator implements Indicator {

    private final ConfigService configService;

    public ExponentialMovingAverageIndicator(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void init(CryptoCurrency currency) {
        Map<String, Object> extraProperties = currency.getAnalysis().getExtraProperties();
        extraProperties.put("last30mPrices", EvictingQueue.create(30));
        extraProperties.put("last30mPriceSlope", 0.0);
    }

    @Override
    public void addSample(Double price, CryptoCurrency currency) {
        currency.getAnalysis().getPrices24().add(price);

        Object[] array24 = currency.getAnalysis().getPrices24().toArray();

        int periodSpan = 10;

        Object[] slice6 = getLastNPrices(7*periodSpan, array24);
        Object[] slice24 = getLastNPrices(25*periodSpan, array24);

        double currentEMA6h = exponential(periodSpan, slice6);
        double currentEMA24h = exponential(periodSpan, slice24);

        currency.getAnalysis().getEMA6h().add(currentEMA6h);
        currency.getAnalysis().getEMA24h().add(currentEMA24h);

        if(currency.getAnalysis().getEMA24h().remainingCapacity() < 1) {
            currency.getAnalysis().setMovingAverageDiff(PriceUtil.getPercentage(currentEMA6h, currentEMA24h));
            EvictingQueue<Double> lastPeriodEmaShort = (EvictingQueue<Double>) currency.getAnalysis().getExtraProperties().get("last30mPrices");
            lastPeriodEmaShort.add(price);
            currency.getAnalysis().getExtraProperties().put("last30mPriceSlope", (PriceUtil.slope(lastPeriodEmaShort)));
        }
    }

    @Override
    public Boolean shouldOpen(CryptoCurrency currency) {
        Double percentage = currency.getAnalysis().getMovingAverageDiff();
        double last30mPriceSlope = (double) currency.getAnalysis().getExtraProperties().get("last30mPriceSlope");
        return percentage > 1 && last30mPriceSlope > 0;
    }

    @Override
    public Boolean shouldClose(Position position) {
        return position.getChange() > configService.getTakeProfit() ||
                position.getThresholdMADiff() > position.getCurrency().getAnalysis().getMovingAverageDiff();
    }

    private Object[] getLastNPrices(int n, Object[] prices) {
        if(prices.length >= n) {
            return Arrays.copyOfRange(prices, prices.length-n, prices.length);
        }
        return Arrays.copyOfRange(prices, 0, prices.length);
    }

}
