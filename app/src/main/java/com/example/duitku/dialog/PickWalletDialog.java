package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.adapter.PickWalletAdapter;
import com.example.duitku.database.DuitkuContract.WalletEntry;

public class PickWalletDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WALLET_LOADER = 0;

    private PickWalletAdapter pickWalletAdapter;
    private ListView pickWalletListView;

    private Context mContext;
    private PickWalletListener listener;

    public PickWalletDialog(Object caller){
        super();
        listener = (PickWalletListener) caller;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // bikin builderny
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_wallet, null);

        // set listview dan adapter nya
        pickWalletListView = view.findViewById(R.id.pick_wallet_listview);
        pickWalletAdapter = new PickWalletAdapter(getContext(), null);
        pickWalletListView.setAdapter(pickWalletAdapter);

        pickWalletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickWallet(id);
                dismiss();
            }
        });

        builder.setView(view);

        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null ,this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case WALLET_LOADER:
                String[] projection = new String[]{WalletEntry.COLUMN_ID, WalletEntry.COLUMN_NAME};
                return new CursorLoader(getContext(), WalletEntry.CONTENT_URI, projection, null, null, null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pickWalletAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        pickWalletAdapter.swapCursor(null);
    }

    public interface PickWalletListener{
        void pickWallet(long id);
    }

}
