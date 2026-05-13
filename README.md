# Employee Attendance System with QR Code

A Java Spring Boot web application to track employee attendance using QR codes.

## Features
- Register employees
- Generate QR code for each employee
- Mark attendance by scanning or entering QR content
- View attendance history
- In-memory H2 database for easy development

## Run the app
1. Open a terminal in the project folder.
2. Build the app:
   ```powershell
   mvn clean package
   ```
3. Run the app:
   ```powershell
   mvn spring-boot:run
   ```
4. Open `http://localhost:8080`

## Notes
- The H2 console is available at `http://localhost:8080/h2-console`.
- QR codes encode the string format `ATTENDANCE:<employeeCode>`.
