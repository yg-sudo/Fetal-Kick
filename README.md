# ﻿Fetal Kick

A simple one-page Android app designed for pregnant women to track fetal kicks after each meal using a calendar, numeric counters, and optional stopwatch timers.

---

## ✅ Purpose

This app allows expectant mothers to monitor fetal movement patterns daily by logging kick counts during four key meal times — breakfast, lunch, evening snacks, and dinner.

---

## 🧱 Screen Layout

This is a **single-screen** app comprising:

### 🗓️ Calendar (Date Picker)

- Displays the **current date** by default  
- Allows selection of **past dates**
- Automatically **loads existing data** on date change

### 🍽️ Meal Kick Entry Section

There are **four meal sections**, each with:
- A **label** (e.g., "Breakfast")
- A **textbox** for kick count (positive integers only)
- Increment [ + ] and Decrement [ – ] buttons
- An independent **Start/Stop Stopwatch** with MM:SS format

Meals:
- Breakfast
- Lunch
- Evening Snacks
- Dinner

---

## 🧠 Behavior & Functional Requirements

### 🧾 Input Validations
- Only **positive integers** accepted (no decimals/negatives/empty values)
- “+” increases count by 1  
- “–” decreases count by 1 (not below 0)

### ⏱ Stopwatch Functionality
- Each meal has its **own independent stopwatch**
- Shows elapsed time in **MM:SS**
- Toggling **Start/Stop** does **not affect kick count**

### 💾 Auto Save (Per Date)
- Data is **saved automatically** whenever:
  - Count is changed (typed or button-clicked)
  - Stopwatch is stopped
- When date changes:
  - Previously saved data is **loaded**
  - If no data, fields reset to zero

---

No cloud sync

No internet required

# 🎨 UI/UX Design
Clean, intuitive interface using Material Design

Each meal section visually separated using subtle backgrounds or dividers

# 🔐 Permissions & Privacy
No permissions required

No internet usage

100% local data storage

🚀 Future Enhancements (Optional)
(Not part of initial version, but possible future scope)

Reminders for kick counting at meal times

Export data to CSV or PDF

Google Drive backup

Weekly kick insights

# 🤝 Contributing
Pull requests and suggestions are welcome! Please open an issue for major changes.
