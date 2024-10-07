# Bharat Bijli Corporation Utility Bill Pay Application

Welcome to the **Bharat Bijli Corporation (BBC)**, a unified platform designed to streamline utility bill payments and management.

- **Sushmita Bennisur** - Employee Portal
- **Niraj Surve** - Customer Portal

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Installation Instructions](#installation-instructions)
- [Customer User Guide](#customer-user-guide-with-screenshots)

## Description

The BBC Utility Bill Pay Application includes two main portals:

1. **Customer Portal (Utility Bill Pay App):** This portal enables customers to manage their utility bills, view invoices, make payments, and track their transaction history.
2. **Employee Portal (Operations Portal):** This portal is for BBC employees to manage customer accounts, generate invoices, and handle administrative tasks.

## Features

### Customer Portal (Utility Bill Pay App)

- **Dashboard:** View billing statistics and recent activities.
- **Invoices:** Access all your invoices, with notifications for unpaid bills.
- **Invoice Payment:** Pay outstanding invoices using a preferred payment method.
- **Transaction History:** Review all past transactions and payment statuses.

### Employee Portal (Operations Portal)

- **Dashboard:** Monitor key statistics and business metrics.
- **Customer Management:** Manage customer accounts and access their transaction history.
- **Bill Generation:** Generate utility bills for customers.

## Installation Instructions

Follow the steps below to set up the BBC Utility Bill Pay App locally:

### Prerequisites

- **Node.js** (v16 or higher)
- **Angular CLI** (v17 or higher)
- **Java 17** or higher
- **Maven** (for backend build)
- **MySQL**

### Backend Setup (Spring Boot with MySQL)

1. Clone the repository:
   ```bash
   git clone https://github.com/nirajs-finzly/bbc-portal.git

2. Navigate to backend folder:
   ```bash
   cd backend
   ```
3. Update `application.properties` with your configurations:

4. Run the spring boot project.

### Frontend Setup (Angular)

1. Clone the repository:
   ```bash
   git clone https://github.com/nirajs-finzly/bbc-portal.git
   ```
2. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```
3. Start development server:
   ```bash
   npm start 
   ```
   or

   ```bash
   ng serve 
   ```
4. View the running app on:
   ```bash
   http://localhost:4200/
   ```

### Customer User Guide with Screenshots

#### Customer Portal

##### Login Page

- Enter the customer ID provided by the system. 
- Click the "Submit OTP" button to receive an OTP on your registered email.

![Login Page](docs/screenshots/login-page.png)

##### OTP Page

- Enter the OTP received on your email.
- Click "Login" to access your account.

![Login OTP Page](docs/screenshots/login-otp-page.png)

##### Dashboard

- View an overview of your utility account and billing statistics.

![Dashboard](docs/screenshots/customer-dashboard.png)

##### Invoices Page

- View all your invoices and pay any unpaid invoices by clicking "Pay Now."

![Invoices Page](docs/screenshots/customer-invoices.png)

##### Invoice Payment Details

- After selecting "Pay Now," you'll be redirected to this page to view the invoice details before payment.

![Payment Details](docs/screenshots/payment-details.png)

##### On next step user has to select the appropriate payment method
##### Select Payment Method (Pay with Card)

- Enter your credit or debit card details to complete the payment.

![Card Payment](docs/screenshots/select-payment-method.png)


##### Select Payment Method (Pay with Net Banking)

- Enter your bank details for net banking payment.

![Card Payment](docs/screenshots/net-banking.png)

##### Select Payment Method (Pay with UPI)

- Enter your UPI ID to pay through UPI.

![Card Payment](docs/screenshots/upi.png)

##### Confirm Payment with OTP

- Click "Confirm Payment" to receive an OTP for payment confirmation via email.
- Enter the OTP to complete the transaction.

![Confirm Payment](docs/screenshots/confirm-payment-otp.png)

- After a successful payment, you will see a confirmation popup.

##### Payment Success Popup

- If the payment fails, you will see a failure notification.

![Payment Success](docs/screenshots/payment-success.png)

##### Payment Failure Popup

- If the payment is failed, then user will see a popup of failed payment.

![Payment Failure](docs/screenshots/payment-failure.png)

##### Customer Transactions

- View your transaction history, including successful and failed transactions.

![Customer Transactions](docs/screenshots/customer-transactions.png)