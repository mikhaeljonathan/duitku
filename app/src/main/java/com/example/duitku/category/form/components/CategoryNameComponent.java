package com.example.duitku.category.form.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CategoryNameComponent extends View {

    private TextInputLayout nameLayout;
    private TextInputEditText nameField;

    private final View rootView;
    private AppCompatActivity activity = null;
    private final String categoryType;

    private String nameBefore = null;
    private String name = null;

    public CategoryNameComponent(Context context, View rootView, Object activity, String categoryType) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity){
            this.activity = (AppCompatActivity) activity;
        }
        this.categoryType = categoryType;

        setUpUI();
    }

    private void setUpUI(){
        initialize();
        setUpComponents();
    }

    private void initialize(){
        if (rootView == null){
            nameLayout = activity.findViewById(R.id.category_name_textinputlayout);
            nameField = activity.findViewById(R.id.category_name_edittext);
        } else {
            nameLayout = rootView.findViewById(R.id.category_name_textinputlayout);
            nameField = rootView.findViewById(R.id.category_name_edittext);
        }
    }

    private void setUpComponents(){
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
                if (editable.toString().length() > 20){
                    nameLayout.setError("Category name too long");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
    }

    public boolean validateInput(){
        name = nameField.getText().toString().trim();
        if (name.equals("")){
            nameLayout.setError("Category name can't be empty");
            return false;
        }

        if (name.length() > 20){
            return false;
        }

        CategoryController categoryController = new CategoryController(getContext());

        Category category = categoryController.getCategoryByNameAndType(name, categoryType);
        if (category != null && nameBefore == null){
            nameLayout.setError("There is a category with this name");
            return false;
        }

        if (nameBefore != null && !nameBefore.equalsIgnoreCase(name)){
            if (categoryController.getCategoryByNameAndType(name, categoryType) != null){
                nameLayout.setError("There is a wallet with this name");
                return false;
            }
        }

        return true;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.nameBefore = name;
        this.name = name;
        nameField.setText(name);
    }

    public void disableField(){
        nameField.setEnabled(false);
        nameLayout.setError("This is a default category\nCan't delete or edit");
    }
}
