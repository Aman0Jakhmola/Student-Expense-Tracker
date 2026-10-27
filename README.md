# Student Expense Tracker (Java)

A beginner-friendly command-line project for tracking daily expenses. It uses core Java, `ArrayList`, `LocalDate`, exception handling, and file I/O.

## Features
- Automatically adds five sample expenses on the first run
- Add an expense with title, category, amount, and date
- View all recorded expenses
- Calculate total spending
- View spending grouped by category
- Save data locally in `expenses.csv` between runs

## Requirements
- Java 17 or later

## Run the project

Open a terminal in this folder and run:

```bash
javac src/Main.java
java -cp src Main
```

The first run adds sample expenses so you can explore the options immediately. Later runs load the saved data. Choose **5. Exit** to save and close the program.

## Sample data
- Lunch — Food — ₹80
- Bus fare — Travel — ₹40
- Notebook — Study — ₹65
- Coffee — Food — ₹50
- Project printout — Study — ₹120

The sample records are illustrative, not real spending records. Delete `expenses.csv` if you want to reset the local data and regenerate the samples.

## Concepts demonstrated
- Classes and objects
- Lists and loops
- Methods and switch statements
- Input validation and exception handling
- File reading/writing
- Date handling with `LocalDate`

## Possible future improvements
- Add monthly budgets and budget warnings
- Add edit/delete expense options
- Replace file storage with MySQL
- Build a web interface using Spring Boot

## Author
Add your name and GitHub profile link here before publishing.
