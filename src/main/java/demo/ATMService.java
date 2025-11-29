package demo;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ATMService {

    private double balance = 100.00;
    private final List<Transaction> history = new ArrayList<>();

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized double deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
        return balance;
    }

    public synchronized double withdraw(double amount) {
        if (amount > balance) {
            history.add(new Transaction("FAILED_WITHDRAW", amount, balance));
            throw new RuntimeException("Insufficient funds");
        }
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance));
        return balance;
    }

    public List<Transaction> getHistory() {
        return history;
    }
}
