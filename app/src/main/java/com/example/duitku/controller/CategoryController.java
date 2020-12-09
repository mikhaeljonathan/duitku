package com.example.duitku.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Category;
import com.example.duitku.model.Transaction;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

public class CategoryController {

    private Context mContext;

    public CategoryController (Context context){
        mContext = context;
    }

    public Uri addCategory(Category category){

        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        values.put(CategoryEntry.COLUMN_TYPE, category.getType());

        Uri uri = mContext.getContentResolver().insert(CategoryEntry.CONTENT_URI, values);
        return uri;

    }

    public String getCategoryNameById(long id){
        String ret = "Transfer";
        Cursor temp = mContext.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, id), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME}, null,null, null);
        if (temp.moveToFirst()){
            ret = temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_NAME));
        }
        return ret;
    }

}
