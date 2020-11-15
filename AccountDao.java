package banking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class AccountDao {

    private AccountDao() {
        throw new IllegalStateException("Account class");
    }

    public static void insert(Account account) {
        String query = " insert into card (number, pin, balance)"
                + " values (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {

            preparedStatement.setString(1, account.getCardNumber());
            preparedStatement.setString(2, account.getPin());
            preparedStatement.setBigDecimal(3, account.getBalance());

            preparedStatement.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Optional<Account> find(String numberCard, String pin) {
        Account account = null;
        String query = " select number, pin, balance from card where number = ? and pin = ?";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            preparedStatement.setString(1, numberCard);
            preparedStatement.setString(2, pin);
            ResultSet rs = preparedStatement.executeQuery();

            account = new Account(rs.getString("number"), rs.getString("pin"), rs.getBigDecimal("balance"));

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(account);
    }

    public static Optional<Account> find(String numberCard) {
        Account account = null;
        String query = " select number, balance from card where number = ?";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            preparedStatement.setString(1, numberCard);
            ResultSet rs = preparedStatement.executeQuery();

            account = new Account(rs.getString("number"), rs.getBigDecimal("balance"));

            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Optional.ofNullable(account);
    }

    public static void addIncome (Account account, BigDecimal income) throws SQLException {
        String query = " update card set balance = ? where number = ? and pin = ?";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            BigDecimal updatedBalance = income.add(account.getBalance());
            preparedStatement.setBigDecimal(1, updatedBalance);
            preparedStatement.setString(2, account.getCardNumber());
            preparedStatement.setString(3, account.getPin());
            preparedStatement.executeUpdate();
        }
    }

    public static boolean isExists(Account account) throws SQLException {
        String query = " select * from card where number = ? and pin = ?";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            preparedStatement.setString(1, account.getCardNumber());
            preparedStatement.setString(2, account.getPin());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                rs.close();
                return true;
            }
        }
        return false;
    }

    public static void doTransfer(Account accountFrom, Account accountTo, BigDecimal amount) throws SQLException {
        if (accountFrom.getBalance().compareTo(amount) < 0) {
            System.out.println("Not enough money!");
        } else if (accountFrom.equals(accountTo)) {
            System.out.println("You can't transfer money to the same account!");
        } else {
            String querySubtract = " update card set balance = ? where number = ?";
            try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
                 PreparedStatement preparedStatement = conn.prepareStatement(querySubtract);) {
                BigDecimal balanceSubtract = accountFrom.getBalance().subtract(amount);
                preparedStatement.setBigDecimal(1, balanceSubtract);
                preparedStatement.setString(2, accountFrom.getCardNumber());
                preparedStatement.executeUpdate();
            }

            String queryAdd = " update card set balance = ? where number = ?";
            try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
                 PreparedStatement preparedStatement = conn.prepareStatement(queryAdd);) {
                BigDecimal balanceAdd = accountTo.getBalance().add(amount);
                preparedStatement.setBigDecimal(1, balanceAdd);
                preparedStatement.setString(2, accountTo.getCardNumber());
                preparedStatement.executeUpdate();
            }

            System.out.println("Success!");
        }
    }

    public static void delete(Account account) throws SQLException {
        String query = " delete from card where number = ?";
        try (Connection conn = DriverManager.getConnection(DataBase.getFullPath());
             PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            preparedStatement.setString(1, account.getCardNumber());
            preparedStatement.executeUpdate();
        }

        System.out.println("The account has been closed!");
    }
}
