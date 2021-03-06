package duitku.project.se.category.form;

import android.content.Context;
import android.view.View;

import duitku.project.se.category.form.components.CategoryNameComponent;

public class CategoryForm extends View {

    private CategoryNameComponent categoryNameComponent;

    private View rootView;
    private Object activity;
    private String categoryType;

    public CategoryForm(Context context, View rootView, Object activity, String categoryType) {
        super(context);
        this.categoryType = categoryType;
        this.rootView = rootView;
        this.activity = activity;
    }

    public void setUpUI() {
        setUpFields();
    }

    private void setUpFields() {
        categoryNameComponent = new CategoryNameComponent(getContext(), rootView, activity, categoryType);
    }

    public boolean validateInput() {
        return categoryNameComponent.validateInput();
    }

    public String getName() {
        return categoryNameComponent.getName();
    }

    public void setName(String name) {
        categoryNameComponent.setName(name);
    }

    public void disableNameField() {
        categoryNameComponent.disableField();
    }

}
