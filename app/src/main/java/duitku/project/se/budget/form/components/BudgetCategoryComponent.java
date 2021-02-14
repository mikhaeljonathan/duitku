package duitku.project.se.budget.form.components;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import duitku.project.se.R;
import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.category.pick.PickCategoryDialog;
import duitku.project.se.database.DuitkuContract;

public class BudgetCategoryComponent extends View {

    private ConstraintLayout categoryConstraintLayout;
    private TextView categoryTextView;
    private TextView errorCategoryTextView;

    private final View rootView;
    private AppCompatActivity activity = null;
    private Fragment fragment = null;

    private final CategoryController categoryController;

    private long categoryIdBefore = -1;
    private long categoryId = -1;

    public BudgetCategoryComponent(Context context, View rootView, Object activity) {
        super(context);

        this.rootView = rootView;
        if (activity instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) activity;
        } else {
            this.fragment = (Fragment) activity;
        }

        categoryController = new CategoryController(context);

        setUpUI();
    }

    private void setUpUI() {
        initialize();
        setUpComponents();
        setPickerBehaviour();
    }

    private void initialize() {
        if (rootView == null) {
            categoryConstraintLayout = activity.findViewById(R.id.budget_category_constraintlayout);
            categoryTextView = activity.findViewById(R.id.budget_category_textview);
            errorCategoryTextView = activity.findViewById(R.id.budget_category_error_textview);
        } else {
            categoryConstraintLayout = rootView.findViewById(R.id.budget_category_constraintlayout);
            categoryTextView = rootView.findViewById(R.id.budget_category_textview);
            errorCategoryTextView = rootView.findViewById(R.id.budget_category_error_textview);
        }
    }

    private void setUpComponents() {
        errorCategoryTextView.setVisibility(View.GONE);
    }

    private void setPickerBehaviour() {
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
                // yang dibudget pasti expense
                PickCategoryDialog pickCategoryDialog = new PickCategoryDialog(categoryPickerListener, DuitkuContract.CategoryEntry.TYPE_EXPENSE);
                FragmentManager fm;
                if (activity == null) {
                    fm = fragment.getFragmentManager();
                } else {
                    fm = activity.getSupportFragmentManager();
                }
                pickCategoryDialog.show(fm, "View Categories Dialog");
            }
        });
    }

    private void changeTextViewToWhite() {
        if (fragment == null) {
            categoryTextView.setTextColor(activity.getResources().getColor(android.R.color.white));
        } else {
            categoryTextView.setTextColor(fragment.getResources().getColor(android.R.color.white));
        }
    }

    public boolean validateInput() {
        if (categoryId == -1) {
            errorCategoryTextView.setText("Category has to be chosen");
            errorCategoryTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCategoryTextView.setVisibility(View.GONE);
        }

        Budget budget = new BudgetController(getContext()).getBudgetByCategoryId(categoryId);
        if (budget != null && categoryIdBefore != categoryId) {
            errorCategoryTextView.setText("There is a budget with this category");
            errorCategoryTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCategoryTextView.setVisibility(View.GONE);
        }

        return true;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryIdBefore = categoryId;
        this.categoryId = categoryId;
        Category category = categoryController.getCategoryById(categoryId);
        categoryTextView.setText(category.getCategory_name());
        changeTextViewToWhite();
    }

}
