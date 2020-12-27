package com.example.duitku.category;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.duitku.category.Category;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

public class CategoryController {

    private Context context;

    public CategoryController (Context context){
        this.context = context;
    }

    // basic operations
    public Uri addCategory(Category category){
        ContentValues values = convertCategoryToContentValues(category);
        Uri uri = context.getContentResolver().insert(CategoryEntry.CONTENT_URI, values);
        return uri;
    }

    public int updateCategory(Category category){
        ContentValues values = convertCategoryToContentValues(category);
        Uri uri = ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, category.getId());
        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);
        return rowsUpdated;
    }

    public int deleteCategory(long id){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, id), null, null);
        return rowsDeleted;
    }

    // get category
    public Category getCategoryById(long id){
        if (id == -1) return null;

        Category ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, id), getFullProjection(), null, null,null);
        if (data.moveToFirst()){
            ret = convertCursorToCategory(data);
        }

        return ret;
    }

    public Category getCategoryByNameAndType(String name, String type){
        String selection = CategoryEntry.COLUMN_NAME + " = ? COLLATE NOCASE AND " + CategoryEntry.COLUMN_TYPE + " = ? "; // COLLATE NOCASE buat insensitive case
        String[] selectionArgs = new String[]{name, type};

        Category ret = null;
        Cursor temp = context.getContentResolver().query(CategoryEntry.CONTENT_URI, getFullProjection(), selection, selectionArgs, null);
        if (temp.moveToFirst()){
            ret = new Category(temp.getLong(temp.getColumnIndex(CategoryEntry.COLUMN_ID)), temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_NAME)), temp.getString(temp.getColumnIndex(CategoryEntry.COLUMN_TYPE)));
        }

        return ret;
    }

    public String[] getFullProjection(){
        String[] projection = new String[]{CategoryEntry.COLUMN_ID,
                CategoryEntry.COLUMN_NAME,
                CategoryEntry.COLUMN_TYPE};
        return projection;
    }

    // converting
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

    private ContentValues convertCategoryToContentValues(Category category){
        ContentValues ret = new ContentValues();
        ret.put(CategoryEntry.COLUMN_NAME, category.getName());
        ret.put(CategoryEntry.COLUMN_TYPE, category.getType());
        return ret;
    }

}
