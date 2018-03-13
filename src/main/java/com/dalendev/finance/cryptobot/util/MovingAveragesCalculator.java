package com.dalendev.finance.cryptobot.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author daniele.orler
 */
public class MovingAveragesCalculator {

    public static double simple(int periodSpan, Object[] data) {
        double[] closingPeriodsPrices = periodsClosingPrices(periodSpan, data);

        return Arrays.stream(closingPeriodsPrices)
                .average()
                .getAsDouble();
    }

    public static double exponential(int periodSpan, Object[] data) {
        double[] closingPeriodsPrices = periodsClosingPrices(periodSpan, data);
        double multiplier = 2 / (closingPeriodsPrices.length + 1.0);
        double currentEma = closingPeriodsPrices[0];
        return ema(multiplier, currentEma, Arrays.copyOfRange(closingPeriodsPrices, 1, closingPeriodsPrices.length));
    }

    private static double ema(double alpha, double oldEma, double data[]) {
        if(data.length > 0) {
            double currentEma = alpha * data[0] + (1 - alpha) * oldEma;
            return ema(alpha, currentEma, Arrays.copyOfRange(data, 1, data.length));
        } else {
            return oldEma;
        }
    }

    private static double[] periodsClosingPrices(int periodSpan, Object[] data) {
        if(data.length < periodSpan) {
            return new double[] { (double) data[data.length-1] };
        }

        List<Double> closingPeriodsPrices = IntStream.range(0, data.length)
                // take only those indexes that represents the end of a period
                .filter(i -> (i + 1) % periodSpan == 0)
                .mapToDouble(i -> (Double) data[i])
                .boxed()
                .collect(Collectors.toList());

        if(data.length % periodSpan != 0) {
            closingPeriodsPrices.add((Double) data[data.length-1]);
        }

        return closingPeriodsPrices.stream().mapToDouble(Double::doubleValue).toArray();
    }

}
