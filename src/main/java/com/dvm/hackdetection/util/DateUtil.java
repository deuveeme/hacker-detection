package com.dvm.hackdetection.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static final String RFC_2822_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public static Long getDifferenceInMinutesFromEpoch(final Long initDate, final Long finalDate) {
        return TimeUnit.SECONDS.toMinutes(finalDate - initDate);
    }

    public static Long getDifferenceInMinutesFromRFC(final String initDate, final String finalDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(RFC_2822_FORMAT, Locale.ENGLISH);
        Date fDate =  sdf.parse(initDate);
        Date sDate = sdf.parse(finalDate);

        long diff = sDate.getTime() - fDate.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }
}
