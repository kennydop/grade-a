# Running the GradeA Quiz Project Locally

This project is a simple Quiz application developed using JavaFX for the GUI, it provides an interface to take multiple types of quizzes.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed the latest version of [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/).
- You have installed [JavaFX SDK](https://openjfx.io/openjfx-docs/#install-javafx).
- You have installed a Java IDE. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) is recommended as it provides better support for JavaFX.

## Setting Up The Project

1. Clone the project repository to your local machine using `git clone https://github.com/kennydop/grade-a`.

2. Open the project in your Java IDE.

3. Link the JavaFX SDK library to the project.

   - If you're using IntelliJ IDEA, follow these steps:
     - Go to `File -> Project Structure`.
     - In the `Libraries` tab, click on the `+` and add the path to the `lib` folder inside your JavaFX SDK installation directory.
     - Click `Apply` and then `OK`.

4. Set the VM options for JavaFX.
   - In IntelliJ IDEA, follow these steps:
     - Go to `Run -> Edit Configurations`.
     - In the `VM options` field, paste the following: `--module-path "<path-to-javafx-sdk>/lib" --add-modules javafx.controls,javafx.fxml`, replacing `<path-to-javafx-sdk>` with the path to your JavaFX SDK installation directory.

## Running The Project

1. In the project directory, locate the `main` method in the `App` class, which is the entry point of the application.

2. Run the `main` method. If the setup was successful, the application GUI should launch and you'll be able to interact with the Quiz application.
