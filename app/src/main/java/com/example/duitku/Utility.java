package com.example.duitku;

import android.database.Cursor;

import com.example.duitku.database.DuitkuContract;
import com.example.duitku.category.CategoryTransaction;
import com.example.duitku.transaction.Transaction;

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

    public static List<Transaction> convertCursorToListOfTransaction(Cursor data){
        // litsnya kita init dlu
        List<Transaction> ret = new ArrayList<>();

        if (!data.moveToFirst()) return ret;
        do {
            // posisi kolom
            int transactionIdColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_ID);
            int dateColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_DATE);
            int walletIdColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_WALLET_ID);
            int walletDestIdColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_WALLET_DEST_ID);
            int categoryIdColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_CATEGORY_ID);
            int descColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_DESC);
            int amountColumnIndex = data.getColumnIndex(DuitkuContract.TransactionEntry.COLUMN_AMOUNT);

            // ambil datanya
            Date curDate = null;
            try {
                curDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString(dateColumnIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long transactionId = data.getLong(transactionIdColumnIndex);
            long walletId = data.getLong(walletIdColumnIndex);
            long walletDestId = data.getLong(walletDestIdColumnIndex);
            long categoryId = data.getLong(categoryIdColumnIndex);
            String desc = data.getString(descColumnIndex);
            double amount= data.getDouble(amountColumnIndex);

            // masukin ke list
            ret.add(new Transaction(transactionId, walletId, walletDestId, categoryId, desc, curDate, amount));

        } while (data.moveToNext());

        // return listnya
        return ret;

    }

    public List<CategoryTransaction> convertHashMapToList(HashMap<Long, CategoryTransaction> hashMap){
        List<CategoryTransaction> ret = new ArrayList<>();
        for (Map.Entry mapElement: hashMap.entrySet()){
            ret.add((CategoryTransaction) mapElement.getValue());
        }
        return ret;
    }

}
