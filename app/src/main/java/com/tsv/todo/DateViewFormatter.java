package com.tsv.todo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateViewFormatter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date parse(String text) throws ParseException {
        return sdf.parse(text);
    }
}
