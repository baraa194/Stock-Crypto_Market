# 📈 Stock-Crypto Market

A backend trading platform built with **Spring Boot** that simulates a real-time **stock and cryptocurrency market**.
Users can securely authenticate, trade assets, manage their portfolio, and receive notifications after each transaction.

---

## 🔐 Authentication & Security

* JWT-based authentication
* Role-based authorization (Admin / User)
* Stateless security architecture
* Secure REST APIs

---

## 💼 Core Features

### 👤 Portfolio Management

* Each user has a dedicated portfolio
* Users can buy and sell assets
* Portfolio automatically updates owned quantities
* **PnL (Profit & Loss) calculated after every trade**
* **Total PnL tracked across all trades**

---

### 💳 Wallet System

* Every user has a virtual wallet
* Balance updates automatically after buy/sell operations
* Updated balance included in trade notifications

---

### 📊 Asset Management

Supports multiple asset types:

* 📈 Stocks
* 💰 Cryptocurrencies

Admin capabilities:

* Add new assets
* Define asset type
* Set initial price
* Manage available assets

---

### ⚡ Market Simulation

* Dynamic price updates using scheduled tasks
* Simulates real-world market fluctuations
* Ensures transactional consistency during trading

---

### 🔔 Notification System

After every successful trade, users receive a notification containing:

* Trade confirmation
* Asset name
* Executed price
* Updated wallet balance

---

## ⚡ Performance Optimization

* **Redis Caching** used to improve performance and reduce database load.

---

## 🏗️ Architecture

* Event-driven architecture for trade execution
* Scheduled jobs for price updates
* Layered architecture (Controller → Service → Repository)

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT
* JPA / Hibernate
* Redis
* REST APIs
* Scheduled Tasks
* Event Listeners

---

## 🚀 Future Improvements

* WebSocket for real-time price streaming
* Admin dashboard
* Advanced trade analytics

---

⭐ Feel free to explore, contribute, or suggest improvements!
