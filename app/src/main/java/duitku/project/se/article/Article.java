package duitku.project.se.article;

public class Article {
    private String title;
    private String address;

    public Article(){

    }

    public Article(String title, String address) {
        this.title = title;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

}
