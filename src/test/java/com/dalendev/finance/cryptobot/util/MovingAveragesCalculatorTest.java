package com.dalendev.finance.cryptobot.util;

import com.dalendev.finance.cryptobot.services.ConfigService;
import com.dalendev.finance.cryptobot.test.TestUtil;
import com.google.common.collect.EvictingQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


/**
 * @author daniele.orler
 */
@RunWith(MockitoJUnitRunner.class)
public class MovingAveragesCalculatorTest {

    @Mock
    private ConfigService configServiceMock;
    @InjectMocks
    private MovingAveragesCalculator ma;

    @Before
    public void setUp() throws Exception {
        when(configServiceMock.getExponentialFactor()).thenReturn(10.0);
    }

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
        assertEquals(13.0, ma.evaluateExponential(5, data.toArray()));
    }

    @Test
    public void exponentialWithRealData() throws Exception {

        Double[] prices = TestUtil.jsonFileToObject("ETC.json", Double[].class);
        assertEquals(0.003, ma.evaluateExponential(60, prices), 0.001);
    }

}