# 🌱 GrowTime

![Android](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Java-orange)
![IDE](https://img.shields.io/badge/IDE-Android%20Studio-blue)
![Status](https://img.shields.io/badge/status-In%20Development-yellow)

GrowTime is an **Android gardening assistant** that helps users successfully grow and maintain plants by providing **location-based plant recommendations** and **automated care reminders**.

The app reduces the frustration many beginner gardeners face by combining plant selection guidance with plant care management in one simple application.

---

# 📖 Table of Contents

* [Overview](#overview)
* [Features](#features)
* [Screenshots](#screenshots)
* [Architecture](#architecture)
* [Tech Stack](#tech-stack)
* [Installation](#installation)
* [Testing](#Building-and-testing)
* [Usage](#usage)
* [Evaluation](#evaluation)
* [Future Improvements](#future-improvements)
* [Team](#team)

---

# 🌿 Overview

Gardening success depends on many variables including:

* Hardiness zones
* Watering schedules
* Weather

Most gardeners rely on scattered information from search engines, forums, and multiple apps. These sources often provide **generic advice** that does not account for a user’s specific region.

GrowTime solves this problem by combining:

* 🌎 **Location-based plant recommendations**
* ⏰ **Automated care reminders**
* 🌧 **Weather-aware watering notifications**

This allows users to choose appropriate plants and maintain them successfully.

---

# ✨ Features

### 🌍 Location-Based Plant Recommendations

Uses a user's **zip code** to recommend plants that grow well in their local soil.

### 🌱 Plant Collection Management

Users can create a personal list of plants they are growing and track their care.

### ⏰ Care Reminders

GrowTime generates reminders for:

* Watering

### 🌧 Weather-Aware Notifications

The app integrates with a **weather API** to adjust watering reminders based on rainfall.

### 📱 Simple Mobile Interface

The application includes multiple screens:

* Home Screen
* Plant recommendation screen
* Reminder screen

---

# 📸 Screenshots

*(Add screenshots here once the UI is implemented)*

---

# 🏗 Architecture

GrowTime follows a **modular mobile application architecture**.

```
User
  │
  ▼
Android UI Layer
(Activity / Fragment Screens)
  │
  ▼
Application Logic
(Recommendation Engine + Reminder System)
  │
  ├── Plant Database (JSON)
  ├── Weather API Service
  └── Notification Scheduler
```

### Components

**UI Layer**
Handles user input and screen navigation.

**Recommendation Engine**
Determines which plants are suitable for the user’s region.

**Plant Database**
JSON dataset containing plant information and hardiness zone compatibility.

**Weather API Service**
Retrieves rainfall and weather information.

**Reminder System**
Generates notifications for watering and other plant care tasks.

---

# 🧰 Tech Stack

| Technology            | Purpose                               |
| --------------------- | ------------------------------------- |
| Java                  | Core application programming language |
| Android Studio        | Development environment               |
| Android Navigation UI | Screen navigation                     |
| JSON                  | Plant dataset storage                 |
| Weather API           | Rainfall and weather information      |
| Android Notifications | Care reminder alerts                  |

---

# ⚙ Installation

### Prerequisites

* Android Studio
* Android SDK
* Java 8+

Steps for installing android studio:
If you do not have Jet Brains Tool Box installed yet, install it with steps 1-3
1) Navigate to https://www.jetbrains.com/toolbox-app/
2) Click the drop down on the ".exe" next to the purple download button and select the option 
that corresponds to your machine. e.g. ".exe (Windows)" for a Windows machine.
3) Click download
4) Once it is installed, open the downloaded file. 
5) Click next/ok if it asks for something during setup. 
6) Search for android studio under tools and install it.
7) Click next/ok if it asks for something during setup.

### Clone the Repository

```bash
git clone https://github.com/chesseraf/GrowTime
```

### Run the Application

1. Open the project in **Android Studio**
2. On the top right, there is a side bar with 5 options. Open the device manager (3rd one)
3. Click "Add a new device..." -> "Create a virtual device" -> "medium phone" -> "finish"
4. This will take a bit to install this phone. Once installed, click run next to this medium phone in the device manager
5. Click "run app" which is a triangle in the top of the screen
6. The phone should launch the app on the right side of the screen
### What to do if it did not launch
Below are some problems that have been encountered and what was done to solve them.
1. If it says that gradle needs to sync in the top of the screen, let it sync
2. If the option to run does not appear, it may need to be built. Click the hammer which is the 6th from the bottom in the tool bar on the bottom left corner. Then click the hammer next to it to build. After building, try launching again.

---

# Building and testing
To run automated tests and build, run the following command:
```
./gradlew connectedAndroidTest
```
If the build fails, it will show which tests failed. While it is running, you can see what some of the tests are doing on the device shown on the right side of the screen.


# Coding tests
The code for the tests is in the folder "app\src\androidTest\java\com\example\growtime"

# 🚀 Usage

1. Launch GrowTime.
2. Enter your **zip code**.
3. Browse recommended plants suitable for your region.
4. Add plants to your **plant collection**.
5. Receive reminders when plants need watering.

---

# 📊 Evaluation

The application will be evaluated using two main metrics.

### Plant Recommendation Accuracy

Accuracy will be calculated using:

```
Accuracy (%) =
(Number of correct recommendations ÷ Total recommendations) × 100
```

The system is considered successful if recommendation accuracy exceeds **75%** across tested regions.

### Reminder System Accuracy

The watering notification system will be tested by simulating weather conditions and verifying that reminders adjust correctly when rainfall occurs.

---

# 🔮 Future Improvements

Potential future improvements include:

* 🌎 Global plant support
* 🌱 Expanded plant database
* ☁ Cloud syncing across devices

---

# 👥 Team

GrowTime was developed as part of a **Software Engineering course project**.

Team Members:

* **Gia Panchal**
* **Max Fitzgerald**
* **Christopher Nguyen**
* **Rafael Pashkov**
* **Natalie Resendes**

---

⭐ If you like this project, consider giving it a star!

---
