package duitku.project.se.search;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract.TransactionEntry;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionAdapter;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.transaction.view.ViewTransactionDialog;

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
        setUpEmptyView();

        LoaderManager.getInstance(this).initLoader(SEARCH_LOADER, null, this);
    }

    private void setUpBackBtn() {
        ImageView backBtn = findViewById(R.id.activity_search_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpSearchEditText() {
        EditText searchField = findViewById(R.id.activity_search_search_edittext);
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

    private void setUpListView() {
        listView = findViewById(R.id.activity_search_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Transaction transaction = adapter.getTransaction(i);
                viewTransaction(transaction.get_id());
            }
        });
    }

    private void setUpEmptyView() {
        View emptyView = findViewById(R.id.activity_search_emptyview);
        listView.setEmptyView(emptyView);
    }

    private void viewTransaction(long id) {
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
            if (pattern.length() > 0) {
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