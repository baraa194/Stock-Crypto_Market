# Stock-Crypto_Market
📈 Stock_Crypto_Market

A backend trading platform built with Spring Boot that simulates a real-time stock and cryptocurrency market.
Users can register, trade assets, manage their portfolio, and receive instant notifications after each transaction.

🔐 Authentication & Security

User Registration & Login system

Secured using JWT (JSON Web Token)

Role-based authorization

Stateless authentication for better scalability and security

💼 Core Features
👤 User Portfolio

Each user has a dedicated portfolio

Users can:

Buy assets

Sell assets

Track owned quantities

Every transaction updates the user’s balance automatically

📊 Assets Management

Support for:

📈 Stocks

💰 Cryptocurrencies

Admin can:

Add new assets

Set initial prices

Define asset type

Asset prices are dynamic and continuously changing

⚡ Real-Time Market Simulation

Prices update dynamically to simulate real market behavior

Ensures real-time data handling

Maintains transactional consistency during buy/sell operations

🔔 Notification System

After every successful trade:

The user receives a notification

Notification includes:

Trade confirmation

Asset name

Executed price

Updated balance

This ensures transparency and instant feedback for every operation.

💳 Wallet System

Each user has a virtual wallet

Balance updates automatically after:

Buying assets

Selling assets

Users are notified with their updated balance after each trade

🛠️ Tech Stack

Java

Spring Boot

Spring Security

JWT Authentication

JPA / Hibernate

REST APIs

Scheduled Tasks (for price updates)

Event-Driven Architecture (for trade notifications)

🚀 Project Goal

To simulate a simplified financial trading platform that demonstrates:

Secure authentication

Real-time data handling

Transaction management

Event-driven notifications

Portfolio & wallet management
