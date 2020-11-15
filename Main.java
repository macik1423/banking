package banking;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final AlgGenerator algGenerator = new AlgGenerator();

    public static void main(String[] args) throws Exception {
        String fileName = args[1];
        DataBase dataBase = new DataBase(fileName);

        Scanner sc = new Scanner(System.in);
        String option = "99";
        boolean abort = false;
        while (!"0".equals(option) && !abort) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            option = sc.nextLine();
            switch(option) {
                case "1":
                    Account account = new Account();
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(account.getCardNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(account.getPin());
                    AccountDao.insert(account);
                    break;
                case "2":
                    System.out.println("Enter your card number:");
                    String enteredNumberCard = sc.nextLine();
                    System.out.println("Enter your PIN:");
                    String enteredPin = sc.nextLine();

                    if (AccountDao.isExists(new Account(enteredNumberCard, enteredPin))) {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        String option2 = "99";
                        boolean isLoggedOut = false;
                        while(!"0".equals(option2) && !isLoggedOut) {
                            Account accountLogged = AccountDao.find(enteredNumberCard, enteredPin).get();
                            System.out.println("1. Balance");
                            System.out.println("2. Add income");
                            System.out.println("3. Do transfer");
                            System.out.println("4. Close account");
                            System.out.println("5. Log out");
                            System.out.println("0. Exit");
                            option2 = sc.nextLine();
                            switch (option2) {
                                case "1":
                                    System.out.println("Balance: " + accountLogged.getBalance());
                                    break;
                                case "2":
                                    System.out.println("Enter income:");
                                    BigDecimal income = new BigDecimal(sc.nextLine());
                                    try {
                                        AccountDao.addIncome(accountLogged, income);
                                        System.out.println("Income was added!");
                                    } catch (SQLException throwables) {
                                        System.out.println("Error added income");
                                    }
                                    break;
                                case "3":
                                    System.out.println("Transfer");
                                    System.out.println("Enter card number");
                                    String accountToNumber = sc.nextLine();
                                    if (!algGenerator.checkLuhn(accountToNumber)) {
                                        System.out.println("Probably you made mistake in the card number. Please try again!");
                                    } else {
                                        AccountDao.find(accountToNumber).ifPresentOrElse(a -> {
                                            System.out.println("Enter how much money you want to transfer:");
                                            BigDecimal amountToTransfer = new BigDecimal(sc.nextLine());
                                            try {
                                                AccountDao.doTransfer(accountLogged, a, amountToTransfer);
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        }, () -> {
                                            System.out.println("Such a card does not exist.");
                                        });
                                    }
                                    break;
                                case "4":
                                    AccountDao.delete(accountLogged);
                                    isLoggedOut = true;
                                    break;
                                case "5":
                                    System.out.println("You have successfully logged out!");
                                    isLoggedOut = true;
                                    break;
                                case "0":
                                    System.out.println("Bye!");
                                    abort = true;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                    break;
                case "0":
                    System.out.println("Bye!");
                    break;
            }
        }
    }
}