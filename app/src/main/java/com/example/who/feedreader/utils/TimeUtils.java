package com.example.who.feedreader.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by who on 22.07.2017.
 */

public class TimeUtils {
    public static String getNormalizedTime(String oldTime) {
        SimpleDateFormat fromUser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat myFormat = new SimpleDateFormat("YYYY, dd MMMMM HH:mm", Locale.ENGLISH);
        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(fromUser.parse(oldTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }
}
