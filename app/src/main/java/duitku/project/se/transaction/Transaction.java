package duitku.project.se.transaction;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable { // serializable biar bisa dipass ke activity

    private long _id;
    private long wallet_id;
    private long walletdest_id;
    private long category_id;
    private String transaction_desc;
    private Date transaction_date;
    private double transaction_amount;

    public Transaction() {

    }

    public Transaction(long _id, long wallet_id, long walletdest_id, long category_id, String transaction_desc, Date transaction_date, double transaction_amount) {
        this._id = _id;
        this.wallet_id = wallet_id;
        this.walletdest_id = walletdest_id;
        this.category_id = category_id;
        this.transaction_desc = transaction_desc;
        this.transaction_date = transaction_date;
        this.transaction_amount = transaction_amount;
    }

    public long get_id() {
        return _id;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public long getWallet_id() {
        return wallet_id;
    }

    public long getWalletdest_id() {
        return walletdest_id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public String getTransaction_desc() {
        return transaction_desc;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setWallet_id(long wallet_id) {
        this.wallet_id = wallet_id;
    }

    public void setWalletdest_id(long walletdest_id) {
        this.walletdest_id = walletdest_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public void setTransaction_desc(String transaction_desc) {
        this.transaction_desc = transaction_desc;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public static Transaction clone(Transaction transaction) {
        long id = transaction.get_id();
        long walletId = transaction.getWallet_id();
        long walletDestId = transaction.getWalletdest_id();
        long categoryId = transaction.getCategory_id();
        String desc = transaction.getTransaction_desc();
        Date date = transaction.getTransaction_date();
        double amount = transaction.getTransaction_amount();

        Transaction ret = new Transaction(id, walletId, walletDestId, categoryId, desc, date, amount);
        return ret;
    }
}
