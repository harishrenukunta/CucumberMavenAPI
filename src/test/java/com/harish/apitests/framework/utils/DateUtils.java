package com.harish.apitests.framework.utils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDate toLocalDate(final XMLGregorianCalendar dt){
        return LocalDate.of(dt.getYear(), dt.getMonth(), dt.getDay());
    }

    public static LocalDate parse(final String dt, final String format){
        return LocalDate.parse(dt, DateTimeFormatter.ofPattern(format));
    }

    public static String getActualDate(final String offset){
        final LocalDate todaysDate = LocalDate.now();
        final LocalDate targetDate = todaysDate.plusDays(Integer.parseInt(offset));
        return targetDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
    }
}
