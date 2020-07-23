package com.nab.microservices.core.phone.util;

import java.sql.Timestamp;

public class TimeUtil {
    private  TimeUtil(){}

    public static long differenceInSecondWithCurrentTime(Timestamp previousTimesTamp){
        long previousMilliseconds = previousTimesTamp.getTime();
        long currentMilliseconds = new Timestamp(System.currentTimeMillis()).getTime();

        long diff = currentMilliseconds - previousMilliseconds;
        long diffSeconds = diff / 1000;


        return diffSeconds;
    }
}
