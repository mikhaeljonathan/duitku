package duitku.project.se.wallet;

public class Wallet {

    private long _id;
    private String wallet_name;
    private double wallet_amount;
    private String wallet_desc;

    public Wallet() {

    }

    public Wallet(long _id, String wallet_name, double wallet_amount, String wallet_desc) {
        this._id = _id;
        this.wallet_name = wallet_name;
        this.wallet_amount = wallet_amount;
        this.wallet_desc = wallet_desc;
    }

    public long get_id() {
        return _id;
    }

    public String getWallet_name() {
        return wallet_name;
    }

    public double getWallet_amount() {
        return wallet_amount;
    }

    public String getWallet_desc() {
        return wallet_desc;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public void setWallet_amount(double wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public void setWallet_desc(String wallet_desc) {
        this.wallet_desc = wallet_desc;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
}
