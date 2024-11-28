# School Event Management System

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)

## Introduction
The School Event Management System is designed to streamline the management of school events, enabling easy registration, login, and user management functionalities. This project aims to provide a user-friendly interface for both students and administrators.

## Features
- **User Authentication:** Secure login and registration for users and admins.
- **User Management:** Admins can create, read, update, and delete user accounts.
- **Event Management:** Users can register for events and view event details.
- **Responsive Design:** Works seamlessly on both desktop and mobile devices.

## Technologies Used
- **Frontend:** React
- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Styling:** CSS

## Installation
To set up the project locally, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/school-event-management-system.git
Navigate to the project directory:
cd school-event-management-system
Install backend dependencies:
cd backend
mvn install
Set up the database:
src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
Run the backend:
mvn spring-boot:run
Run the frontend:
cd frontend
npm install
npm start

## Usage
Once the application is up and running, follow these instructions to use the School Event Management System.

1. **Access the Frontend:**
   - Open your web browser and navigate to `http://localhost:3000`.

2. **User Registration:**
   - To register a new user, click on the "Register" link in the navigation.
   - Fill in the registration form with the required details, including:
     - First Name
     - Last Name
     - Email Address
     - Role (e.g., student or admin)
     - Password
   - Click the "Submit" button to create your account.

3. **User Login:**
   - To log in, click on the "Login" link.
   - Enter your registered email and password.
   - Choose the appropriate login button (User or Admin) based on your role.
   - After logging in, you'll be redirected to your respective dashboard:
     - Admins will see the Admin Dashboard with management options.
     - Regular users will see the User Homepage.

4. **Managing Users (Admin only):**
   - If you are logged in as an admin, you can manage user accounts.
   - To add a new user, fill in the "Add User" form with the required information.
   - To edit an existing user, click the "Edit" button next to their name, make the necessary changes, and submit the form.
   - To delete a user, click the "Delete" button next to the user you want to remove.

5. **Viewing Events:**
   - Both users and admins can view events in the system.
   - Navigate to the "Events" section to see the list of upcoming events.





