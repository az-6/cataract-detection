package com.dicoding.capstone.ui.auth.signup

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // LiveData for managing loading state and toast messages
    val isLoading = MutableLiveData<Boolean>()
    val toastMessage = MutableLiveData<String>()

    // LiveData for input validation
    val nameError = MutableLiveData<String?>()
    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val signupSuccess = MutableLiveData<Boolean>() // Tambahkan ini

    // Validate user input
    fun validateInputs(name: String, email: String, password: String) {
        if (name.isEmpty()) {
            nameError.value = "Name is required"
        }
        if (email.isEmpty()) {
            emailError.value = "Email is required"
        } else if (!email.contains("@")) {
            emailError.value = "Invalid email format"
        }
        if (password.isEmpty()) {
            passwordError.value = "Password is required"
        } else if (password.length < 6) {
            passwordError.value = "Password must be at least 6 characters"
        }
    }

    // Perform signup with Firebase Authentication
    fun performSignup(name: String, email: String, password: String) {
        isLoading.value = true

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false

                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email
                    )

                    user?.let {
                        firestore.collection("users").document(it.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                toastMessage.value = "Account created successfully"
                                signupSuccess.value = true // Beri sinyal bahwa signup berhasil
                            }
                            .addOnFailureListener { e ->
                                toastMessage.value = "Failed to save data: ${e.message}"
                                signupSuccess.value = false
                            }
                    }
                } else {
                    toastMessage.value = task.exception?.message ?: "Signup failed"
                    signupSuccess.value = false
                }
            }
    }
}
