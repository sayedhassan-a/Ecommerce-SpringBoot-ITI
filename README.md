# E-Commerce Application

This is a Spring Boot-based e-commerce web application that allows users to browse products, manage their shopping cart, and complete purchases using different payment methods. The system includes an admin panel for managing inventory, pricing, and customer profiles.

## Contributors
- Mahmoud Essam Mensheteh
- Abdulmajeed Mohamed Abdulmajeed
- Antony Sameh Boutros
- Sayed Hassan Abdelrazek

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Testing](#testing)
- [License](#license)

## Introduction
This e-commerce platform offers both user and admin functionalities. Users can browse product categories, add items to a shopping cart, and proceed to checkout with various payment options. Admins can manage products, orders, and customer data from a dedicated panel.

## Features
1. **Home Page**  
   - View products, daily deals, and discounts for guests and registered users.

2. **User Registration & Authentication**  
   - Email and password validation.
   - Username availability check during registration.
   - Social login (Google).

3. **User Profile Management**  
   - Manage addresses, payment methods, and view order history.

4. **Product Catalog**  
   - Browse products by category, price range, or type.

5. **Shopping Cart**  
   - Add/remove items, update quantities, and persist cart data.

6. **Order Management**  
   - Track order status and provide real-time updates.

7. **Payment Integration**  
   - Credit card payments and Cash on Delivery (COD) options.

8. **Admin Panel**  
   - Manage product listings, inventory, and view customer profiles.

9. **Asynchronous Features**  
   - Real-time updates for cart items, and username checks during registration.

## System Architecture
- **Client**: Web-based front-end using HTML5, CSS3, and JavaScript, communicating with the backend via RESTful APIs.
- **Server**: Spring Boot-based backend to handle business logic.
- **Database**: Relational database (MySQL, MongoDB) for user and product data.
- **Payment Gateway**: Integration with online and offline payment services.
- **Asynchronous Features**: AJAX for dynamic user experience.

## Technologies Used
- **Backend**: Java, Spring Boot, Spring Data JPA, Spring Security, Spring AOP
- **Database**: MySQL/MongoDB
- **Frontend**: HTML5, CSS3, JavaScript, AJAX
- **Payment Integration**: Credit Cards, Cash on Delivery (COD)
- **Testing**: JUnit, Mockito
- **Version Control**: GitHub

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository.git
   cd e-commerce-app
   
2. Ensure you have Docker installed and running.

3. Build and run the application using Docker:
   - Build the Docker image:
     ```bash
     docker build -t e-commerce-app .
     ```

   - Start the services with Docker Compose:
     ```bash
     docker-compose up -d
     ```

4. Access the application:
   - The backend will be available at `http://localhost:8080`.
   - If you have a frontend, it will be accessible at `http://localhost:3000` or the specified port.

5. To stop the application:
   ```bash
   docker-compose down
