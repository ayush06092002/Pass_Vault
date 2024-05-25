# PassVault - Password Manager
<img src = "https://github.com/ayush06092002/Pass_Vault/assets/22142132/44b2ddd5-abad-42b0-af14-1c770b591cb9" width="250" height="250" />

PassVault is a secure and user-friendly password manager application built with Jetpack Compose, Hilt for dependency injection, Room for database management, and ViewModel for state management. The application allows users to securely store and manage their passwords.

## Features

- **Add Password**: Securely add new passwords by providing account type (e.g., Gmail, Facebook, Instagram), username/email, and password.
- **View/Edit Password**: View and edit existing passwords, including account details like username/email and password.
- **List Passwords**: Display a list of all saved passwords on the home screen.
- **Delete Password**: Delete passwords securely.
- **Biometric Authentication**: Secure access to the app using biometric authentication.
- **Encryption**: Strong encryption algorithms (AES) to secure password data.
- **Input Validation**: Ensures that mandatory fields are not empty.
- **Random Strong Password Generation**: Can generate a strong 12 characters long Random Password.

## Technologies Used

- **Jetpack Compose**: For building the user interface.
- **Hilt**: For dependency injection.
- **Room**: For local database management.
- **ViewModel**: For managing UI-related data.
- **Kotlin Coroutines**: For asynchronous programming.
- **Biometric Authentication**: For secure access to the app.

## Encryption Techniques

The application uses AES (Advanced Encryption Standard) for encrypting and decrypting password data. AES is a symmetric encryption algorithm that provides a high level of security and performance.

### Encryption Example

```kotlin
object EncryptionHelper {
    private const val transformation = "AES/CBC/PKCS7Padding"
    private val secretKeySpec = SecretKeySpec(yourSecretKey, "AES")
    private val iv = ByteArray(16) // Initialization vector

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        val decodedBytes = Base64.decode(data, Base64.DEFAULT)
        return String(cipher.doFinal(decodedBytes))
    }
}
```

## Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/ayush06092002/Pass_Vault/
    cd Pass_Vault
    ```

2. **Open the project in Android Studio**.

3. **Build the project** to download all dependencies.

4. **Run the project** on an emulator or physical device with biometric capabilities.

## Usage

1. **Home Screen**: View the list of saved passwords.
2. **Add New Account**: Click on the "+" button to add a new password. Fill in the account type, username/email, and password.
3. **View/Edit Account**: Click on an account to view or edit its details. Use the show/hide password button to toggle password visibility.
4. **Delete Account**: Click the "Delete" button to remove an account.
5. **Biometric Authentication**: Authenticate using biometrics to access the app.

## Screenshots

### Add New Account
<img src = "https://github.com/ayush06092002/Pass_Vault/assets/22142132/47be47ef-7489-4a0a-a937-8b8fb29a87a3" width="250" height="555.56" />


### Account Details
<img src = "https://github.com/ayush06092002/Pass_Vault/assets/22142132/19e73d95-79ff-482a-ae6d-4a0539938131" width="250" height="555.56" />
