package com.dvm.hackdetection.service;

import com.dvm.hackdetection.repository.impl.ActivityRepositoryImpl;
import com.dvm.hackdetection.service.impl.HackerDetectionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class HackerDetectionTest {
    @TestConfiguration
    static class HackerDetectionIntegrationTest {
        @Bean
        public HackerDetection service() {
            return new HackerDetectionImpl(new ActivityRepositoryImpl());
        }
    }

    @Autowired
    private HackerDetection service;

    @MockBean
    private ActivityRepositoryImpl repository;

    @Captor
    ArgumentCaptor<String> ipSuccess;

    @Before
    public void setUp(){
        //service = new HackerDetectionImpl();
        Mockito.doNothing().when(repository).addActivity(Mockito.anyString(),Mockito.anyLong());
    }


    @Test
    public void testParseLine_5Failures(){
        final String logLine = "88.88.55.956,161719690,SIGNIN_FAILURE,Peter Parker";
        List<Long> datesFailed = new ArrayList<>(Arrays.asList(1617191407L,1617191467L,1617191655L,1617191606L,1617191690L));
        Mockito.when(repository.containsAttemptsForIp(Mockito.anyString())).thenReturn(true);
        Mockito.when(repository.getAttemptDatesFromIp(Mockito.anyString())).thenReturn(datesFailed);
        final String ipFailure = service.parseLine(logLine);
        Assert.assertNotNull(ipFailure);
        Assert.assertEquals("88.88.55.956", ipFailure);

    }

    @Test
    public void testParseLine_3Failures(){
        final String logLine = "88.88.55.956,161719690,SIGNIN_FAILURE,Peter Parker";
        List<Long> datesFailed = new ArrayList<>(Arrays.asList(1617161407L,1617161467L,1617191655L,1617191606L,1617191690L));
        Mockito.when(repository.containsAttemptsForIp(Mockito.anyString())).thenReturn(true);
        Mockito.when(repository.getAttemptDatesFromIp(Mockito.anyString())).thenReturn(datesFailed);
        final String ipFailure = service.parseLine(logLine);
        Assert.assertNull(ipFailure);
    }

    @Test
    public void testParseLine_1Failure(){
        final String logLine = "88.88.55.956,161719690,SIGNIN_FAILURE,Peter Parker";
        List<Long> datesFailed = new ArrayList<>(Arrays.asList(1617191690L));
        Mockito.when(repository.containsAttemptsForIp(Mockito.anyString())).thenReturn(true);
        Mockito.when(repository.getAttemptDatesFromIp(Mockito.anyString())).thenReturn(datesFailed);
        final String ipFailure = service.parseLine(logLine);
        Assert.assertNull(ipFailure);
    }

    @Test
    public void testParseLine_Success(){
        final String logLine = "88.88.55.956,161719690,SIGNIN_SUCCESS" +
                ",Peter Parker";
        List<Long> datesFailed = new ArrayList<>(Arrays.asList(1617161407L,1617161467L,1617191655L,1617191606L,1617191690L));
        Mockito.when(repository.containsAttemptsForIp(Mockito.anyString())).thenReturn(true);
        Mockito.when(repository.getAttemptDatesFromIp(Mockito.anyString())).thenReturn(datesFailed);
        final String ipFailure = service.parseLine(logLine);
        Mockito.verify(repository).deleteTracesForIp(ipSuccess.capture());
        Assert.assertNull(ipFailure);
        Assert.assertEquals("88.88.55.956", ipSuccess.getValue());
    }
}
