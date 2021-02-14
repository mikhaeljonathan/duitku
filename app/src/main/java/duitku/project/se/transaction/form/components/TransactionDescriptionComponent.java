package duitku.project.se.transaction.form.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import duitku.project.se.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TransactionDescriptionComponent extends View {

    private TextInputLayout descLayout;
    private TextInputEditText descField;

    private final View rootView;
    private AppCompatActivity activity = null;

    private String desc = null;

    public TransactionDescriptionComponent(Context context, View rootView, Object activity) {
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
            descLayout = activity.findViewById(R.id.transaction_desc_textinputlayout);
            descField = activity.findViewById(R.id.transaction_desc_edittext);
        } else {
            descLayout = rootView.findViewById(R.id.transaction_desc_textinputlayout);
            descField = rootView.findViewById(R.id.transaction_desc_edittext);
        }
    }

    private void setUpComponents() {
        descField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 50) {
                    descLayout.setError("Description max 50 characters");
                } else {
                    descLayout.setErrorEnabled(false);
                }
            }
        });
    }

    public boolean validateInput() {
        desc = descField.getText().toString().trim();
        return desc.length() <= 50;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        descField.setText(desc);
    }
}
