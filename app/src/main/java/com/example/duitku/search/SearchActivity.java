package com.example.duitku.search;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionAdapter;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.transaction.view.ViewTransactionDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final TransactionController transactionController = new TransactionController(this);

    private String pattern = "";
    private final int SEARCH_LOADER = 0;

    private TransactionAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpBackBtn();
        setUpSearchEditText();
        setUpListView();

        LoaderManager.getInstance(this).initLoader(SEARCH_LOADER, null, this);
    }

    private void setUpBackBtn(){
        ImageView backBtn = findViewById(R.id.activity_search_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpSearchEditText(){
        EditText searchField = findViewById(R.id.activity_search_search_edittext);
        searchField.setFocusable(true);
        searchField.requestFocus();
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pattern = charSequence.toString();
                LoaderManager.getInstance(SearchActivity.this).restartLoader(SEARCH_LOADER, null, SearchActivity.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setUpListView(){
        listView = findViewById(R.id.activity_search_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction transaction = adapter.getTransaction(i);
                viewTransaction(transaction.getId());
            }
        });
    }

    private void viewTransaction(long id){
        ViewTransactionDialog viewTransactionDialog = new ViewTransactionDialog(id);
        viewTransactionDialog.show(getSupportFragmentManager(), "View Transaction Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == SEARCH_LOADER) {
            String[] projection = transactionController.getFullProjection();
            String selection = TransactionEntry.COLUMN_DESC + " LIKE ? COLLATE NOCASE";
            String[] selectionArgs;
            if (pattern.length() > 0){
                selectionArgs = new String[]{"%" + pattern + "%"};
            } else { // kalau kosong gausa tampilin apapun, jadi pilih regex yang ga mungkin match dengan apapun
                selectionArgs = new String[]{"%" + "\b" + "%"};
            }
            return new CursorLoader(this, TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter = new TransactionAdapter(this,
                transactionController.convertCursorToListOfTransaction(data), null);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}