package com.example.duitku.category;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.database.DuitkuContract;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.transaction.TransactionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryController {

    private final Context context;

    public CategoryController (Context context){
        this.context = context;
    }

    // basic operations
    public Uri addCategory(Category category){
        ContentValues values = convertCategoryToContentValues(category);
        return context.getContentResolver().insert(CategoryEntry.CONTENT_URI, values);
    }

    public int updateCategory(Category category){
        ContentValues values = convertCategoryToContentValues(category);
        Uri uri = ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, category.getId());
        return context.getContentResolver().update(uri, values, null, null);
    }

    public int deleteCategory(Category category){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, category.getId()), null, null);
        new TransactionController(context).deleteAllTransactionWithCategoryId(category.getId());
        new BudgetController(context).deleteBudgetWithCategoryId(category.getId());
        return rowsDeleted;
    }

    // get category
    public List<Category> getAllCategory(){
        Cursor data = context.getContentResolver().query(CategoryEntry.CONTENT_URI,
                getFullProjection(), null, null, null);
        return convertCursorToListOfCategory(data);
    }

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
            ret = convertCursorToCategory(temp);
        }
        temp.close();

        return ret;
    }

    public Category getDefaultCategory(String type){
        String selection = CategoryEntry.COLUMN_NAME + " = ? COLLATE NOCASE AND " + CategoryEntry.COLUMN_TYPE + " = ? "; // COLLATE NOCASE buat insensitive case
        String[] selectionArgs = new String[]{CategoryEntry.DEFAULT_CATEGORY_NAME, type};

        Category ret;
        Cursor temp = context.getContentResolver().query(CategoryEntry.CONTENT_URI, getFullProjection(), selection, selectionArgs, null);
        if (temp.moveToFirst()){
            ret = convertCursorToCategory(temp);
        } else {
            ret = new Category(-1, CategoryEntry.DEFAULT_CATEGORY_NAME, type);
            addCategory(ret);
            ret = getDefaultCategory(type);
        }
        temp.close();

        return ret;
    }

    public String[] getFullProjection(){
        return new String[]{CategoryEntry.COLUMN_ID,
                CategoryEntry.COLUMN_NAME,
                CategoryEntry.COLUMN_TYPE};
    }

    // converting
    public Category convertCursorToCategory(Cursor data){
        int idColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_ID);
        int nameColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_NAME);
        int typeColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_TYPE);

        long id = data.getLong(idColumnIndex);
        String name = data.getString(nameColumnIndex);
        String type = data.getString(typeColumnIndex);

        return new Category(id, name, type);
    }

    public List<Category> convertCursorToListOfCategory(Cursor data){
        List<Category> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToCategory(data));
        } while (data.moveToNext());
        return ret;
    }

    private ContentValues convertCategoryToContentValues(Category category){
        ContentValues ret = new ContentValues();
        ret.put(CategoryEntry.COLUMN_NAME, category.getName());
        ret.put(CategoryEntry.COLUMN_TYPE, category.getType());
        return ret;
    }

    public HashMap<String, Object> convertCategoryToHashMap(Category category){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(CategoryEntry.COLUMN_ID, category.getId());
        hashMap.put(CategoryEntry.COLUMN_NAME, category.getName());
        hashMap.put(CategoryEntry.COLUMN_TYPE, category.getType());
        return hashMap;
    }

}
