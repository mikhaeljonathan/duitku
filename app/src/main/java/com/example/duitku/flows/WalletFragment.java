package com.example.duitku.flows;

import com.example.duitku.controller.WalletController;
import com.example.duitku.database.DuitkuContract.WalletEntry;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.duitku.view.WalletFragmentView;

public class WalletFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private WalletController walletController = new WalletController(getActivity());
    private WalletFragmentView walletFragmentView;

    private final int WALLET_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        walletFragmentView = new WalletFragmentView(inflater, container, this);
        walletFragmentView.setUpUI();

        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null, this);

        return walletFragmentView.getView();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id){
            case WALLET_LOADER:
                String[] projection = walletController.getProjection();
                return new CursorLoader(getContext(), WalletEntry.CONTENT_URI, projection,null,null,null);
            default:
                throw new IllegalStateException("Unknown Loader");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        walletFragmentView.updateTotalWalletTextView(walletController.calculateTotalAmount(data));
        walletFragmentView.getAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        walletFragmentView.getAdapter().swapCursor(null);
    }

}
