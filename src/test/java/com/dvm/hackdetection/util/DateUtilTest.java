package com.dvm.hackdetection.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

@RunWith(SpringRunner.class)
public class DateUtilTest {

    @Test
    public void TestGetDifferenceInMinutesFromEpoch_3min(){
        final Long initEpoch = 1617191606L; //Wednesday, 31 March 2021 13:53:26 GMT+02:00
        final Long finalEpoch = 1617191786L; //Wednesday, 31 March 2021 13:56:26 GMT+02:00

        Long result = DateUtil.getDifferenceInMinutesFromEpoch(initEpoch, finalEpoch);
        Assert.assertEquals(3L, result.longValue());
    }

    @Test
    public void testGetDifferenceInMinutesFromEpoch_3min23sec(){
        final Long initEpoch = 1617191606L; //Wednesday, 31 March 2021 13:53:26 GMT+02:00
        final Long finalEpoch = 1617191809L; //Wednesday, 31 March 2021 13:56:49 GMT+02:00

        Long result = DateUtil.getDifferenceInMinutesFromEpoch(initEpoch, finalEpoch);
        Assert.assertEquals(3L, result.longValue());
    }

    @Test
    public void testGetDifferenceInMinutesFromRFC() throws ParseException {
        final String initDate= "Wed, 31 Mar 2021 13:53:26 +0100";
        final String finalDate = "Wed, 31 Mar 2021 13:56:26 +0100";
        Long result = DateUtil.getDifferenceInMinutesFromRFC(initDate, finalDate);
        Assert.assertEquals(3L, result.longValue());
    }
}
