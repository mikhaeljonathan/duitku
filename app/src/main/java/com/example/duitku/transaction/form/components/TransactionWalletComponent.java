package com.example.duitku.transaction.form.components;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.example.duitku.wallet.pick.PickWalletDialog;

public class TransactionWalletComponent extends View {

    private ConstraintLayout walletConstraintLayout;
    private TextView walletTextView;
    private TextView walletErrorTextView;
    private ConstraintLayout walletDestConstraintLayout;
    private TextView walletDestTextView;
    private TextView walletDestErrorTextView;

    private final WalletController walletController;

    private final View rootView;
    private AppCompatActivity activity = null;
    private Fragment fragment = null;
    private final String type;

    private long walletId = -1;
    private long walletDestId = -1;

    public TransactionWalletComponent(Context context, View rootView, Object activity, String type) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) activity;
        } else {
            this.fragment = (Fragment) activity;
        }
        this.type = type;

        walletController = new WalletController(context);

        setUpUI();
    }

    private void setUpUI() {
        initialize();
        setUpComponents();
        setPickerBehaviour();
    }

    private void initialize() {
        if (rootView == null) {
            walletConstraintLayout = activity.findViewById(R.id.transaction_wallet_constraintlayout);
            walletTextView = activity.findViewById(R.id.transaction_wallet_textview);
            walletErrorTextView = activity.findViewById(R.id.transaction_wallet_error_textview);
            walletDestConstraintLayout = activity.findViewById(R.id.transaction_walletdest_constraintlayout);
            walletDestTextView = activity.findViewById(R.id.transaction_walletdest_textview);
            walletDestErrorTextView = activity.findViewById(R.id.transaction_walletdest_error_textview);
        } else {
            walletConstraintLayout = rootView.findViewById(R.id.transaction_wallet_constraintlayout);
            walletTextView = rootView.findViewById(R.id.transaction_wallet_textview);
            walletErrorTextView = rootView.findViewById(R.id.transaction_wallet_error_textview);
            walletDestConstraintLayout = rootView.findViewById(R.id.transaction_walletdest_constraintlayout);
            walletDestTextView = rootView.findViewById(R.id.transaction_walletdest_textview);
            walletDestErrorTextView = rootView.findViewById(R.id.transaction_walletdest_error_textview);
        }
    }

    private void setUpComponents() {
        if (!type.equals(CategoryEntry.TYPE_TRANSFER)) {
            walletDestConstraintLayout.setVisibility(View.GONE);
        }
        walletErrorTextView.setVisibility(View.GONE);
        walletDestErrorTextView.setVisibility(View.GONE);
    }

    private void setPickerBehaviour() {
        setWalletSourceBehavior();
        setWalletDestBehavior();
    }

    private void setWalletSourceBehavior() {
        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletTextView.setText(wallet.getWallet_name());
                changeTextViewToWhite(walletTextView);
                walletId = id;
            }
        };
        walletConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                FragmentManager fm;
                if (activity == null) {
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                pickWalletDialog.show(fm, "Pick Wallet Dialog");
            }
        });
    }

    private void setWalletDestBehavior() {
        final PickWalletDialog.PickWalletListener pickWalletListener = new PickWalletDialog.PickWalletListener() {
            @Override
            public void pickWallet(long id) {
                Wallet wallet = walletController.getWalletById(id);
                walletDestTextView.setText(wallet.getWallet_name());
                changeTextViewToWhite(walletDestTextView);
                walletDestId = id;
            }
        };
        walletDestConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickWalletDialog pickWalletDialog = new PickWalletDialog(pickWalletListener);
                FragmentManager fm;
                if (activity == null) {
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                pickWalletDialog.show(fm, "Pick Wallet Dest Dialog");
            }
        });
    }

    private void changeTextViewToWhite(TextView view) {
        if (fragment == null) {
            view.setTextColor(activity.getResources().getColor(android.R.color.white));
        } else {
            view.setTextColor(fragment.getResources().getColor(android.R.color.white));
        }
    }

    public boolean validateInput() {
        if (walletId == -1) {
            walletErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletErrorTextView.setVisibility(View.GONE);
        }

        // wallet dest
        if (type.equals(CategoryEntry.TYPE_TRANSFER) && walletDestId == -1) {
            walletDestErrorTextView.setText("Wallet destination has to be chosen");
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        // wallet and walletdest
        if (type.equals(CategoryEntry.TYPE_TRANSFER) && walletId == walletDestId) {
            walletDestErrorTextView.setText("Wallet source can't be same\nwith wallet destination");
            walletDestErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            walletDestErrorTextView.setVisibility(View.GONE);
        }

        return true;
    }

    public long getWalletId() {
        return walletId;
    }

    public long getWalletDestId() {
        return walletDestId;
    }

    public void setWalletId(long id) {
        Wallet wallet = walletController.getWalletById(id);
        walletTextView.setText(wallet.getWallet_name());
        changeTextViewToWhite(walletTextView);
        walletId = id;
    }

    public void setWalletDestId(long id) {
        Wallet wallet = walletController.getWalletById(id);
        if (wallet == null) return;

        walletDestTextView.setText(wallet.getWallet_name());
        changeTextViewToWhite(walletDestTextView);
        walletDestId = id;
    }


}
