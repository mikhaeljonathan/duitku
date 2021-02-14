package duitku.project.se.category;

public class Category {

    private long _id;
    private String category_name;
    private String category_type;

    public Category() {

    }

    public Category(long _id, String category_name, String category_type) {
        this._id = _id;
        this.category_name = category_name;
        this.category_type = category_type;
    }

    public long get_id() {
        return _id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }
}
