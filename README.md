# Running the GradeA Quiz Project Locally

This project is a simple Quiz application developed using JavaFX for the GUI, it provides an interface to take multiple types of quizzes.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed the latest version of [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/).
- You have installed [JavaFX SDK](https://openjfx.io/openjfx-docs/#install-javafx) 13 or higher.
- Maven installed and set up on your PATH.
- MySQL server set up and running. (Optional)

## Setting Up The Project

If you haven't already, start by cloning the repository to your local machine.

```bash
git clone https://github.com/kennydop/grade-a
cd grade-a
```

## Database Configuration

1. Open the `DB.java` file located in `com.gradea.controllers`.

2. Update the MySQL server details according to your local setup:

   - For a **Local MySQL Server**:

     ```java
     protected static final String host = "localhost"; // Or your MySQL host
     protected static final int port = 3306; // Or your MySQL port
     protected static final String user = "root"; // Or your MySQL user
     protected static final String password = "root"; // Or your MySQL password
     protected static final String dbName = "gradea"; // Or your database name
     ```

   - For a **Remote MySQL Server**:
     ```java
     protected static final String host = "sql8.freemysqlhosting.net"; // Or your remote MySQL host
     protected static final int port = 3306; // Or your remote MySQL port
     protected static final String user = "sql8638380"; // Or your remote MySQL user
     protected static final String password = "mabJYZZyXd"; // Or your remote MySQL password
     protected static final String dbName = "sql8638380"; // Or your remote database name
     ```

3. If you want the application to connect to the MySQL server with a password, set the `connectWithPassword` variable to `true`. If not, set it to `false`:

   ```java
   private static boolean connectWithPassword = true; // true for password, false otherwise.
   ```

## Build and Run

Navigate to the root directory of the project:

```bash
mvn clean install
mvn javafx:run
```

This will compile and start the JavaFX application.

## Troubleshooting

- If you encounter any issues related to database connection, double-check the database configuration in the `DB.java` file to ensure all settings are correct.
- Ensure your MySQL server is running and that the user has the necessary permissions to create and manage the database and its tables.

---

That's it! Your Grade A JavaFX application should be up and running locally. If you encounter issues or need further assistance, feel free to ask.

---
