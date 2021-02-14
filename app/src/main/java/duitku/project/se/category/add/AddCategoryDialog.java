package duitku.project.se.category.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import duitku.project.se.R;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.category.form.CategoryForm;
import duitku.project.se.database.DuitkuContract;

public class AddCategoryDialog extends AppCompatDialogFragment {

    private CategoryForm categoryForm;
    private View view;

    private final String categoryType;

    public AddCategoryDialog(String categoryType) {
        this.categoryType = categoryType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setUpUI();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        return dialog;
    }

    private void setUpUI() {
        setUpViews();
        setUpForm();
        setUpButtons();
    }

    private void setUpViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add,
                (ViewGroup) getActivity().findViewById(R.id.dialog_add_constraintlayout));

        setUpTitle();

        LinearLayout categoryFormContainer = view.findViewById(R.id.dialog_add_form);
        categoryFormContainer.addView(inflater.inflate(R.layout.form_category,
                (ViewGroup) getActivity().findViewById(R.id.form_category_constraintlayout)));

        Button addBtn = view.findViewById(R.id.category_save_btn);
        addBtn.setText("Add");
    }

    private void setUpTitle() {
        TextView titleTV = view.findViewById(R.id.dialog_add_title);
        if (categoryType.equals(DuitkuContract.CategoryEntry.TYPE_EXPENSE)) {
            titleTV.setText("Add Expense Category");
        } else {
            titleTV.setText("Add Income Category");
        }
    }

    private void setUpForm() {
        categoryForm = new CategoryForm(getActivity(), view, this, categoryType);
        categoryForm.setUpUI();
    }

    private void setUpButtons() {
        setUpAddBtn();
    }

    private void setUpAddBtn() {
        Button addButton = view.findViewById(R.id.category_save_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!categoryForm.validateInput()) return;
                Uri uri = addCategory();
                if (uri == null) {
                    Toast.makeText(getContext(), "Error adding new category", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }

    private Uri addCategory() {
        String name = categoryForm.getName();

        Category category = new Category(-1, name, categoryType);
        return new CategoryController(getActivity()).addCategory(category);
    }

}
