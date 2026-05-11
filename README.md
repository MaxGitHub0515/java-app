# ShopAdmin — E-Commerce Admin Panel

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7-success?style=for-the-badge&logo=springboot)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-brown?style=for-the-badge&logo=hibernate)
![MySQL](https://img.shields.io/badge/MySQL-8-blue?style=for-the-badge&logo=mysql)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-green?style=for-the-badge&logo=thymeleaf)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apachemaven)

</p>

<p align="center">
  Full-stack Java Spring Boot administration panel for e-commerce management.
</p>

---

# Overview

ShopAdmin is a modern web-based administration panel for managing an e-commerce store.

The application allows administrators to:
- Manage customers
- Control inventory
- Process orders
- Monitor analytics in real time
- Secure the platform using Spring Security authentication

Built with modern Java backend technologies and MVC architecture.

---

# Preview

<div align="center">

## Login Page

<img src=".github/assets/login-page.png" width="900"/>

Secure administrator authentication with Spring Security and BCrypt encryption.

---

## Analytics Dashboard

<img src=".github/assets/analytics-dashboard.png" width="900"/>

Real-time KPI tracking:
- Revenue
- Orders
- Inventory value
- Top customers
- Best-selling products

---

## Customers Management

<img src=".github/assets/customers-management.png" width="900"/>

CRUD operations with customer search and profile management.

---

## Products Management

<img src=".github/assets/products-management.png" width="900"/>

Inventory management with:
- Restocking
- Stock monitoring
- Low-stock alerts

---

## Orders Management

<img src=".github/assets/orders-management.png" width="900"/>

Order lifecycle management with:
- Status tracking
- Cancellation
- Automatic inventory restoration

</div>

---

# Features

- Customer management system
- Product inventory management
- Order processing workflow
- Analytics dashboard
- Secure authentication system
- BCrypt password encryption
- JPQL analytical queries
- MVC architecture with DAO layer
- Real-time inventory synchronization

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend language |
| Spring Boot | Web framework |
| Spring Security | Authentication & authorization |
| Hibernate / JPA | ORM & database mapping |
| MySQL | Relational database |
| Thymeleaf | Server-side rendering |
| Maven | Dependency management |

---

# Architecture

The application follows the MVC (Model-View-Controller) pattern with a dedicated DAO layer.

```text
src/main/java/org/example/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AnalyticsController.java
│   ├── AuthController.java
│   ├── CustomerController.java
│   ├── ProductController.java
│   └── OrderController.java
├── dao/
│   ├── AnalyticsDAO.java
│   ├── CustomerDAO.java
│   ├── ProductDAO.java
│   └── OrderDAO.java
└── model/
    ├── Customer.java
    ├── Product.java
    ├── Order.java
    └── OrderDetail.java
```

---

# Installation

## Clone Repository

```bash
git clone https://github.com/your-username/shopadmin.git
cd shopadmin
```

---

## Configure Database

Import the database:

```bash
mysql -u root -p < E-Commerce.sql
```

Update `application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=none
```

---

## Run Application

```bash
mvn spring-boot:run
```

Open in browser:

```text
http://localhost:8080
```

---

# Default Credentials

```text
Username: admin
Password: admin123
```

---

# Security

The application uses Spring Security to protect all endpoints.

Features include:
- Authentication system
- BCrypt password hashing
- Protected admin routes
- Secure login/logout flow

---

# Documentation

📄 [Project Documentation](dokumentacja.pdf)

---

# Future Improvements

- REST API support
- Docker deployment
- Role-based authorization
- Product image uploads
- Email notifications
- Unit & integration testing
- CI/CD pipeline

---

# Author

## MaxGithub0515

Computer Science Student • Java Backend Developer

---

# License

This project is for educational purposes.
