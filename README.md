# 📈 Stock-Crypto Market

A backend trading platform built with **Spring Boot** that simulates a real-time stock and cryptocurrency market.

The system allows users to register, authenticate securely, trade assets, manage their portfolio, and receive real-time notifications after each transaction.

---

## 🔐 Authentication & Security

- User Registration & Login
- JWT (JSON Web Token) based authentication
- Role-Based Authorization
- Stateless Security Architecture
- Secure REST APIs

---

## 💼 Core Features

### 👤 User Portfolio
- Each user has a dedicated portfolio
- Users can:
  - Buy assets
  - Sell assets
  - Track owned quantities
- Automatic balance updates after every trade

---

### 💳 Wallet System
- Every user has a virtual wallet
- Balance updates automatically after:
  - Buying assets
  - Selling assets
- Updated balance is included in trade notifications

---

### 📊 Asset Management
The platform supports multiple asset types:

- 📈 Stocks  
- 💰 Cryptocurrencies  

Admin capabilities:
- Add new assets
- Define asset type
- Set initial price
- Manage available assets in the system

---

### ⚡ Real-Time Market Simulation
- Asset prices update dynamically
- Simulates real-world market fluctuations
- Ensures transactional consistency during buy/sell operations
- Real-time data handling for accurate trading simulation

---

### 🔔 Notification System

After every successful trade:

- A notification is sent to the user
- Includes:
  - Trade confirmation
  - Asset name
  - Executed price
  - Updated wallet balance

This guarantees transparency and instant feedback for every transaction.

---

## 🏗️ System Architecture

- Event-Driven Architecture for trade execution
- Scheduled jobs for dynamic price updates
- RESTful APIs
- Layered architecture (Controller - Service - Repository)

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- JPA / Hibernate
- REST APIs
- Scheduled Tasks
- Event Listeners

---

## 🎯 Project Objective

This project demonstrates:

- Secure authentication with JWT
- Real-time data processing
- Transaction management
- Portfolio & wallet handling
- Event-driven notification system

---

## 🚀 Future Improvements

- WebSocket for real-time price streaming
- Admin dashboard
- Advanced trade analytics
- Performance optimization for high trading volume

---

⭐ Feel free to explore, contribute, or suggest improvements!
