package com.example.duitku.transaction.form.components;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duitku.R;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.category.pick.PickCategoryDialog;
import com.example.duitku.database.DuitkuContract.CategoryEntry;

public class TransactionCategoryComponent extends View {

    private ConstraintLayout categoryConstraintLayout;
    private TextView categoryTextView;
    private TextView errorCategoryTextView;

    private final View rootView;
    private AppCompatActivity activity = null;
    private Fragment fragment = null;
    private final String type;

    private final CategoryController categoryController;

    private long categoryId = -1;

    public TransactionCategoryComponent(Context context, View rootView, Object activity, String type) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity){
            this.activity = (AppCompatActivity) activity;
        } else {
            this.fragment = (Fragment) activity;
        }
        this.type = type;

        categoryController = new CategoryController(context);

        setUpUI();
    }

    private void setUpUI(){
        initialize();
        setUpComponents();
        setPickerBehaviour();
    }

    private void initialize(){
        if (rootView == null){
            categoryConstraintLayout = activity.findViewById(R.id.transaction_category_constraintlayout);
            categoryTextView = activity.findViewById(R.id.transaction_category_textview);
            errorCategoryTextView = activity.findViewById(R.id.transaction_category_error_textview);
        } else {
            categoryConstraintLayout = rootView.findViewById(R.id.transaction_category_constraintlayout);
            categoryTextView = rootView.findViewById(R.id.transaction_category_textview);
            errorCategoryTextView = rootView.findViewById(R.id.transaction_category_error_textview);
        }
    }

    private void setUpComponents(){
        if (type.equals(CategoryEntry.TYPE_TRANSFER)) {
            categoryConstraintLayout.setVisibility(View.GONE);
        }
        errorCategoryTextView.setVisibility(View.GONE);
    }

    private void setPickerBehaviour(){
        final PickCategoryDialog.PickCategoryListener categoryPickerListener = new PickCategoryDialog.PickCategoryListener() {
            @Override
            public void pickCategory(long id) {
                Category category = categoryController.getCategoryById(id);
                categoryTextView.setText(category.getCategory_name());
                changeTextViewToWhite();
                categoryId = id;
            }
        };
        categoryConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(categoryPickerListener, type);
                FragmentManager fm;
                if (activity == null){
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                pickCategoryDialog.show(fm, "View Categories Dialog");
            }
        });
    }

    private void changeTextViewToWhite(){
        if (fragment == null){
            categoryTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
        } else {
            categoryTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
        }
    }

    public boolean validateInput(){
        if (!type.equals(CategoryEntry.TYPE_TRANSFER) && categoryId == -1){
            errorCategoryTextView.setText("Category has to be chosen");
            errorCategoryTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCategoryTextView.setVisibility(View.GONE);
        }

        return true;
    }

    public long getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(long categoryId){
        this.categoryId = categoryId;
        Category category = categoryController.getCategoryById(categoryId);
        if (category == null) return;

        categoryTextView.setText(category.getCategory_name());
        changeTextViewToWhite();
    }
}
