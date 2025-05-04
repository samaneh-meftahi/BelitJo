BelitJo - Movie Ticket Booking Console Application
Overview
BelitJo is a simple console-based movie ticket booking application developed with Spring Boot.
This application allows users to register, login, browse available movies, and purchase tickets.
It also supports an admin exit option and displays all purchased tickets at the end.

Features
User registration and login by email

Display list of movies with available tickets and prices

Filter out movies with no tickets left

Purchase tickets with quantity validation and stock checking

Show total price for each purchase

Admin can exit the application gracefully

Display all purchased tickets when the admin ends the program

Technologies Used
Java 17+

Spring Boot

Spring Data JPA

H2 in-memory database (default, can be changed)

Java Collections and Streams API

Getting started
Prerequisites
Java Development Kit (JDK) 17 or higher

Maven build tool

IDE (IntelliJ IDEA) or command line

How to Run
Clone the repository or download the source code.

Build the project:

bash
mvn clean install
Run the Spring Boot application:

bash
mvn spring-boot:run
The console application will start. Follow the on-screen instructions to:

Register a new user or login with an existing email

Browse movies with available tickets

Purchase tickets by entering movie ID and quantity

Repeat purchase or exit

To exit the application, choose the admin exit option from the main menu.

Usage Example
text
Please choose:
1. Register
2. Login
3. End (admin only)
   Your choice: 1
   Enter your name: Samaneh
   Enter your email: samaneh@gmail.com
   Registration successful. Welcome Samaneh!

Available movies:
1. Titanic - Tickets available: 15 - Price: 50000 Toman
2. The Dark Knight - Tickets available: 10 - Price: 60000 Toman
   ...

Enter the ID of the movie you want to buy tickets for: 1
Enter the number of tickets you want to buy: 2
Purchase successful! Total price: 100,000 Tomans

Do you want to buy tickets for another movie? (yes/no): no
Thank you for your purchase, Samaneh!

Please choose:
1. Register
2. Login
3. End (admin only)
   Your choice: 3
   Admin ended the program.

All purchased tickets:
Ticket ID: 1, Customer: Samaneh, Movie: Titanic, Quantity: 2
Notes
The application uses an in-memory H2 database by default, so data resets on each run.

You can configure a persistent database in application.properties if needed.

Error handling is implemented for invalid inputs and insufficient ticket stock.

The code uses Java Streams and Collections for filtering and displaying data.

Future Improvements
Add password authentication for users

Implement a web or GUI interface

Add ticket cancellation and refund features

Support multiple admins with role-based access

Persist data in a production-grade database

License
This project is open-source and free to use.

If you have any questions or want to contribute, feel free to open an issue or pull request.

Enjoy booking your tickets with BelitJo! 🎬🍿
