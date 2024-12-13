# CataScan

CataScan is an Android mobile application designed for early cataract detection using machine learning. The app allows users to upload an eye image for analysis and provides instant detection results, making it a user-friendly tool for healthcare, particularly in resource-limited environments.

---

## MD Implementation Overview

The mobile app integrates a machine learning model through an API, allowing users to upload an eye image for analysis. The model processes the image and returns instant detection results. This functionality ensures that the app is user-friendly and efficient for early cataract detection, enabling users to quickly assess their eye health.

---

## Why This Approach?

- **Portability**: As a mobile application, CataScan is easy to carry and use anywhere, making it convenient for users to check their eye health on the go.
  
- **Accessibility**: The app is designed to be accessible to a wide range of users, including those in remote or underserved regions. It aims to provide an affordable, reliable tool for early cataract detection, even in healthcare settings with limited resources.

- **User-Friendliness**: The interface is intuitive and straightforward, designed to ensure that users, regardless of their technical expertise, can easily upload images and view results.

---

## Mockups and Designs

The app's interface is designed with simplicity and usability in mind. Key features include:

- **Image Uploading**: Users can easily upload an image of their eye for analysis.
- **Detection Results**: After the image is analyzed, the results are displayed instantly, giving users immediate feedback on their eye health.
- **Guided Navigation**: The app guides users step-by-step through the process, ensuring a smooth and seamless experience from start to finish.

---

## Enhancements Using Additional Libraries

To enrich this mobile development project, CataScan integrates several key libraries that enhance the functionality and user experience.

### Firebase Authentication

Firebase is used to securely handle user authentication, allowing users to log in or sign up using various methods (e.g., email, Google accounts). This ensures a personalized experience for each user, while also providing a secure authentication process.

- **Email Sign-up/Log-in**
- **Google Account Sign-in**
- **Secure user authentication**

### Room Database for Local Storage

The Room Database is implemented to store user-uploaded cataract data locally on the device. This ensures that users can access their history of analysis results offline, making the app even more useful in low connectivity areas.

- **Store User Data**: Cataract detection results are stored locally.
- **Offline Access**: Users can view their history even when they are offline.
- **Data Persistence**: Ensures that data is not lost even after app restarts.

### Google Maps Integration

The app integrates Google Maps to show nearby eye clinics or hospitals. This feature provides users with a way to easily find healthcare providers for follow-up consultations after their cataract analysis.

- **Show Nearby Clinics**: Users can view clinics or hospitals in their area.
- **Directions**: Integration with Google Maps enables users to get directions to the nearest healthcare provider.
- **Find Help Quickly**: This feature is particularly useful for users who may need immediate follow-up care.

### Retrofit for API Communication

Retrofit is used to communicate with the backend API that hosts the machine learning model for cataract detection. It handles the image data transmission, ensures efficient data transfer, and provides robust error handling during the analysis process.

- **Efficient API Communication**: Retrofit handles communication with the backend API.
- **Data Transfer**: Uploads eye images and retrieves detection results.
- **Error Handling**: Ensures smooth communication with the backend and handles any errors gracefully.

---

## Installation

To install and run CataScan on your local machine, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/az-6/cataract-detection/tree/main/mobile-development
    ```

2. **Open the project in Android Studio**:
    - Open Android Studio and select `Open an existing project`.
    - Navigate to the directory where the repository is located and open it.

3. **Set up Firebase**:
    - Follow the Firebase setup guide to link your project with Firebase.
    - Enable Firebase Authentication and configure your sign-in methods (e.g., email/password, Google sign-in).

4. **Set up the machine learning API**:
    - Set up the backend API that processes the uploaded images.
    - Use Retrofit to connect the app with the API and fetch analysis results.

5. **Run the app**:
    - Once everything is set up, you can run the app on your Android device or emulator.

---

## Features

- **Cataract Detection**: Upload an eye image for instant analysis.
- **Firebase Authentication**: Secure login/sign-up using Firebase.
- **Offline Data Storage**: Store uploaded images and results locally using Room.
- **Instant Results**: Get immediate feedback on cataract detection after uploading the image.
- **User-Friendly UI**: Easy-to-use interface designed for all users.

---

## Contributing

If you would like to contribute to CataScan, feel free to fork the repository and submit a pull request. We welcome contributions to improve the app's functionality and user experience.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- **Firebase**: For secure and easy authentication services.
- **Room Database**: For offline data storage.
- **Retrofit**: For handling network requests seamlessly.
- **Open-source Contributors**: Thanks to the open-source community for providing libraries and tools to make this app possible.

---

With CataScan, we aim to make cataract detection more accessible and affordable for people everywhere. Your health is important, and this app helps you take the first step toward a clearer vision.

--- 

You can now easily use this updated README for your GitHub project at [Cataract Detection Mobile Development](https://github.com/az-6/cataract-detection/tree/main/mobile-development).
