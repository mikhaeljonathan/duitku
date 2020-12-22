package com.example.duitku.controller;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.duitku.database.DuitkuContract;
import com.example.duitku.model.Category;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

public class CategoryController {

    private Context context;

    public CategoryController (Context context){
        this.context = context;
    }

    public Uri addCategory(Category category){
        // taruh di contentvalues
        ContentValues values = new ContentValues();
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        values.put(CategoryEntry.COLUMN_TYPE, category.getType());

        Uri uri = context.getContentResolver().insert(CategoryEntry.CONTENT_URI, values);
        return uri;
    }

    public String getCategoryNameById(long id){
        String ret = "Transfer";
        Cursor temp = context.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, id), new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME}, null,null, null);
        if (temp.moveToFirst()){
            ret = temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_NAME));
        }
        return ret;
    }

    public Category getCategoryByNameAndType(String name, String type){
        Category ret = null;
        Cursor temp = context.getContentResolver().query(CategoryEntry.CONTENT_URI, new String[]{CategoryEntry.COLUMN_ID, CategoryEntry.COLUMN_NAME, CategoryEntry.COLUMN_TYPE}, CategoryEntry.COLUMN_NAME + " = ? AND " + CategoryEntry.COLUMN_TYPE + " = ?", new String[]{name, type}, null);
        if (temp.moveToFirst()){
            ret = new Category(temp.getLong(temp.getColumnIndex(CategoryEntry.COLUMN_ID)), temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_NAME)), temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE)));
        }
        return ret;
    }

    public Category getCategoryById(long id){
        Category ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, id), getProjection(), null, null,null);
        if (data.moveToFirst()){
            ret = convertCursorToCategory(data);
        }
        return ret;
    }

    public Category convertCursorToCategory(Cursor data){
        int idColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_ID);
        int nameColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_NAME);
        int typeColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_TYPE);

        long id = data.getLong(idColumnIndex);
        String name = data.getString(nameColumnIndex);
        String type = data.getString(typeColumnIndex);

        Category ret = new Category(id, name, type);
        return ret;
    }

    public String[] getProjection(){
        String[] projection = new String[]{CategoryEntry.COLUMN_ID,
                CategoryEntry.COLUMN_NAME,
                CategoryEntry.COLUMN_TYPE};
        return projection;
    }

}
