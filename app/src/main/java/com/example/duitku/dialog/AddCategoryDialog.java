package com.example.duitku.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.duitku.R;
import com.example.duitku.controller.CategoryController;
import com.example.duitku.model.Category;

public class AddCategoryDialog extends AppCompatDialogFragment {

    private EditText categoryNameEditText;
    private EditText categoryTypeEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // bikin builderny
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);

        // initialize view nya
        categoryNameEditText = view.findViewById(R.id.add_category_name_edittext);
        categoryTypeEditText = view.findViewById(R.id.add_category_type_edittext);

        // set buildernya
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // ambil value
                        String categoryName = categoryNameEditText.getText().toString().trim();
                        String categoryType = categoryTypeEditText.getText().toString().trim();

                        // insrt ke database
                        Category category = new Category(categoryName, categoryType);
                        Uri uri = new CategoryController(getContext()).addCategory(category);

                        if (uri == null){
                            Toast.makeText(getContext(), "Error adding new category", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        // buat judulnya biar putih
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.WHITE);
        tv.setText("Add Category");
        tv.setPadding(50, 50, 50, 50);
        tv.setTextSize(20F);
        builder.setCustomTitle(tv);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;

    }
}
