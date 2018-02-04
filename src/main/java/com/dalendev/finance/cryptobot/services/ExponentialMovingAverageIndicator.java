package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Position;
import com.dalendev.finance.cryptobot.util.MovingAveragesCalculator;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author daniele.orler
 */
@Component
public class ExponentialMovingAverageIndicator implements Indicator {

    private final MovingAveragesCalculator movingAveragesCalculator;

    @Autowired
    public ExponentialMovingAverageIndicator(MovingAveragesCalculator movingAveragesCalculator) {
        this.movingAveragesCalculator = movingAveragesCalculator;
    }

    @Override
    public void addSample(Double price, CryptoCurrency currency) {
        currency.getAnalysis().getPrices6().add(price);
        currency.getAnalysis().getPrices24().add(price);

        double currentEMA6h = movingAveragesCalculator.evaluateExponential(60, currency.getAnalysis().getPrices6().toArray());
        double currentEMA24h = movingAveragesCalculator.evaluateExponential(60, currency.getAnalysis().getPrices24().toArray());

        currency.getAnalysis().getEMA6h().add(currentEMA6h);
        currency.getAnalysis().getEMA24h().add(currentEMA6h);

        if(currency.getAnalysis().getEMA24h().remainingCapacity() < 1) {
            Double diff = currentEMA6h - currentEMA24h;
            currency.getAnalysis().setMovingAverageDiff(PriceUtil.getPercentage(currentEMA6h, diff));
            currency.getAnalysis().getPrice30m().add(price);
            currency.getAnalysis().setPrice30mSlope(PriceUtil.slope(currency.getAnalysis().getPrice30m()));
        }
    }

    @Override
    public Boolean shouldBuy(CryptoCurrency currency) {
        return null;
    }

    @Override
    public Boolean shouldSell(Position position) {
        return null;
    }
}
