package com.example.duitku.database;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.UserEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DuitkuDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "duitku.db";

    private static final int DATABASE_VERSION = 7;

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
                CategoryEntry.COLUMN_TYPE + " TEXT NOT NULL CHECK(" + CategoryEntry.COLUMN_TYPE + " IN ('" + CategoryEntry.TYPE_EXPENSE + "', '" + CategoryEntry.TYPE_INCOME + "')))";

        final String CREATE_BUDGET_TABLE = "CREATE TABLE " + BudgetEntry.TABLE_NAME + " (" +
                BudgetEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BudgetEntry.COLUMN_STARTDATE + " TEXT, " +
                BudgetEntry.COLUMN_ENDDATE + " TEXT, " +
                BudgetEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL, " +
                BudgetEntry.COLUMN_USED + " DOUBLE NOT NULL DEFAULT 0, " +
                BudgetEntry.COLUMN_TYPE + " TEXT CHECK(" + BudgetEntry.COLUMN_TYPE + " IN ('" + BudgetEntry.TYPE_MONTH + "', '" + BudgetEntry.TYPE_3MONTH + "', '" + BudgetEntry.TYPE_YEAR + "')), "+
                BudgetEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY (" + BudgetEntry.COLUMN_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_ID + "))";

        final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TransactionEntry.TABLE_NAME + " (" +
                TransactionEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TransactionEntry.COLUMN_DESC + " TEXT, " +
                TransactionEntry.COLUMN_DATE + " TEXT NOT NULL CHECK(" + TransactionEntry.COLUMN_DATE + " LIKE '%/%/%'), " +
                TransactionEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL DEFAULT 0, " +
                TransactionEntry.COLUMN_WALLET_ID + " INTEGER, " +
                TransactionEntry.COLUMN_WALLET_DEST_ID + " INTEGER, " +
                TransactionEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY (" + TransactionEntry.COLUMN_WALLET_ID + ") REFERENCES " + WalletEntry.TABLE_NAME + "(" + WalletEntry.COLUMN_ID + "), " +
                "FOREIGN KEY (" + TransactionEntry.COLUMN_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_ID + "))";

        final String CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
                UserEntry.COLUMN_USER_STATUS + " TEXT NOT NULL CHECK(" + UserEntry.COLUMN_USER_STATUS + " IN ('" + UserEntry.TYPE_REGULAR + "', '" + UserEntry.TYPE_PREMIUM + "')), " +
                UserEntry.COLUMN_USER_FIRST_TIME + " TEXT NOT NULL CHECK(" + UserEntry.COLUMN_USER_FIRST_TIME + " IN ('" + UserEntry.TYPE_FIRST_TIME + "', '" + UserEntry.TYPE_NOT_FIRST_TIME + "')), " +
                UserEntry.COLUMN_USER_PASSCODE + " TEXT)";

        // execute di database nya
        sqLiteDatabase.execSQL(CREATE_WALLET_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_BUDGET_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSACTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    // di execute kalo versinya ganti
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WalletEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BudgetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void dropAllTables(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WalletEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BudgetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}