package com.example.duitku.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DuitkuContract {

    public static final String CONTENT_AUTHORITY = "com.example.duitku";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WALLET = "wallet";
    public static final String PATH_TRANSACTION = "transaction";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_BUDGET = "budget";
    public static final String PATH_USER = "user";

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

        // tipe2 category
        public static final String TYPE_INCOME = "INC";
        public static final String TYPE_EXPENSE = "EXP";

        // ini ga ada di db, cuma penamaan aja
        public static final String TYPE_TRANSFER = "TRANS";

        // default category
        public static final String DEFAULT_CATEGORY_NAME = "Others";

    }

    public static final class BudgetEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUDGET).build();

        public static final String TABLE_NAME = "budget";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_AMOUNT = "budget_amount";
        public static final String COLUMN_USED = "budget_used";
        public static final String COLUMN_STARTDATE = "budget_startdate";
        public static final String COLUMN_ENDDATE = "budget_enddate";
        public static final String COLUMN_TYPE = "budget_type";
        public static final String COLUMN_CATEGORY_ID = "category_id";

        // tipe2 budget
        public static final String TYPE_MONTH = "MONTH";
        public static final String TYPE_3MONTH = "3MONTH";
        public static final String TYPE_YEAR = "YEAR";
    }

    public static final class TransactionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();

        public static final String TABLE_NAME = "transactions";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_WALLET_ID = "wallet_id";
        public static final String COLUMN_WALLET_DEST_ID = "walletdest_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_DESC = "transaction_desc";
        public static final String COLUMN_DATE = "transaction_date";
        public static final String COLUMN_AMOUNT = "transaction_amount";
    }

    public static final class UserEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_EMAIL = "user_email";
        public static final String COLUMN_USER_STATUS = "user_status";
        public static final String COLUMN_USER_FIRST_TIME= "user_first_time";
        public static final String COLUMN_USER_PASSCODE = "user_passcode";
    }

}
