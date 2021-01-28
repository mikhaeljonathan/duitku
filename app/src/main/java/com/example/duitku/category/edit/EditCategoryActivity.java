package com.example.duitku.category.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.form.CategoryForm;

public class EditCategoryActivity extends AppCompatActivity {

    private CategoryForm categoryForm;
    private Button deleteBtn;
    private Button saveBtn;

    private CategoryController categoryController;

    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryController = new CategoryController(this);
        category = categoryController.getCategoryById(getIntent().getLongExtra("ID", -1));

        setUpViews();
        setUpForm();
        setUpButtons();

        hideView();
    }

    private void setUpViews(){
        setContentView(R.layout.activity_edit);

        TextView titleTV = findViewById(R.id.activity_edit_title);
        titleTV.setText("Edit Category");

        LinearLayout walletFormContainer = findViewById(R.id.activity_edit_form);
        walletFormContainer.addView(getLayoutInflater().inflate(R.layout.form_category,
                (ViewGroup) findViewById(R.id.form_category_constraintlayout)));

        deleteBtn = findViewById(R.id.activity_edit_delete);
        deleteBtn.setText("Delete Category");
    }

    private void setUpForm(){
        categoryForm = new CategoryForm(this, null, this, category.getCategory_type());
        categoryForm.setUpUI();
        categoryForm.setName(category.getCategory_name());
    }

    private void setUpButtons(){
        setUpSaveButton();
        setUpDeleteButton();
        setUpBackButton();
    }

    private void setUpSaveButton(){
        saveBtn = findViewById(R.id.category_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!categoryForm.validateInput()) return;
                int rowsUpdated = updateCategory();
                if (rowsUpdated == 0){
                    Toast.makeText(EditCategoryActivity.this, "Error editing category", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditCategoryActivity.this, "Category edited", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private int updateCategory() {
        String name = categoryForm.getName();

        category.setCategory_name(name);

        return categoryController.updateCategory(category);
    }

    private void setUpDeleteButton(){
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Delete Confirmation");
        alertDialogBuilder.setMessage("Are you sure to delete this category?\nAll transactions with this category and budget with this category are deleted too.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCategory();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary); //biar bg gelap
        alertDialog.show();
    }

    private void deleteCategory(){
        int rowsDeleted = categoryController.deleteCategory(category);
        if (rowsDeleted == 0){
            Toast.makeText(EditCategoryActivity.this, "Error deleting category", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditCategoryActivity.this, "Category is deleted", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void setUpBackButton(){
        ImageButton backBtn = findViewById(R.id.activity_edit_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void hideView(){
        if (category.getCategory_name().equalsIgnoreCase("others")){
            saveBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            categoryForm.disableNameField();
        }
    }

}