package com.example.duitku.wallet.fragment;

import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.wallet.WalletController;

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

public class WalletFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final WalletController walletController = new WalletController(getActivity());
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
        if (id == WALLET_LOADER) {
            String[] projection = walletController.getFullProjection();
            return new CursorLoader(getActivity(), WalletEntry.CONTENT_URI, projection, null, null, null);
        }
        throw new IllegalStateException("Unknown Loader");
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
