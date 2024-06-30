# SITA_TEST_TASK

## Description
This project processes files by summing the numbers in each file and creating an output file with the result. Processed files are moved to appropriate directories based on the outcome.

## How to Build

1. Ensure you have Maven and Java installed.
2. Navigate to the project directory.
3. Run the following command to build the project:
    ```shell
    mvn clean install
    ```

## How to Run

1. Deploy the generated WAR file to a Glassfish/Tomcat server.
2. Ensure the directories specified in the `application.properties` file exist:
    - `C:/SITA_TEST_TASK/IN`
    - `C:/SITA_TEST_TASK/OUT`
    - `C:/SITA_TEST_TASK/PROCESSED`
    - `C:/SITA_TEST_TASK/ERROR`
3. Place input files in the `IN` directory. The application will process them automatically.

## Configuration

Configuration is done through the `application.properties` file.

```properties
input.directory=C:/SITA_TEST_TASK/IN
output.directory=C:/SITA_TEST_TASK/OUT
processed.directory=C:/SITA_TEST_TASK/PROCESSED
error.directory=C:/SITA_TEST_TASK/ERROR
polling.interval=5000
```

## Unit Tests
Run the tests using:

```sh
mvn test
```