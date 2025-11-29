package demo;

public class Transaction {

    private String type;
    private double amount;
    private double resultingBalance;

    public Transaction(String type, double amount, double resultingBalance) {
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getResultingBalance() {
        return resultingBalance;
    }
}
