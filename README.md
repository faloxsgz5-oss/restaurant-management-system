# Restaurant Management System

Java Swing desktop application for managing a restaurant: table orders, kitchen queue, payments and daily sales reports, with separate screens for managers, employees (waiters) and chefs.

> University project, course 1101093 OOP & Data Structures, Suranaree University of Technology (team of 4).

## Features

- **Login by role**: manager, employee and chef each get their own screen.
- **Employee**: take orders by table, pick menu items, edit or delete orders, and handle payment by table.
- **Chef**: view incoming orders, work through the queue and mark orders as finished.
- **Manager**: manage employee accounts (add, search, delete) and view daily sales reports with charts (JFreeChart).
- **MySQL database** for users, menu, tables, orders, payments and sales data.

## Tech stack

| Area | Technology |
|---|---|
| Language | Java (Swing GUI) |
| Database | MySQL / MariaDB (JDBC) |
| Charts | JFreeChart 1.0.19, JCommon 1.0.23 |
| DB driver | MySQL Connector/J 9.3.0 |

## Project structure

```
src/Restaurant/   Java source code (entry point: Login.java)
src/images/       Image used by the GUI
db/               restaurant_db.sql (schema + sample data)
docs/             Project report (PDF)
lib/              Put the required .jar files here (see below)
```

## Getting started

1. **Create the database**: import `db/restaurant_db.sql` into MySQL or MariaDB (for example with phpMyAdmin or XAMPP). It creates the `restaurant_db` database with sample data.
2. **Add libraries**: download these jars and place them in `lib/`, then add them to the project classpath.
   - `mysql-connector-j-9.3.0.jar`
   - `jfreechart-1.0.19.jar`
   - `jcommon-1.0.23.jar`
3. **Check the connection**: the app connects to `jdbc:mysql://localhost:3306/restaurant_db` with user `root` and an empty password. Change this in the code if your MySQL setup is different.
4. **Run** `Restaurant.Login` (in Eclipse or another IDE with `src/` as the source folder and `src/images` on the classpath).

## Sample accounts

The SQL file includes demo accounts for testing only. See the `users` table in `db/restaurant_db.sql` for the usernames, passwords and roles.

## My contribution

> TODO: describe the parts you built yourself, for example which screens or features, in 2–3 bullet points.

## Known limitations / possible improvements

- Passwords are stored as plain text in the demo database; a real system should store salted hashes.
- Database credentials are hard-coded in several classes; they should be read from one configuration file (`DatabaseManager` already centralizes part of this).

## Screenshots

> TODO: add 2–3 screenshots (login, order screen, daily report) in `docs/` and link them here.
