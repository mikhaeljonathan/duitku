package com.example.duitku.wallet;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WalletController {

    private final Context context;

    public WalletController(Context context){
        this.context = context;
    }

    // basic operations
    public Uri addWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        Uri uri = context.getContentResolver().insert(WalletEntry.CONTENT_URI, values);

        if (wallet.getWallet_amount() != 0){
            long id = ContentUris.parseId(uri);
            new TransactionController(context).addTransactionFromInitialWallet(id, wallet);
        }

        return uri;
    }

    public int updateWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        Uri uri = ContentUris.withAppendedId(WalletEntry.CONTENT_URI, wallet.get_id());
        return context.getContentResolver().update(uri, values, null, null);
    }

    public int deleteWallet(Wallet wallet){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, wallet.get_id()), null, null);
        new TransactionController(context).deleteAllTransactionWithWalletId(wallet.get_id());
        return rowsDeleted;
    }

    public double calculateTotalAmount(Cursor data){
        double ret = 0;
        if (!data.moveToFirst()) return ret;
        do {
            ret += data.getDouble(data.getColumnIndex(WalletEntry.COLUMN_AMOUNT));
        } while (data.moveToNext());
        return ret;
    }

    // get wallet
    public List<Wallet> getAllWallet(){
        Cursor data = context.getContentResolver().query(WalletEntry.CONTENT_URI,
                getFullProjection(), null, null, null);
        return convertCursorToListOfWallet(data);
    }

    public Wallet getWalletById(long id){
        if (id == -1) return null;

        Wallet ret = null;
        Cursor data = context.getContentResolver().query(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, id), getFullProjection(), null, null, null);
        if (data.moveToFirst()){
            ret = convertCursorToWallet(data);
        }

        return ret;
    }

    public Wallet getWalletByName(String name){
        Wallet ret = null;
        String selection = WalletEntry.COLUMN_NAME + " = ? COLLATE NOCASE"; // COLLATE NOCASE buat insensitive case
        String[] selectionArgs = new String[]{name};
        Cursor data = context.getContentResolver().query(WalletEntry.CONTENT_URI, getFullProjection(), selection, selectionArgs, null);
        if (data.moveToFirst()){
            ret = convertCursorToWallet(data);
        }
        return ret;
    }

    public String[] getFullProjection(){
        return new String[]{WalletEntry.COLUMN_ID,
                WalletEntry.COLUMN_NAME,
                WalletEntry.COLUMN_AMOUNT,
                WalletEntry.COLUMN_DESC};
    }

    // converting
    public Wallet convertCursorToWallet(Cursor data){
        int idColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_ID);
        int nameColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_NAME);
        int amountColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_AMOUNT);
        int descColumnIndex = data.getColumnIndex(WalletEntry.COLUMN_DESC);

        long id = data.getLong(idColumnIndex);
        String name = data.getString(nameColumnIndex);
        double amount = data.getDouble(amountColumnIndex);
        String desc = data.getString(descColumnIndex);

        return new Wallet(id, name, amount, desc);
    }

    public List<Wallet> convertCursorToListOfWallet(Cursor data){
        List<Wallet> ret = new ArrayList<>();
        if (!data.moveToFirst()) return ret;
        do {
            ret.add(convertCursorToWallet(data));
        } while (data.moveToNext());
        return ret;
    }

    private ContentValues convertWalletToContentValues(Wallet wallet){
        ContentValues ret = new ContentValues();
        ret.put(WalletEntry.COLUMN_NAME, wallet.getWallet_name());
        ret.put(WalletEntry.COLUMN_AMOUNT, wallet.getWallet_amount());
        ret.put(WalletEntry.COLUMN_DESC, wallet.getWallet_desc());
        return ret;
    }

    public HashMap<String, Object> convertWalletToHashMap(Wallet wallet){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(WalletEntry.COLUMN_ID, wallet.get_id());
        hashMap.put(WalletEntry.COLUMN_NAME, wallet.getWallet_name());
        hashMap.put(WalletEntry.COLUMN_AMOUNT, wallet.getWallet_amount());
        hashMap.put(WalletEntry.COLUMN_DESC, wallet.getWallet_desc());
        return hashMap;
    }

    // operations from other entity's operation
    public int updateWalletFromInitialTransaction(Transaction transaction){
        Category category = new CategoryController(context).getCategoryById(transaction.getCategory_id());
        Wallet wallet = getWalletById(transaction.getWallet_id());
        Wallet walletDest = getWalletById(transaction.getWalletdest_id());

        if (category == null){ // transfer
            wallet.setWallet_amount(wallet.getWallet_amount() - transaction.getTransaction_amount());
            walletDest.setWallet_amount(walletDest.getWallet_amount() + transaction.getTransaction_amount());
        } else if (category.getCategory_type().equals(CategoryEntry.TYPE_EXPENSE)){
            wallet.setWallet_amount(wallet.getWallet_amount() - transaction.getTransaction_amount());
        } else { // income
            wallet.setWallet_amount(wallet.getWallet_amount() + transaction.getTransaction_amount());
        }

        int rowsUpdated = updateWallet(wallet);
        if (walletDest != null) updateWallet(walletDest);
        return rowsUpdated;
    }

    public void updateWalletFromUpdatedTransaction(Transaction transactionBefore, Transaction transactionAfter){

        Category category = new CategoryController(context).getCategoryById(transactionBefore.getCategory_id());

        if (category == null){ // Transfer

            // if wallet source changed
            if (transactionBefore.getWallet_id() != transactionAfter.getWallet_id()){

                updateWalletAmount(transactionBefore.getWallet_id(), transactionBefore.getTransaction_amount());
                updateWalletAmount(transactionAfter.getWallet_id(), -transactionAfter.getTransaction_amount());

            } else if (transactionBefore.getTransaction_amount() != transactionAfter.getTransaction_amount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWallet_id(), transactionBefore.getTransaction_amount() - transactionAfter.getTransaction_amount());
            }

            // wallet destination changed
            if (transactionBefore.getWalletdest_id() != transactionAfter.getWalletdest_id()){

                updateWalletAmount(transactionBefore.getWalletdest_id(), -transactionBefore.getTransaction_amount());
                updateWalletAmount(transactionAfter.getWalletdest_id(), transactionAfter.getTransaction_amount());

            } else if (transactionBefore.getTransaction_amount() != transactionAfter.getTransaction_amount()){ // wallet dest not changed but amount changed
                updateWalletAmount(transactionAfter.getWalletdest_id(), -transactionBefore.getTransaction_amount() + transactionAfter.getTransaction_amount());
            }

        } else if (category.getCategory_type().equals(CategoryEntry.TYPE_EXPENSE)){ // Expense

            if (transactionBefore.getWallet_id() != transactionAfter.getWallet_id()){

                updateWalletAmount(transactionBefore.getWallet_id(), transactionBefore.getTransaction_amount());
                updateWalletAmount(transactionAfter.getWallet_id(), -transactionAfter.getTransaction_amount());

            } else if (transactionBefore.getTransaction_amount() != transactionAfter.getTransaction_amount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWallet_id(), transactionBefore.getTransaction_amount() - transactionAfter.getTransaction_amount());
            }

        } else { // Income

            if (transactionBefore.getWallet_id() != transactionAfter.getWallet_id()){

                updateWalletAmount(transactionBefore.getWallet_id(), -transactionBefore.getTransaction_amount());
                updateWalletAmount(transactionAfter.getWallet_id(), transactionAfter.getTransaction_amount());

            } else if (transactionBefore.getTransaction_amount() != transactionAfter.getTransaction_amount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWallet_id(), -transactionBefore.getTransaction_amount() + transactionAfter.getTransaction_amount());
            }

        }

    }

    private void updateWalletAmount(long walletId, double amount){
        Wallet wallet = getWalletById(walletId);
        wallet.setWallet_amount(wallet.getWallet_amount() + amount);
        updateWallet(wallet);
    }

    public void updateWalletFromDeletedTransaction(Transaction transaction){
        Category category = new CategoryController(context).getCategoryById(transaction.getCategory_id());

        if (category == null) {

            updateWalletAmount(transaction.getWallet_id(), transaction.getTransaction_amount());
            updateWalletAmount(transaction.getWalletdest_id(), -transaction.getTransaction_amount());

        } else if (category.getCategory_type().equals(CategoryEntry.TYPE_EXPENSE)){
            updateWalletAmount(transaction.getWallet_id(), transaction.getTransaction_amount());
        } else {
            updateWalletAmount(transaction.getWallet_id(), -transaction.getTransaction_amount());
        }
    }

}
