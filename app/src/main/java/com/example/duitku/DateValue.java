package com.example.duitku;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.HashMap;

public class DateValue {

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

}
