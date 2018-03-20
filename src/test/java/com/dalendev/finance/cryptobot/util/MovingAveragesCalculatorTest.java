package com.dalendev.finance.cryptobot.util;

import com.google.common.collect.EvictingQueue;
import org.junit.Test;

import static com.dalendev.finance.cryptobot.util.MovingAveragesCalculator.exponential;
import static junit.framework.TestCase.assertEquals;


/**
 * @author daniele.orler
 */
public class MovingAveragesCalculatorTest {

    @Test
    public void exponentialOnlyOneDataPoint() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        assertEquals(1.0, exponential(5, data.toArray()));
    }

    @Test
    public void exponentialIncompletePeriod() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        data.add(2.0);
        assertEquals(2.0, exponential(5, data.toArray()));
    }

    @Test
    public void exponentialOneCompletePeriod() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        data.add(4.0);
        data.add(5.0);
        assertEquals(5.0, exponential(5, data.toArray()));
    }

    @Test
    public void exponentialOnePeriodAndSomething() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        data.add(4.0);
        data.add(5.0);
        data.add(6.0);
        assertEquals(5.666, exponential(5, data.toArray()), 0.001);
    }

    @Test
    public void exponentialTwoPeriodsOfSpan1() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(5.0);
        data.add(6.0);
        assertEquals(5.666, exponential(1, data.toArray()), 0.001);
    }

}