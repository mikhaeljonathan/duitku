package com.example.duitku.main;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Utility {

    public static final String[] daysName = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final String[] monthsName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] monthsNameShort = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public static final HashMap<String, Integer> monthPosition() {
        HashMap<String, Integer> ret = new HashMap<>();
        for (int i = 0; i < monthsName.length; i++) {
            ret.put(monthsName[i], i);
        }
        return ret;
    }


    public static String[] generateYear() {
        Calendar c = Calendar.getInstance();
        c.getTime();
        int year = c.get(Calendar.YEAR) - 5;

        String[] ret = new String[11];
        for (int i = 0; i < 11; i++, year++) {
            ret[i] = Integer.toString(year);
        }

        return ret;
    }

    public static Date parseDate(String date) {
        if (date == null) return null;
        Date ret = null;
        try {
            ret = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String convertDateToFullString(Date date) {
        String ret = DateFormat.getDateInstance(DateFormat.FULL).format(date);

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) == curYear && calendar.get(Calendar.MONTH) == curMonth && calendar.get(Calendar.DAY_OF_MONTH) == curDay) {
            ret = "Today";
        }
        return ret;
    }

    public static String convertDateToString(Date date) {
        String ret = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return ret;
    }

    public static int getMaxDayOfMonth(int month, int year) {
        int[] maxDayOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (month == 2 && leapYear(year)) {
            return 29;
        } else {
            return maxDayOfMonth[month - 1];
        }
    }

    public static boolean leapYear(int year) {
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
            return true;
        return false;
    }

    public static Date convertElementsToDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

    public static int getQuarter(int month) {
        return month / 4 + 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getIntervalsFromWeek(int lastWeek, int month, int year) {
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.US);

        YearMonth ym = YearMonth.of(year, month + 1);
        LocalDate firstOfMonth = ym.atDay(1);

        TemporalAdjuster ta = TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY);
        LocalDate previousOrSameMonday = firstOfMonth.with(ta);

        LocalDate endOfMonth = ym.atEndOfMonth();
        LocalDate weekStart = previousOrSameMonday;
        switch (lastWeek) {
            case 2:
                weekStart = weekStart.plusWeeks(1);
                break;
            case 3:
                weekStart = weekStart.plusWeeks(2);
                break;
            case 4:
                weekStart = weekStart.plusWeeks(3);
                break;
            case 5:
                weekStart = weekStart.plusWeeks(4);
                break;
            case 6:
                weekStart = weekStart.plusWeeks(5);
                break;
        }
        LocalDate weekStop = weekStart.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        return weekStart.format(formatter) + " - " + weekStop.format(formatter);
    }

}
