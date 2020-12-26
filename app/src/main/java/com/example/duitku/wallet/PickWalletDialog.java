package com.example.duitku.wallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.WalletEntry;

public class PickWalletDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView pickWalletListView;
    private ImageView addWalletBtn;

    private WalletAdapter pickWalletAdapter;

    private PickWalletListener listener;

    private WalletController walletController;

    private final int WALLET_LOADER = 0;

    public PickWalletDialog(PickWalletListener listener){
        this.listener = listener;
        walletController = new WalletController(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_wallet, null);

        setUpListView(view);
        setUpAdapter();
        setUpAddBtn(view);

        builder.setView(view);

        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null ,this);


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;
    }

    private void setUpAdapter(){
        pickWalletAdapter = new WalletAdapter(getActivity(), null);
        pickWalletListView.setAdapter(pickWalletAdapter);
    }

    private void setUpListView(View view){
        pickWalletListView = view.findViewById(R.id.pick_wallet_listview);
        pickWalletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickWallet(id);
                dismiss();
            }
        });
    }

    private void setUpAddBtn(View view){
        addWalletBtn = view.findViewById(R.id.pick_wallet_add_btn);
        addWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });
    }

    private void addWallet(){
        AddWalletDialog addWalletDialog = new AddWalletDialog();
        addWalletDialog.show(getFragmentManager(), "Add Wallet Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case WALLET_LOADER:
                String[] projection = walletController.getFullProjection();
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
