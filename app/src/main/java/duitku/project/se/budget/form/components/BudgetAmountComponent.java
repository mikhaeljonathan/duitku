package duitku.project.se.budget.form.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class BudgetAmountComponent extends View {

    private TextInputLayout amountLayout;
    private TextInputEditText amountField;

    private final View rootView;
    private AppCompatActivity activity = null;

    private double amount = 0;

    public BudgetAmountComponent(Context context, View rootView, Object activity) {
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
            amountLayout = activity.findViewById(R.id.budget_amount_textinputlayout);
            amountField = activity.findViewById(R.id.budget_amount_field);
        } else {
            amountLayout = rootView.findViewById(R.id.budget_amount_textinputlayout);
            amountField = rootView.findViewById(R.id.budget_amount_field);
        }
    }

    private void setUpComponents() {
        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 9) {
                    amountLayout.setError("Amount too much");
                } else {
                    amountLayout.setErrorEnabled(false);
                }
            }
        });
    }

    public boolean validateInput() {
        String amountString = amountField.getText().toString().trim();

        if (amountString.equals("")) {
            amountLayout.setError("Amount can't be empty");
            return false;
        } else {
            amount = Double.parseDouble(amountString);
        }

        if (amount <= 0) {
            amountLayout.setError("Amount not allowed");
            return false;
        }

        return !(amount > 999999999);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        amountField.setText(new DecimalFormat("#").format(amount));
    }

}
