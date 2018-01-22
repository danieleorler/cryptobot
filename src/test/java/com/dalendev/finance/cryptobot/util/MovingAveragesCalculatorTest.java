package com.dalendev.finance.cryptobot.util;

import com.dalendev.finance.cryptobot.test.TestUtil;
import com.google.common.collect.EvictingQueue;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


/**
 * @author daniele.orler
 */
public class MovingAveragesCalculatorTest {


    private MovingAveragesCalculator ma = new MovingAveragesCalculator();

    @Test
    public void exponentialOnlyOneDataPoint() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        assertEquals(1.0, ma.evaluateExponential(5, data.toArray()));
    }

    @Test
    public void exponentialIncompletePeriod() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        data.add(2.0);
        assertEquals(1.5, ma.evaluateExponential(5, data.toArray()));
    }

    @Test
    public void exponentialOneCompletePeriod() throws Exception {
        EvictingQueue<Double> data = EvictingQueue.create(10);
        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        data.add(4.0);
        data.add(5.0);
        assertEquals(3.0, ma.evaluateExponential(5, data.toArray()));
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
        assertEquals(3.0, ma.evaluateExponential(5, data.toArray()));
    }

    @Test
    public void exponentialWithRealData() throws Exception {

        Double[] prices = TestUtil.jsonFileToObject("ETC.json", Double[].class);
        assertEquals(0.003, ma.evaluateExponential(60, prices));
    }

}