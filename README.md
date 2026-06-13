# MessageMe 💬

MessageMe is a modern, real-time messaging application built for the Android platform. It enables users to register, sign in, discover other users, and chat in real-time. The application supports rich media capabilities, including text messages, image attachments, and voice recordings, powered by Firebase Firestore and Firebase Cloud Messaging (FCM).

---

## 🚀 Features

- **User Authentication**: Secure Sign-Up and Sign-In flow with email and password, utilizing profile image upload and local preference management.
- **Real-Time Messaging**: Instant one-on-one text messaging powered by Firebase Firestore listeners.
- **Rich Media Sharing**:
  - **Image Sharing**: Send images directly from your gallery (automatically scaled and optimized using Base64 encoding).
  - **Voice Notes**: Touch-and-hold to record audio notes using `MediaRecorder`, stream, and play them directly in the chat bubble using `MediaPlayer`.
- **Recent Conversations list**: A unified home screen showing the latest active chats, last message previews, and relative timestamps, automatically sorted in chronological order.
- **User Presence Tracking**: Real-time availability indicators showing whether a contact is currently online or offline.
- **Device Scaling UI**: Adapts perfectly to different screen sizes and resolutions using scalable size units (SDP/SSP).

---

## 🛠️ Tech Stack & Libraries

- **Language**: Java
- **UI Architecture**: XML Layouts with View Binding
- **Database & Real-Time Sync**: Firebase Firestore
- **Push Notifications**: Firebase Cloud Messaging (FCM)
- **Networking**: Retrofit (for sending push notifications via REST API)
- **Key Libraries**:
  - **Material Components**: Premium UI look & feel.
  - **SDP / SSP**: `com.intuit.sdp`/`ssp` for multi-screen layout auto-scaling.
  - **RoundedImageView**: `com.makeramen:roundedimageview` for elegant circular user profiles.
  - **MultiDex**: Supports larger project build structures.

---

## 📂 Project Architecture

The app is structured cleanly following standard Android package patterns:
- `activities`: Handles UI screens, layout setup, click listeners, and lifecycle bindings (`MainActivity`, `ChatActivity`, `SignInActivity`, etc.).
- `adapters`: Binds datasets to RecyclerViews (`ChatAdapter`, `RecentConversationsAdapter`, `UsersAdapter`).
- `models`: Holds plain Java objects (POJOs) representing app entities (`User`, `ChatMessage`).
- `firebase`: Extends `FirebaseMessagingService` to receive real-time push events.
- `network`: Retrofit API clients and service definitions for remote communication.
- `utilities`: Constants and global preference helpers (`PreferenceManager`, `Constants`).

---

## ⚙️ Setup Instructions

### Prerequisites
- Android Studio Koala (or newer)
- JDK 11 or higher
- A Firebase Project

### Steps to Run Locally

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/raihan12121/MessageMe--A-Messaging-Application-for-Android-Platform.git
   cd MessageMe
   ```

2. **Add Firebase configuration**:
   - Go to the [Firebase Console](https://console.firebase.google.com/) and register a new Android app with the package name `com.example.messageme`.
   - Download the `google-services.json` file.
   - Place `google-services.json` into the `/app` directory of your project folder.

3. **Build the Application**:
   Open the project in Android Studio, wait for Gradle sync to complete, and click **Run** (`Shift + F10`) to compile and install on an emulator or physical device.

---

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
