package com.dalendev.finance.cryptobot.util;

import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author daniele.orler
 */
public class MovingAveragesCalculator implements UnivariateStatistic {

    @Override
    public double evaluate(double[] data) {
        return evaluate(data, 0, data.length);
    }

    @Override
    public double evaluate(double[] data, int begin, int periods) {
        double wma = 0;
        int multiplier = 1;
        for(int i = begin; i < periods; i++) {
            wma += multiplier++ * data[i];
        }
        return wma / ((multiplier * (multiplier-1)) / 2);
    }

    public double evaluateWeighted(Object[] data) {
        double[] collect = Arrays.stream(data)
                .mapToDouble(value -> (double) value)
                .toArray();

        return evaluate(collect);
    }

    public double evaluateSimple(Object[] data) {
        double collect = Arrays.stream(data)
                .mapToDouble(value -> (double) value)
                .reduce(0, Double::sum);

        return collect/data.length;

    }

    public double evaluateExponential(int periodSpan, Object[] data) {
        double[] closingPeriodsPrices = closingPeriodsPrices(periodSpan, data);
        double multiplier = 8.0 / (closingPeriodsPrices.length + 1.0);
        double currentEma = closingPeriodsPrices[0];
        double ema = ema(multiplier, currentEma, Arrays.copyOfRange(closingPeriodsPrices, 1, closingPeriodsPrices.length));
        return ema;

    }

    private double ema(double alpha, double oldEma, double data[]) {
        if(data.length > 0) {
            double currentEma = alpha * data[0] + (1 - alpha) * oldEma;
            return ema(alpha, currentEma, Arrays.copyOfRange(data, 1, data.length));
        } else {
            return oldEma;
        }
    }

    private double[] closingPeriodsPrices(int periodSpan, Object[] data) {

        if(data.length < periodSpan) {
            double sum = Arrays.stream(data)
                    .mapToDouble(val -> (double) val)
                    .sum();
            return new double[] {sum / data.length};
        }

        List<Double> closingPeriodsPrices = new ArrayList<>();

        double sum = 0.0;
        for(int i = 0; i < data.length; i++) {
            sum += (double)data[i];
            if((i+1) % periodSpan == 0) {
                closingPeriodsPrices.add(sum / periodSpan);
                sum = 0.0;
            }

        }

        if(data.length % periodSpan != 0) {
            closingPeriodsPrices.add(sum / (data.length % periodSpan));
        }

        double[] toReturn = new double[closingPeriodsPrices.size()];

        IntStream.range(0, closingPeriodsPrices.size())
                .forEach(i -> toReturn[i] = closingPeriodsPrices.get(i));

        return toReturn;
    }

    @Override
    public UnivariateStatistic copy() {
        return null;
    }
}
