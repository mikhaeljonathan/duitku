package duitku.project.se.report;

public class Report {

    private long categoryId;
    private double amount;
    private double percentage;

    public Report(long categoryId, double amount, double mPercentage) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.percentage = mPercentage;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }
}
