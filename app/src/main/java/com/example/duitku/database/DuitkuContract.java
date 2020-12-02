package com.example.duitku.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DuitkuContract {

    // Class ini untuk nyimpen2 URI, nama tabel, nama column dll
    // URI ini buat ngasih tau ContentResolver table mana yang mau diakses
    public static final String CONTENT_AUTHORITY = "com.example.duitku";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WALLET = "wallet";
    public static final String PATH_TRANSACTION = "transaction";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_BUDGET = "budget";

    // Setiap inner class di sini mewakili 1 table
    public static final class WalletEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WALLET).build();

        public static final String TABLE_NAME = "wallet";

        // nama kolom di tabel nya
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "wallet_name";
        public static final String COLUMN_AMOUNT = "wallet_amount";
        public static final String COLUMN_DESC = "wallet_desc";

    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String TABLE_NAME = "category";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "category_name";
        public static final String COLUMN_TYPE = "category_type";

    }

    public static final class BudgetEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUDGET).build();

        public static final String TABLE_NAME = "budget";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_AMOUNT = "budget_amount";
        public static final String COLUMN_USED = "budget_used";

    }

    public static final class TransactionEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();

        public static final String TABLE_NAME = "transactions";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_WALLET_ID = "wallet_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_DESC = "transaction_desc";
        public static final String COLUMN_DATE = "transaction_date";
        public static final String COLUMN_AMOUNT = "transaction_amount";

    }

}
