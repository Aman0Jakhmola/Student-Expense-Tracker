import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "expenses.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        addSampleDataIfNeeded();

        while (true) {
            System.out.println("\n=== STUDENT EXPENSE TRACKER ===");
            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. View total spending");
            System.out.println("4. View spending by category");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addExpense();
                case "2" -> viewExpenses();
                case "3" -> showTotal();
                case "4" -> showCategoryTotals();
                case "5" -> {
                    saveExpenses();
                    System.out.println("Expenses saved. Goodbye!");
                    return;
                }
                default -> System.out.println("Please enter a number from 1 to 5.");
            }
        }
    }

    private static void addSampleDataIfNeeded() {
        if (!expenses.isEmpty()) return;

        LocalDate today = LocalDate.now();
        expenses.add(new Expense("Lunch", "Food", 80.00, today));
        expenses.add(new Expense("Bus fare", "Travel", 40.00, today));
        expenses.add(new Expense("Notebook", "Study", 65.00, today));
        expenses.add(new Expense("Coffee", "Food", 50.00, today));
        expenses.add(new Expense("Project printout", "Study", 120.00, today));

        saveExpenses();
        System.out.println("Sample expenses added so you can try the project immediately.");
    }

    private static void addExpense() {
        try {
            System.out.print("Expense title: ");
            String title = scanner.nextLine().trim();
            if (title.isEmpty() || title.contains("|")) {
                System.out.println("Title cannot be empty or contain '|'.");
                return;
            }

            System.out.print("Category (Food/Travel/Study/Other): ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty() || category.contains("|")) {
                System.out.println("Category cannot be empty or contain '|'.");
                return;
            }

            System.out.print("Amount (e.g. 120.50): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be a positive number.");
                return;
            }

            System.out.print("Date (YYYY-MM-DD, or press Enter for today): ");
            String dateInput = scanner.nextLine().trim();
            LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

            expenses.add(new Expense(title, category, amount, date));
            saveExpenses();
            System.out.println("Expense added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Example: 120.50");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
        }
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.printf("%-4s %-20s %-12s %-12s %-12s%n",
                "No.", "Title", "Category", "Amount", "Date");
        System.out.println("---------------------------------------------------------------");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%-4d %-20s %-12s ₹%-11.2f %-12s%n",
                    i + 1, shorten(e.title, 20), shorten(e.category, 12), e.amount, e.date);
        }
    }

    private static void showTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.printf("Total spending: ₹%.2f%n", total);
    }

    private static void showCategoryTotals() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        List<String> categories = new ArrayList<>();
        List<Double> totals = new ArrayList<>();

        for (Expense e : expenses) {
            int index = categories.indexOf(e.category.toLowerCase());
            if (index >= 0) {
                totals.set(index, totals.get(index) + e.amount);
            } else {
                categories.add(e.category.toLowerCase());
                totals.add(e.amount);
            }
        }

        System.out.println("Spending by category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%-15s ₹%.2f%n", categories.get(i), totals.get(i));
        }
    }

    private static void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 4) {
                    try {
                        expenses.add(new Expense(
                                parts[0], parts[1], Double.parseDouble(parts[2]),
                                LocalDate.parse(parts[3])));
                    } catch (RuntimeException ignored) {
                        System.out.println("Skipped an invalid saved record.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load saved expenses: " + e.getMessage());
        }
    }

    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.title + "|" + e.category + "|" + e.amount + "|" + e.date);
            }
        } catch (IOException e) {
            System.out.println("Could not save expenses: " + e.getMessage());
        }
    }

    private static String shorten(String value, int maxLength) {
        return value.length() <= maxLength ? value : value.substring(0, maxLength - 3) + "...";
    }

    private static class Expense {
        private final String title;
        private final String category;
        private final double amount;
        private final LocalDate date;

        private Expense(String title, String category, double amount, LocalDate date) {
            this.title = title;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }
    }
}
