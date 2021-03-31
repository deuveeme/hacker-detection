package com.dvm.hackdetection.repository;

import com.dvm.hackdetection.repository.impl.ActivityRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class ActivityRepositoryTest {

    public static final String IP_00 = "00.00.00.000";
    public static final String IP_01 = "11.11.11.111";

    ActivityRepository repository;

    @Before
    public void setUp(){
        repository = new ActivityRepositoryImpl();
        repository.addActivity(IP_00, 112234556L);
        repository.addActivity(IP_00, 112236557L);
        repository.addActivity(IP_01, 123456789L);
    }

    @Test
    public void testAddActivity(){
        repository.addActivity("22.22.22.22",133612947L);
        Assert.assertTrue("22.22.22.22 should be added",repository.containsAttemptsForIp("22.22.22.22"));
    }

    @Test
    public void testContainAttemptsForIp(){
        Assert.assertTrue(IP_01+" IP is present in the repository: ", repository.containsAttemptsForIp(IP_01));
        Assert.assertFalse("99.00.00.00 IP is not present in the repository",repository.containsAttemptsForIp("99.00.00.00"));
    }

    @Test
    public void testDeleteTracesForIp(){
        repository.deleteTracesForIp(IP_01);
        Assert.assertFalse(IP_01+" deleted?: ",repository.containsAttemptsForIp(IP_01));

    }

    @Test
    public void testGetAttemptDatesFromIp(){
        List<Long> dates = repository.getAttemptDatesFromIp(IP_00);
        Assert.assertEquals(2,dates.size());

    }
}