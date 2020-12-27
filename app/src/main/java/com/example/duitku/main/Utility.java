package com.example.duitku.main;

import android.database.Cursor;

import com.example.duitku.database.DuitkuContract;
import com.example.duitku.category.CategoryTransaction;
import com.example.duitku.transaction.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    public static final String[] daysName = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final String[] monthsName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] monthsNameShort = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public static final HashMap<String, Integer> monthPosition(){
        HashMap<String, Integer> ret = new HashMap<>();
        for (int i = 0; i < monthsName.length; i++){
            ret.put(monthsName[i], i);
        }
        return ret;
    }


    public static String[] generateYear(){
        Calendar c = Calendar.getInstance();
        c.getTime();
        int year = c.get(Calendar.YEAR) - 5;

        String[] ret = new String[11];
        for (int i = 0; i < 11; i++, year++){
            ret[i] = Integer.toString(year);
        }

        return ret;
    }

    public static Date parseDate(String date){
        if (date == null) return null;
        Date ret = null;
        try {
            ret = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String convertDateToFullString(Date date){
        String ret = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        String[] components = ret.split(", ");
        ret = components[0] + "\n" + components[1];

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(date);

        if (calendar.get(Calendar.YEAR) == curYear && calendar.get(Calendar.MONTH) == curMonth && calendar.get(Calendar.DAY_OF_MONTH) == curDay){
            ret = "Today";
        }
        return ret;
    }

    public static String convertDateToString(Date date){
        String ret = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return ret;
    }

    public static int getMaxDayOfMonth(int month, int year){
        int[] maxDayOfMonth = {31, 28, 21, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (month == 2 && leapYear(year)){
            return 29;
        } else {
            return maxDayOfMonth[month - 1];
        }
    }

    public static boolean leapYear(int year){
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
            return true;
        return false;
    }

    public static Date convertElementsToDate(int year, int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }

}
