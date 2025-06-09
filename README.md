# 💰 Savora: Smart Budget Tracking App

<div align="center">
  
  ![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-orange?style=for-the-badge&logo=kotlin)
  ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=for-the-badge&logo=android)
  ![Firebase](https://img.shields.io/badge/Firebase-Backend-yellow?style=for-the-badge&logo=firebase)
  ![MPAndroidChart](https://img.shields.io/badge/Charts-MPAndroidChart-blueviolet?style=for-the-badge&logo=chartmogul)
  
  <h3>Track Smarter. Spend Better. Achieve More.</h3>

  <p><em>A mobile-first budgeting app built using Firebase and Jetpack Compose, empowering users to master personal finance.</em></p>

  <a href="https://youtu.be/zGAsvYdTKY8">
    <img src="https://img.shields.io/badge/Watch_Demo-YouTube-red?style=for-the-badge&logo=youtube" alt="Watch Demo on YouTube">
  </a>
</div>

---

## 📱 Overview

**Savora** is an intuitive and powerful Android budgeting app developed with **Kotlin**, **Jetpack Compose**, and **Firebase**. It helps users:

- 🧾 **Monitor daily expenses**
- 📈 **Visualize and manage budget goals**
- 🔍 **Filter & analyze spending insights**
- ☁️ **Sync data in real-time across devices**

---

## 🔑 Key Features

### 🔐 **Authentication**
- Firebase Email/Password authentication  
- Secure, user-specific access

### 📊 **Budget Visualization**
- Interactive bar graphs (MPAndroidChart)  
- Dynamic color indicators:
  - ✅ Green: Within budget  
  - 🟠 Orange: Near limit  
  - 🔴 Red: Over budget  
- Min/Max budget goal lines

### 🌤️ **Firebase Integration**
- **Realtime Database**: Sync expenses instantly  
- **Cloud Storage**: Store image attachments (receipts, docs)  
- Real-time data architecture

### 🚀 **Custom Features**

- 📎 **Attachments and Location Tagging**
  - Upload receipt photos  
  - Tag expenses with GPS location  

- 📝 **Notes and Smart Suggestions**
  - Add personal notes  
  - AI-based suggestions based on user habits

---

## 👥 User Roles

| Role          | Access Capabilities                                  |
|---------------|------------------------------------------------------|
| **Regular User** | Add/View/Delete expenses, apply filters, set goals |

---

## 🧱 Technology Stack

| Layer           | Technology                       |
|-----------------|----------------------------------|
| **Language**    | Kotlin                           |
| **UI Toolkit**  | Jetpack Compose                  |
| **Backend**     | Firebase Realtime Database       |
| **Authentication** | Firebase Auth (Email/Password) |
| **File Storage**| Firebase Cloud Storage           |
| **Charts**      | MPAndroidChart                   |
| **Build Tool**  | Gradle                           |
| **CI/CD**       | GitHub Actions                   |

---

## ⚙️ Setup Instructions

### ✅ Prerequisites

- Android Studio (latest)
- Kotlin SDK
- Firebase Console Account
- Git

### 🔧 Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create new project (e.g., `SavoraApp`)
3. Add Android app and download `google-services.json` to `/app` directory
4. Enable:
   - Firebase Auth (Email/Password)
   - Realtime Database
   - Firebase Storage

### 🔨 Build and Run

```bash
git clone https://github.com/YourUsername/Savora.git
cd Savora

# Open in Android Studio
# Sync Gradle and Run on real device
 📈 Using the App
➕ Add Expense
Input: Amount, Category, Tags

Optional: Note, Photo, GPS Location

🔍 View Expenses
Reverse chronological order

Filter by date, tag, or category

📊 Budget Graph
Category-wise bar chart

Limit indicators for budget

🎛 Filters
📆 Date: Today, Last 7 Days, Month, Custom

🏷️ Tags: "Work", "Personal", etc.

📂 Category: Food, Transport, Rent, etc.

🔄 GitHub and CI/CD
📁 Version Control
Firebase Auth Integration

Expense CRUD Operations

Budget Visualization

Smart Suggestions + Attachments

⚙️ GitHub Actions
CI/CD pipeline triggered on push

Workflow file: .github/workflows/build.yml

🧠 Design and Architecture
🧩 MVVM architecture

🌊 StateFlow for reactive UI state

📐 Material Design with modern Compose UI

⚖️ Scalable, maintainable, clean codebase

🎬 Demo Video
Watch full walkthrough:

✅ Login and Auth

✅ Adding and Filtering Expenses

✅ Budget Visualization

✅ AI Suggestions

✅ Real Device Demo

👉 Watch Full Demo on YouTube

✅ PoE Final Checklist
Requirement	Status
Firebase Auth	✅
Firebase Database	✅
Firebase Storage	✅
Graph Visualization	✅
Budget Limits UI	✅
Filtering System	✅
Attachments + Suggestions	✅
Real Device Deployment	✅
GitHub Repo + CI	✅
Demo Video	✅

🧠 Credits
Developed by:

Christinah Chitombi

Lindokuhle Moyana

Keira Wilmot

Ngobani Moyana

As part of Prog7313 Final PoE

🙏 Special Thanks To:
🔥 Firebase (Backend services)

📊 MPAndroidChart (Visualization)

🖌️ Jetpack Compose (Modern UI)


