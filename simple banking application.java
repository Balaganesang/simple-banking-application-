import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Account Class
class Account {
    private String accountHolderName;
    private String accountNumber;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String accountHolderName, String accountNumber) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: $" + amount);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
        }
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
}

// BankingAppGUI Class
class BankingAppGUI extends JFrame {
    private Account account;
    private JLabel balanceLabel;
    private JTextArea transactionHistoryArea;
    private JTextField amountField;

    public BankingAppGUI(Account account) {
        this.account = account;

        // Set up JFrame
        setTitle("Simple Banking Application");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Balance Panel
        JPanel balancePanel = new JPanel(new FlowLayout());
        balanceLabel = new JLabel("Current Balance: $0.0");
        balancePanel.add(balanceLabel);
        add(balancePanel, BorderLayout.NORTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton depositButton = new JButton("Deposit Funds");
        JButton withdrawButton = new JButton("Withdraw Funds");
        JButton viewHistoryButton = new JButton("View Transaction History");
        amountField = new JTextField(15);

        centerPanel.add(new JLabel("Enter Amount:"));
        centerPanel.add(amountField);
        centerPanel.add(depositButton);
        centerPanel.add(withdrawButton);
        centerPanel.add(viewHistoryButton);

        add(centerPanel, BorderLayout.CENTER);

        // Transaction History Panel
        transactionHistoryArea = new JTextArea(10, 30);
        transactionHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionHistoryArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Event Listeners
        depositButton.addActionListener(e -> handleDeposit());
        withdrawButton.addActionListener(e -> handleWithdraw());
        viewHistoryButton.addActionListener(e -> handleViewHistory());
    }

    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount > 0) {
                account.deposit(amount);
                updateBalance();
                JOptionPane.showMessageDialog(this, "Deposited $" + amount + " successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid deposit amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        }
        amountField.setText("");
    }

    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount > 0 && account.getBalance() >= amount) {
                account.withdraw(amount);
                updateBalance();
                JOptionPane.showMessageDialog(this, "Withdrew $" + amount + " successfully.");
            } else if (amount > account.getBalance()) {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid withdrawal amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        }
        amountField.setText("");
    }

    private void handleViewHistory() {
        ArrayList<String> history = account.getTransactionHistory();
        transactionHistoryArea.setText("");
        for (String transaction : history) {
            transactionHistoryArea.append(transaction + "\n");
        }
    }

    private void updateBalance() {
        balanceLabel.setText("Current Balance: $" + account.getBalance());
    }

    public static void main(String[] args) {
        // Prompt the user for account details
        String accountHolderName = JOptionPane.showInputDialog("Enter account holder name:");
        String accountNumber;

        // Validate account number
        while (true) {
            accountNumber = JOptionPane.showInputDialog("Enter a 16-digit account number:");
            if (accountNumber != null && accountNumber.matches("\\d{16}")) {
                break; // Valid account number
            } else {
                JOptionPane.showMessageDialog(null, "Invalid account number! Please enter exactly 16 digits.");
            }
        }

        // Create an account with valid details
        Account account = new Account(accountHolderName, accountNumber);

        // Launch the GUI
        BankingAppGUI app = new BankingAppGUI(account);
        app.setVisible(true);
    }
}