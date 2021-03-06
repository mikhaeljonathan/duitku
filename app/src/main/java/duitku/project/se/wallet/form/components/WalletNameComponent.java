package duitku.project.se.wallet.form.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.R;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class WalletNameComponent extends View {

    private TextInputLayout nameLayout;
    private TextInputEditText nameField;

    private final View rootView;
    private AppCompatActivity activity = null;

    private String nameBefore = null;
    private String name = null;

    public WalletNameComponent(Context context, View rootView, Object activity) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) activity;
        }

        setUpUI();
    }

    private void setUpUI() {
        initialize();
        setUpComponents();
    }

    private void initialize() {
        if (rootView == null) {
            nameLayout = activity.findViewById(R.id.wallet_name_textinputlayout);
            nameField = activity.findViewById(R.id.wallet_name_field);
        } else {
            nameLayout = rootView.findViewById(R.id.wallet_name_textinputlayout);
            nameField = rootView.findViewById(R.id.wallet_name_field);
        }
    }

    private void setUpComponents() {
        nameField.requestFocus();
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 20) {
                    nameLayout.setError("Wallet name max 20 characters");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
    }

    public boolean validateInput() {
        name = nameField.getText().toString().trim();
        if (name.equals("")) {
            nameLayout.setError("Wallet name can't be empty");
            return false;
        }

        if (name.length() > 20) {
            return false;
        }

        WalletController walletController = new WalletController(getContext());

        Wallet wallet = walletController.getWalletByName(name);
        if (wallet != null && nameBefore == null) {
            nameLayout.setError("There is a wallet with this name");
            return false;
        }

        if (nameBefore != null && !nameBefore.equalsIgnoreCase(name)) {
            if (walletController.getWalletByName(name) != null) {
                nameLayout.setError("There is a wallet with this name");
                return false;
            }
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.nameBefore = name;
        this.name = name;
        nameField.setText(name);
    }
}
