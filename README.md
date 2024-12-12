## About Surevenir Android App

## Features

## App ScreenShot

## Demo Video

### On Device

### Live Action

Click the link below to download the latest release:

[![Download Surevenir v1.0](https://img.shields.io/badge/Download-Surevenir--v1.0-blue?style=for-the-badge&logo=android)](https://github.com/surevenir/surevenir-md/releases/download/v1.0/Surevenir-v1.0)

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
