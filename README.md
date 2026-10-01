# 📈 Stock-Crypto Market Platform

A full-stack simulation trading platform built with **Spring Boot** and **Angular** that simulates a real-time **stock and cryptocurrency market**. 

Users can securely authenticate, execute real-time trades, set conditional orders (Take Profit / Stop Loss), monitor their detailed financial history via a transaction ledger, and analyze portfolio performance through dynamic real-time analytics.

---

## 🛠️ Tech Stack

### Frontend
* **Angular**
* **TypeScript**
* **RxJS**
* **HTML5 & CSS3 / Tailwind CSS**

### Backend
* **Java**
* **Spring Boot**
* **Spring Security & JWT**
* **Spring Data JPA / Hibernate**
* **Redis** (Caching)
* **PostgreSQL / MySQL**
* **Scheduled Tasks & Event Listeners**
* **JUnit 5 & Mockito**

---

## 🚀 Key Features & System Modules

### 🔐 1. Authentication & Security
* **JWT-Based Authentication**: Secure login and session management using tokens.
* **Role-Based Access Control**: Differentiated permissions for `ADMIN` and `USER` roles.
* **Stateless Architecture**: Secure REST APIs following modern security guidelines.

---

### 📊 2. Real-Time Portfolio Analytics
* **Live Overview Metrics**:
  * **Total Market Value**: Calculated dynamically using live asset prices.
  * **Cost Basis**: Tracks total original capital invested in open positions.
  * **Total PnL ($ & %)**: Combined performance metrics across all trades.
  * **Realized PnL**: Locked-in profits or losses from closed trades.
  * **Unrealized PnL**: Floating profits or losses from active open positions.
* **Asset Allocation**: Visual distribution of assets across the portfolio (e.g., Stocks vs. Crypto).
* **Holdings Breakdown**: Detailed breakdown per asset displaying units owned, average buy price, current market value, and specific PnL.
* **Real-Time Scheduler**: Background scheduler continuously updates analytics instantly whenever underlying market prices fluctuate.
<img width="1350" height="620" alt="Screenshot (4293)" src="https://github.com/user-attachments/assets/7e848693-3eb2-43f2-8573-0337215e311b" />
<img width="1366" height="559" alt="Screenshot (4294)" src="https://github.com/user-attachments/assets/eb917bde-7cb2-41df-be23-86448a3b9b8c" />

---

### 🎯 3. Trading & Advanced Order Execution
* **Market Orders**: Instant execution at current market prices.
* **Target Price Orders**: Conditional orders triggered automatically when an asset reaches a specified target price.
* **Risk Management Orders**:
  * **Take Profit (TP)**: Automatically closes positions to secure gains once a profit target is reached.
  * **Stop Loss (SL)**: Automatically triggers sell orders to limit losses if the market declines past a set threshold.
 
<img width="1256" height="617" alt="Screenshot (4313)" src="https://github.com/user-attachments/assets/2c0ead44-1562-444c-8f5c-c3e410db7dde" />
<img width="1272" height="609" alt="Screenshot (4300)" src="https://github.com/user-attachments/assets/2fbb0904-2cc9-40c0-898d-11bb76afb4ae" />
<img width="1286" height="626" alt="Screenshot (4299)" src="https://github.com/user-attachments/assets/8e30df7a-5fc9-4423-938d-ad80a5474c31" />


---

### 💳 4. Wallet System & Financial Ledger
* **Virtual Wallet**: Automated balance deductions and additions for buy/sell operations.
* **Detailed Transaction Ledger**: Detailed audit trail recording every financial movement (deposits, withdrawals, trades) for total financial transparency.
<img width="1282" height="501" alt="Screenshot (4296)" src="https://github.com/user-attachments/assets/a8db60c3-b074-44a7-a0a7-38f882f3c9d5" />
<img width="1274" height="585" alt="Screenshot (4295)" src="https://github.com/user-attachments/assets/15350335-53d1-407e-b292-df7e9f07e9f1" />
<img width="1209" height="580" alt="sG61Q" src="https://github.com/user-attachments/assets/a92fbad2-c542-477a-93d4-bf396388417f" />

<img width="1584" height="656" alt="LZEkm" src="https://github.com/user-attachments/assets/fc9157aa-3169-4b02-a550-7bb4944d00fc" />



---

### 📈 5. Asset Management & Market Simulation
* **Multi-Asset Support**: Real-time simulation for **Stocks** and **Cryptocurrencies**.
* **Dynamic Market Engine**: Scheduled tasks simulate real-world market price fluctuations.
* **Admin Dashboard Capabilities**: Adding new assets, specifying asset types, setting initial prices, and managing market liquidity.
<img width="1366" height="546" alt="Screenshot (4312)" src="https://github.com/user-attachments/assets/d9c0a8d1-9b04-4543-8c4a-494e1ffb7fbe" />
<img width="1212" height="624" alt="Screenshot (4303)" src="https://github.com/user-attachments/assets/4c80279e-2ae4-40d3-bf54-94ca9d5e4d78" />





---

### 🔔 6. Real-Time Notifications
* Automatic notifications sent to users post-trade execution detailing asset name, executed price, quantity, trade status, and updated wallet balance.

* <img width="1366" height="627" alt="Screenshot (4307)" src="https://github.com/user-attachments/assets/28955a39-94a2-40b6-b80f-a561ef5f3885" />
<img width="1157" height="625" alt="Screenshot (4308)" src="https://github.com/user-attachments/assets/46b31487-d4cf-4621-be0d-a2eeee1fafbf" />



---

## ⚡ Performance & Architecture

* **Redis Caching**: Caching active asset market prices to minimize database load and optimize response latency.
* **Clean Layered Architecture**: Strict separation of concerns (`Controller` → `Service` → `Repository`).
* **Centralized Error Handling**: Custom global exception handlers delivering clear and consistent API responses.
* **Unit Testing**: Service layer business logic and repository queries fully tested using **JUnit 5** and **Mockito**.

---

⭐ Feel free to explore, star the repository, or contribute!
