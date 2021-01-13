package com.example.duitku.wallet;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;

public class WalletController {

    private final Context context;

    public WalletController(Context context){
        this.context = context;
    }

    // basic operations
    public Uri addWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        Uri uri = context.getContentResolver().insert(WalletEntry.CONTENT_URI, values);

        if (wallet.getAmount() != 0){
            long id = ContentUris.parseId(uri);
            new TransactionController(context).addTransactionFromInitialWallet(id, wallet);
        }

        return uri;
    }

    public int updateWallet(Wallet wallet){
        ContentValues values = convertWalletToContentValues(wallet);
        Uri uri = ContentUris.withAppendedId(WalletEntry.CONTENT_URI, wallet.getId());
        return context.getContentResolver().update(uri, values, null, null);
    }

    public int deleteWallet(Wallet wallet){
        int rowsDeleted = context.getContentResolver().delete(ContentUris.withAppendedId(WalletEntry.CONTENT_URI, wallet.getId()), null, null);
        new TransactionController(context).deleteAllTransactionWithWalletId(wallet.getId());
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

    private ContentValues convertWalletToContentValues(Wallet wallet){
        ContentValues ret = new ContentValues();
        ret.put(WalletEntry.COLUMN_NAME, wallet.getName());
        ret.put(WalletEntry.COLUMN_AMOUNT, wallet.getAmount());
        ret.put(WalletEntry.COLUMN_DESC, wallet.getDescription());
        return ret;
    }

    // operations from other entity's operation
    public int updateWalletFromInitialTransaction(Transaction transaction){
        Category category = new CategoryController(context).getCategoryById(transaction.getCategoryId());
        Wallet wallet = getWalletById(transaction.getWalletId());
        Wallet walletDest = getWalletById(transaction.getWalletDestId());

        if (category == null){ // transfer
            wallet.setAmount(wallet.getAmount() - transaction.getAmount());
            walletDest.setAmount(walletDest.getAmount() + transaction.getAmount());
        } else if (category.getType().equals(CategoryEntry.TYPE_EXPENSE)){
            wallet.setAmount(wallet.getAmount() - transaction.getAmount());
        } else { // income
            wallet.setAmount(wallet.getAmount() + transaction.getAmount());
        }

        int rowsUpdated = updateWallet(wallet);
        if (walletDest != null) updateWallet(walletDest);
        return rowsUpdated;
    }

    public void updateWalletFromUpdatedTransaction(Transaction transactionBefore, Transaction transactionAfter){

        Category category = new CategoryController(context).getCategoryById(transactionBefore.getCategoryId());

        if (category == null){ // Transfer

            // if wallet source changed
            if (transactionBefore.getWalletId() != transactionAfter.getWalletId()){

                updateWalletAmount(transactionBefore.getWalletId(), transactionBefore.getAmount());
                updateWalletAmount(transactionAfter.getWalletId(), -transactionAfter.getAmount());

            } else if (transactionBefore.getAmount() != transactionAfter.getAmount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWalletId(), transactionBefore.getAmount() - transactionAfter.getAmount());
            }

            // wallet destination changed
            if (transactionBefore.getWalletDestId() != transactionAfter.getWalletDestId()){

                updateWalletAmount(transactionBefore.getWalletDestId(), -transactionBefore.getAmount());
                updateWalletAmount(transactionAfter.getWalletDestId(), transactionAfter.getAmount());

            } else if (transactionBefore.getAmount() != transactionAfter.getAmount()){ // wallet dest not changed but amount changed
                updateWalletAmount(transactionAfter.getWalletDestId(), -transactionBefore.getAmount() + transactionAfter.getAmount());
            }

        } else if (category.getType().equals(CategoryEntry.TYPE_EXPENSE)){ // Expense

            if (transactionBefore.getWalletId() != transactionAfter.getWalletId()){

                updateWalletAmount(transactionBefore.getWalletId(), transactionBefore.getAmount());
                updateWalletAmount(transactionAfter.getWalletId(), -transactionAfter.getAmount());

            } else if (transactionBefore.getAmount() != transactionAfter.getAmount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWalletId(), transactionBefore.getAmount() - transactionAfter.getAmount());
            }

        } else { // Income

            if (transactionBefore.getWalletId() != transactionAfter.getWalletId()){

                updateWalletAmount(transactionBefore.getWalletId(), -transactionBefore.getAmount());
                updateWalletAmount(transactionAfter.getWalletId(), transactionAfter.getAmount());

            } else if (transactionBefore.getAmount() != transactionAfter.getAmount()){ // wallet source not changed but amount changed
                updateWalletAmount(transactionAfter.getWalletId(), -transactionBefore.getAmount() + transactionAfter.getAmount());
            }

        }

    }

    private void updateWalletAmount(long walletId, double amount){
        Wallet wallet = getWalletById(walletId);
        wallet.setAmount(wallet.getAmount() + amount);
        updateWallet(wallet);
    }

    public void updateWalletFromDeletedTransaction(Transaction transaction){
        Category category = new CategoryController(context).getCategoryById(transaction.getCategoryId());

        if (category == null) {

            updateWalletAmount(transaction.getWalletId(), transaction.getAmount());
            updateWalletAmount(transaction.getWalletDestId(), -transaction.getAmount());

        } else if (category.getType().equals(CategoryEntry.TYPE_EXPENSE)){
            updateWalletAmount(transaction.getWalletId(), transaction.getAmount());
        } else {
            updateWalletAmount(transaction.getWalletId(), -transaction.getAmount());
        }
    }

}
