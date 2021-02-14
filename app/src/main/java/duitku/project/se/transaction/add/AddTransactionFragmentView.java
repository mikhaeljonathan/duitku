package duitku.project.se.transaction.add;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import duitku.project.se.R;
import duitku.project.se.database.DuitkuContract;
import duitku.project.se.interfaces.UIView;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.transaction.form.TransactionForm;
import duitku.project.se.user.UserController;
import duitku.project.se.wallet.WalletController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Date;
import java.util.Random;

public class AddTransactionFragmentView implements UIView {

    private InterstitialAd interstitialAd;

    private TransactionForm transactionForm;

    private final LayoutInflater inflater;
    private final ViewGroup container;
    private View view;
    private final Fragment fragment;

    private String type;

    public AddTransactionFragmentView(LayoutInflater inflater, ViewGroup container, Fragment fragment, String type) {
        this.inflater = inflater;
        this.container = container;
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    public void setUpUI() {
        initInterstitialAd();
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void initInterstitialAd() {
        interstitialAd = new InterstitialAd(fragment.getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-2097480504227856/4553088954");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void setUpViews() {
        this.view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }

    private void setUpForm() {
        transactionForm = new TransactionForm(fragment.getActivity(), view, fragment, type);
        transactionForm.setUpUI();
    }

    private void setUpButtons() {
        setUpAddBtn();
    }

    private void setUpAddBtn() {
        Button addBtn = view.findViewById(R.id.transaction_save_btn);
        addBtn.setText("Add");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!transactionForm.validateInput()) return;
                Uri uri = addTransaction();
                if (uri == null) {
                    Toast.makeText(fragment.getActivity(), "Error adding new transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(fragment.getActivity(), "Transaction added", Toast.LENGTH_SHORT).show();
                }
                showInterstitialAd();
                fragment.getActivity().finish();
            }
        });
    }

    private Uri addTransaction() {
        long walletId = transactionForm.getWalletId();
        long walletDestId = transactionForm.getWalletDestId();
        long categoryId = transactionForm.getCategoryId();
        String desc = transactionForm.getDesc();
        Date date = transactionForm.getDate();
        double amount = transactionForm.getAmount();

        Transaction transaction = new Transaction(-1, walletId, walletDestId, categoryId, desc, date, amount);
        Uri uri = new TransactionController(fragment.getActivity()).addTransaction(transaction);

        new WalletController(fragment.getActivity()).updateWalletFromInitialTransaction(transaction);

        return uri;
    }

    private void showInterstitialAd() {
        if (new UserController(fragment.getActivity()).getUser().getUser_status().equals(DuitkuContract.UserEntry.TYPE_PREMIUM)) {
            return;
        }
        if (new Random().nextInt(2) == 0) {
            interstitialAd.show();
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
