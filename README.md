# 🎵 Java-Music-Player

A clean, modular **WAV music player** developed using **Java Swing** and the **JLayer** audio library.

This project showcases real-time **audio playback**, **GUI-based control**, and **multi-song playlist functionality** — ideal for beginners learning GUI design and audio handling in Java.

---

## 📚 Table of Contents

* [Overview](#-overview)
* [Player UI Preview](#-player-ui-preview)
* [Features](#-features)
* [Quick Setup](#-quick-setup)
* [Tech Stack](#-tech-stack)
* [Architecture](#-architecture)
* [How It Works](#-how-it-works)
* [Personal Achievements](#-personal-achievements)
* [Contributions](#-contributions)
* [Contact & Questions](#-contact--questions)

---

## 🌐 Overview


This project simulates a **basic desktop audio player** capable of managing and playing `.wav` files through a visually intuitive Java Swing interface. The player supports core features such as **Play**, **Pause**, **Resume**, **Stop**, and **Playlist Management**.

Built entirely in Java, this project helped solidify key skills in **object-oriented design**, **multithreading**, and **event-driven programming**.

---

## 🖼️ Player UI Preview

<img width="2559" height="1439" alt="Screenshot 2025-07-19 154242" src="https://github.com/user-attachments/assets/2a8566b9-9eb2-40c3-9365-5da023770843" />


![Player Preview](https://github.com/user-attachments/assets/preview-musicplayer.gif)

---

## 🚀 Features

### 🎧 Core Playback

* **Play, Pause, Resume, Stop** functions for `.wav` files
* Threaded audio playback using **JLayer's Player API**

### 📂 Playlist Handling

* Add multiple files to the playlist at once
* Clear playlist and reload songs dynamically

### 🖱️ User Interface

* Intuitive layout using **Swing** components
* Real-time status updates on playback state
* File chooser dialog to select local audio files

---

## 🛠️ Quick Setup

### ✅ Prerequisites

* Java JDK 17+
* JLayer JAR library (MP3 decoding engine)

### ⚙️ Installation Steps

```bash
git clone https://github.com/khaled1234kh/Java-Music-Player.git
cd Java-Music-Player
javac -cp .:jl1.0.1.jar MusicPlayer.java
java -cp .:jl1.0.1.jar MusicPlayer
```

> Make sure to include `jl1.0.1.jar` in the same directory or linked to your classpath.

---

## 🧰 Tech Stack

| Component | Version  | Description                 |
| --------- | -------- | --------------------------- |
| Java      | 17+      | Main programming language   |
| Swing     | Built-in | GUI rendering toolkit       |
| JLayer    | 1.0.1    | Java MP3 decoder and player |
| Threads   | Built-in | Handles async audio control |

---

## 🧠 Architecture

```
MusicPlayer.java
├── JFrame Setup (Main UI)
│   ├── Playlist Display (JList)
│   ├── Buttons (Play, Pause, etc.)
│   └── File Chooser Dialog
├── Playback Thread
│   ├── JLayer Player Instance
│   ├── Pause/Resume State
│   └── Song Tracking (index, duration)
├── Event Handlers
│   ├── ActionListeners for controls
│   └── Window Close Handler
```

---

## 🔍 How It Works

### 🔊 Playback Engine

* Uses `FileInputStream` → `BufferedInputStream` → `Player` for efficient audio decoding
* Launches a **dedicated thread** to prevent UI blocking during playback
* Supports stopping and replaying songs without crashing the app

### 📋 Playlist Support

* Reads files from user-selected input
* Displays them via a `JList`
* Keeps track of current index and playback status

---

## 🏆 Personal Achievements

This solo-built project provided:

* Hands-on experience with **Java Swing UI development**
* Solid understanding of **audio streaming and thread management**
* Integration of external libraries (`JLayer`) into a structured Java application
* A ready-to-use desktop tool for audio playback

---

## 🤝 Contributions

Want to improve this project? Here's how:

### 🐛 Reporting Issues

Please include:

* Operating system and Java version
* Playback issues, bugs, or crash messages
* Screenshots or screen recordings

### 💡 Feature Suggestions

Some features you can propose:

* Shuffle / Repeat Mode
* Visualizer for audio waves
* Volume control slider
* Drag-and-drop for playlist

---

## 📬 Contact & Questions

For inquiries, improvements, or collaboration:

* **Email:** [khaledabdulla@gmail.com](mailto:khaledabdulla@gmail.com)
* **GitHub:** [github.com/khaled1234kh](https://github.com/khaled1234kh)
* **LinkedIn:** [linkedin.com/in/khaled-mohamed-22a22a325](https://linkedin.com/in/khaled-mohamed-22a22a325)
