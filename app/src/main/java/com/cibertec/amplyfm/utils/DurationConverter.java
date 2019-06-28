package com.cibertec.amplyfm.utils;


public class DurationConverter {
    private static final String SEPARATOR = ":";
    private static final String DEFAULT_VALUE = "0:0";

    public static String getDurationInMinutesText(long durationInSeconds) {
        durationInSeconds = durationInSeconds / 1000;
        if (durationInSeconds <= 0) {
            return DEFAULT_VALUE;
        }
        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;
        return Long.toString(minutes) + SEPARATOR + Long.toString(seconds);
    }
}
