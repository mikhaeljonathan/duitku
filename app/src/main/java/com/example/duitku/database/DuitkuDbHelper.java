package com.example.duitku.database;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DuitkuDbHelper extends SQLiteOpenHelper {

    // Ini class buat bikin database SQLite nya
    // class ini harus subclass dari SQLiteOpenHelper

    // Ini nama database nya
    private static final String DATABASE_NAME = "duitku.db";

    // Ini versi database nya
    private static final int DATABASE_VERSION = 1;

    public DuitkuDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Kalau misal databasenya gaada, bakalan execute onCreate ini
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Wallet sama category dlu yang dibuat karena ga mengandung FK

        final String CREATE_WALLET_TABLE = "CREATE TABLE " + WalletEntry.TABLE_NAME + " (" +
                WalletEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WalletEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                WalletEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL DEFAULT 0, " +
                WalletEntry.COLUMN_DESC + "  TEXT)";

        final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_TYPE + " TEXT NOT NULL CHECK(" + CategoryEntry.COLUMN_TYPE + " IN ('EXP', 'INC', 'TRANS')))";

        final String CREATE_BUDGET_TABLE = "CREATE TABLE " + BudgetEntry.TABLE_NAME + " (" +
                BudgetEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BudgetEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL DEFAULT 0, " +
                BudgetEntry.COLUMN_USED + " DOUBLE NOT NULL DEFAULT 0, " +
                BudgetEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY (" + BudgetEntry.COLUMN_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_ID + "))";

        final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TransactionEntry.TABLE_NAME + " (" +
                TransactionEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TransactionEntry.COLUMN_DESC + " TEXT, " +
                TransactionEntry.COLUMN_DATE + " TEXT NOT NULL CHECK(" + TransactionEntry.COLUMN_DATE + " LIKE '[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]'), " +
                TransactionEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL DEFAULT 0, " +
                TransactionEntry.COLUMN_WALLET_ID + " INTEGER, " +
                TransactionEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY (" + TransactionEntry.COLUMN_WALLET_ID + ") REFERENCES " + WalletEntry.TABLE_NAME + "(" + WalletEntry.COLUMN_ID + "), " +
                "FOREIGN KEY (" + TransactionEntry.COLUMN_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_ID + "))";

        // execute di database nya
        sqLiteDatabase.execSQL(CREATE_WALLET_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_BUDGET_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSACTION_TABLE);

    }

    // di execute kalo versinya ganti
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WalletEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BudgetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

}
