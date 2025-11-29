package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
public class ATMController {

    private final ATMService atmService;
    private static final Logger logger = LoggerFactory.getLogger(ATMController.class);

    public ATMController(ATMService atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/balance")
    public double getBalance() {
        logger.info("Balance checked");
        return atmService.getBalance();
    }

    @PostMapping("/deposit")
    public double deposit(@RequestParam double amount) {
        logger.info("Deposit request: {}", amount);
        return atmService.deposit(amount);
    }

    @PostMapping("/withdraw")
    public double withdraw(@RequestParam double amount) {
        logger.info("Withdraw request: {}", amount);
        return atmService.withdraw(amount);
    }

    @GetMapping("/history")
    public Object getHistory() {
        logger.info("Transaction history requested");
        return atmService.getHistory();
    }
}
