package duitku.project.se.wallet.pick;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import duitku.project.se.R;

import duitku.project.se.wallet.fragment.WalletAdapter;
import duitku.project.se.wallet.WalletController;
import duitku.project.se.wallet.add.AddWalletDialog;
import duitku.project.se.database.DuitkuContract;

public class PickWalletDialog extends AppCompatDialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private WalletAdapter pickWalletAdapter;

    private final PickWalletListener listener;
    private View view;

    private final int WALLET_LOADER = 0;

    public PickWalletDialog(PickWalletListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setUpUI();

        builder.setView(view);

        LoaderManager.getInstance(this).initLoader(WALLET_LOADER, null, this);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;
    }

    private void setUpUI() {
        setUpViews();
        setUpListView();
        setUpEmptyView();
        setUpButtons();
    }

    private void setUpViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_pick,
                (ViewGroup) getActivity().findViewById(R.id.dialog_pick_constraintlayout));

        TextView titleTV = view.findViewById(R.id.dialog_pick_title);
        titleTV.setText("Pick Wallet");
    }

    private void setUpListView() {
        GridView gridView = view.findViewById(R.id.dialog_pick_gridview);
        gridView.setVisibility(View.GONE);

        listView = view.findViewById(R.id.dialog_pick_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listener.pickWallet(id);
                dismiss();
            }
        });

        setUpAdapter();
    }

    private void setUpEmptyView() {
        View emptyView = view.findViewById(R.id.dialog_pick_emptyview);

        ImageView imageEmptyView = emptyView.findViewById(R.id.dialog_pick_empty_imageview);
        imageEmptyView.setImageResource(R.drawable.empty_wallet);

        TextView textView = emptyView.findViewById(R.id.dialog_pick_empty_textview);
        textView.setText("There is no wallet\nTry adding a new one");

        listView.setEmptyView(emptyView);
    }


    private void setUpAdapter() {
        pickWalletAdapter = new WalletAdapter(getActivity(), null);
        listView.setAdapter(pickWalletAdapter);
    }

    private void setUpButtons() {
        setUpAddBtn();
    }

    private void setUpAddBtn() {
        ImageView addWalletBtn = view.findViewById(R.id.dialog_pick_add_btn);
        addWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWallet();
            }
        });
    }

    private void addWallet() {
        AddWalletDialog addWalletDialog = new AddWalletDialog();
        addWalletDialog.show(getFragmentManager(), "Add Wallet Dialog");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == WALLET_LOADER) {
            String[] projection = new WalletController(getActivity()).getFullProjection();
            return new CursorLoader(getContext(), DuitkuContract.WalletEntry.CONTENT_URI, projection, null, null, null);
        }
        throw new IllegalStateException("Unknown Loader");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pickWalletAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        pickWalletAdapter.swapCursor(null);
    }

    public interface PickWalletListener {
        void pickWallet(long id);
    }

}
