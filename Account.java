package banking;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private final AlgGenerator algGenerator = new AlgGenerator();
    private String cardNumber;
    private String pin;
    private BigDecimal balance = new BigDecimal("0");

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public Account(String cardNumber, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public Account(String cardNumber, String pin, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public Account() {
        setCardNumber();
        setPinNumber();
    }

    private void setCardNumber() {
        String firstCardNumberDigits = "400000";
        long min = 0L;
        long max = 999999999L;
        long random = (long) (Math.random() * ((max - min) + 1)) + min;
        String numberToAlgorithm = firstCardNumberDigits + String.format("%09d", random);
        long checkSumDigit = algGenerator.generateCheckSumDigit(numberToAlgorithm);
        this.cardNumber = numberToAlgorithm + checkSumDigit;
    }

    private void setPinNumber() {
        long min = 0L;
        long max = 9999L;
        long random = (long) (Math.random() * ((max - min) + 1)) + min;
        this.pin = String.format("%04d", random);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(cardNumber, account.cardNumber) &&
                Objects.equals(pin, account.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, pin);
    }
}
