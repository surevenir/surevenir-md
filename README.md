## About Surevenir Android App

Inspired by observing tourists in Bali struggling with language barriers and unreliable information when buying souvenirs, we developed **Surevenir**. This app serves as a bridge between travelers and Balinese craftsmanship, ensuring informed, meaningful, and fairly priced purchases. 

With Surevenir, users can scan items to uncover their significance, authenticity, and the impact on supporting local artisans. Our mission is to enhance travel experiences, promote ethical shopping, support the local economy, and preserve Balinese cultural heritage for future generations.

---

Click the link below to download the latest release:
[![Download Surevenir v1.0](https://img.shields.io/badge/Download-Surevenir--v1.0-blue?style=for-the-badge&logo=android)](https://github.com/surevenir/surevenir-md/releases/download/v1.0/Surevenir-v1.0)

## Features

- **Firebase Authentication**: Secure login with Google Sign-In.
- **Product Favorites**: Save your favorite souvenirs for quick access.
- **Interactive Maps**: Explore marked shops and markets with detailed information and directions.
- **Notification Screen**: View a history of notifications triggered by geofencing or Firebase Cloud Messaging (FCM).
- **FCM Notifications**: Receive updates and alerts about offers and cultural events.
- **Geofencing**: Get notified when within 1 km of a nearby market or shop.
- **Souvenir Scanning**: Discover details of a souvenir by scanning its image.
- **Scan History**: Keep track of all scanned items for future reference.
- **Cart and Checkout**: Add items to the cart and seamlessly proceed with checkout.
- **Gamification**: Leaderboard points system to reward users who scan the most souvenirs.

## App ScreenShot

![App Screenshot](https://drive.google.com/file/d/1wEwPGXnZpjNWCX8oIrq8IK_XnJ68mlm6/view?usp=sharing)  
*See more screenshots [here](https://drive.google.com/drive/folders/11eHSSGwNUmTrK9dH8kqzp9DlHTFRnKzW?usp=drive_link).*

## Demo Video

We have both on-device and live-action demo videos. Check them out below!

### On Device


### Live Action

[![On Device Demo](https://img.youtube.com/vi/<VIDEO_ID>/0.jpg)](https://www.youtube.com/watch?v=<VIDEO_ID>)

Click the link below to download the latest release:

[![Live Action Demo](https://img.youtube.com/vi/FlkcaorMKwc/0.jpg)](https://www.youtube.com/watch?v=FlkcaorMKwc)

## Tools, Integration, and Libraries

### 1. **Kotlin**
   - Main programming language used for developing this Android application. Supports null safety, coroutines, and other modern features.

### 2. **Android Studio**
   - The official IDE for Android development, used for coding, debugging, and testing the application.

### 3. **Jetpack Compose**
   - A modern UI toolkit from Google for building declarative and responsive user interfaces.

### 4. **Coil (Coil-kt)**
   - A library for loading and managing images quickly and efficiently.
   - **Usage:** Display souvenir images from the server or local resources.

### 5. **Hilt**
   - A dependency injection library that simplifies component management throughout the application.
   - **Usage:** Enhances modularity and management of objects like ViewModel, Repository, and more.

### 6. **Retrofit**
   - A library for RESTful API communication.
   - **Usage:** Fetch data for souvenirs, locations, and users from the server.

### 7. **Paging 3**
   - A library for efficiently loading large data sets in chunks.
   - **Usage:** Implements a souvenir list with gradual loading features.

### 8. **Gson**
   - A library for JSON serialization/deserialization.
   - **Usage:** Simplifies parsing API data into Kotlin objects.

### 9. **LottieFiles**
   - A library for playing JSON-based animations.
   - **Usage:** Provides interactive animations for signup success.

### 10. **Firebase**
   - **Firebase Cloud Messaging (FCM):** Sends notifications to users.
   - **Firebase Authentication:** Authenticates users via email and Google,.

### 11. **Google Maps SDK**
   - **Geofencing:** Alerts users when entering specific zones.
   - **Maps SDK:** Displays store or souvenir locations on an interactive map.

### 12. **CameraX**
   - A framework for camera integration on Android.
   - **Usage:** Allows users to take photos or scan items to find related souvenirs.

## Requirements

- **Compile SDK:** 34
- **Minimum SDK:** 21 (Android 5.0 Lollipop)
- **Target SDK:** 34
- **Build Tools Version:** 34.0.0
- **Kotlin Version:** 1.8.10 or later
- **Gradle Version:** 8.0.0 or later

---

## Additional Notes

- Ensure the Google Maps API Key is correctly configured in the `AndroidManifest.xml` or via `local.properties`.
- Add the `google-services.json` file in the `app/` directory for Firebase integration.
- Use Android Studio Electric Eel or later for full compatibility.
- Internet access is required for API calls, Firebase, and Maps functionalities.
- Grant necessary permissions like location, camera, and storage to ensure smooth operation of features.
