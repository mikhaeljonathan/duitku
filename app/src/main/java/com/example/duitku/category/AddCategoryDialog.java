package com.example.duitku.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddCategoryDialog extends AppCompatDialogFragment {

    private TextView titleTextView;
    private TextInputLayout nameLayout;
    private TextInputEditText nameField;
    private Button addButton;

    private String name;

    private CategoryController categoryController;

    private String categoryType;

    public AddCategoryDialog(String categoryType){
        this.categoryType = categoryType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);

        setUpTitle(view);
        setUpUI(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;
    }

    private void setUpTitle(View view){
        titleTextView = view.findViewById(R.id.add_category_title_textview);
        if (categoryType.equals(CategoryEntry.TYPE_EXPENSE)) {
            titleTextView.setText("Add Expense Category");
        } else {
            titleTextView.setText("Add Income Category");
        }
    }

    private void setUpUI(View view){
        nameLayout = view.findViewById(R.id.add_category_name_textinputlayout);
        nameField = view.findViewById(R.id.add_category_name_edittext);
        addButton = view.findViewById(R.id.add_category_add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateInput()) return;
                Uri uri = addCategory();
                if (uri == null){
                    Toast.makeText(getContext(), "Error adding new category", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private boolean validateInput(){
        name = nameField.getText().toString().trim();
        if (name.equals("")){
            nameLayout.setError("Category name can't be empty");
            return false;
        }
        Category category = categoryController.getCategoryByNameAndType(name, categoryType);
        if (category != null){
            nameLayout.setError("There is a category with this name");
            return false;
        }

        return true;
    }

    private Uri addCategory(){
        Category category = new Category(-1, name, categoryType);
        Uri uri = categoryController.addCategory(category);
        return uri;
    }

    // kalo gaada onAttach nanti getActivity() atau getContext() bisa null
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.categoryController = new CategoryController(context);
    }
}
