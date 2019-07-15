package com.dimaoprog.newsapiapp.utils;

import androidx.databinding.InverseMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {

    @InverseMethod("stringToInt")
    public static String intToString(int value) {
        return String.valueOf(value);
    }

    public static int stringToInt(String value) {
        return value.isEmpty() ? 0 : Integer.valueOf(value);
    }

    @InverseMethod("stringToDouble")
    public static String doubleToString(double value) {
        return String.valueOf(value);
    }

    public static double stringToDouble(String value) {
        return Double.valueOf(value);
    }

    public static String dateToString(Long date) {
        if (date != null) {
            Date date1 = new Date(date);
            return new SimpleDateFormat("dd/MM", Locale.getDefault()).format(date1);
        } else {
            return null;
        }
    }

    public static String toIconPath(String path) {
        return "http:" + path;
    }
}
