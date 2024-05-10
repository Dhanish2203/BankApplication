package BankProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApplication {
	private Connection con;
    private Map<String, Customer> customers;

	 public BankApplication() {
	        this.customers = new HashMap<>();
	        initializeDatabase();
	        loadCustomersFromDatabase();
	    }

	 private void initializeDatabase() {
		    try {
		        // Connect to the Oracle database using the custom DBConnection class
		        con = DBConnection.getConnection();

		        // Create a table if not exists
		        Statement statement = con.createStatement();
		        String createTableQuery = "CREATE TABLE customers (custName VARCHAR2(255) PRIMARY KEY, password VARCHAR2(255))";

		        statement.execute(createTableQuery);
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

    private void loadCustomersFromDatabase() {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");

            while (resultSet.next()) {
                String custName = resultSet.getString("custName");
                String password = resultSet.getString("password");
                customers.put(custName, new Customer(custName, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void saveCustomerToDatabase(Customer customer) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO customers VALUES (?, ?)");
            preparedStatement.setString(1, customer.getCustName());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showMenu() {
        System.out.println("\nCustomer\n");
        System.out.println("1. Customer Login");
        System.out.println("2. New Customer Sign in");
        System.out.println("3. Exit");
    }

    public void loginMenu(String custName) {
        System.out.println("\nWelcome " + custName + "!\n");
        System.out.println("Account Details\n");
        System.out.println("a. Amount Deposit");
        System.out.println("b. Amount Withdrawal");
        System.out.println("c. Check Balance");
        System.out.println("d. Exit");
    }

    public double depositAmount(double balance) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                if (amount > 0) {
                    balance += amount;
                    System.out.println("Current balance: " + balance);
                    break;
                } else {
                    System.out.println("Amount should be greater than 0.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.nextLine(); // consume the invalid input
            }
        }
        return balance;
    }

    // Similar methods for withdrawAmount and checkBalance

    public double withdrawAmount(double balance) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                if (0 < amount && amount <= balance) {
                    balance -= amount;
                    System.out.println("Current balance: " + balance);
                    break;
                } else {
                    System.out.println("Invalid amount or insufficient balance.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.nextLine(); // consume the invalid input
            }
        }
        return balance;
    }

    public void checkBalance(double balance) {
        System.out.println("Current balance: " + balance);
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Customer Name: ");
        String custName = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();

        if (customers.containsKey(custName) && customers.get(custName).getPassword().equals(password)) {
            loginMenu(custName);
            double balance = 0.0;

            while (true) {
                System.out.print("Enter your choice: ");
                String subChoice = scanner.next();

                if ("a".equals(subChoice)) {
                    balance = depositAmount(balance);
                } else if ("b".equals(subChoice)) {
                    balance = withdrawAmount(balance);
                } else if ("c".equals(subChoice)) {
                    checkBalance(balance);
                } else if ("d".equals(subChoice)) {
                    System.out.println("Exited Account Details.");
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } else {
            System.out.println("Invalid Customer Name or Password.");
        }
    }

    public void newCustomerSignIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter New Customer Name: ");
        String newCustName = scanner.next();
        System.out.print("Enter New Password: ");
        String newPassword = scanner.next();

        if (!newCustName.isEmpty() && !newPassword.isEmpty()) {
            customers.put(newCustName, new Customer(newCustName, newPassword));
            System.out.println("New customer added successfully.");
        } else {
            System.out.println("Invalid customer details. Please try again.");
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.next();

            if ("1".equals(choice)) {
                login();
            } else if ("2".equals(choice)) {
                newCustomerSignIn();
            } else if ("3".equals(choice)) {
                System.out.println("Exited Application successfully.");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        scanner.close();
    }
}

