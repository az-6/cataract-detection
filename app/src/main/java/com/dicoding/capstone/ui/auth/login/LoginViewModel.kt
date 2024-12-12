package com.dicoding.capstone.ui.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    // Fungsi untuk menangani login
    fun performLogin(email: String, password: String) {
        isLoading.value = true

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.let {
                        getUserDataFromFirestore(it.uid)
                    }
                } else {
                    errorMessage.value = "Invalid email or password"
                }
            }
    }

    // Fungsi untuk mengambil data pengguna dari Firestore
    private fun getUserDataFromFirestore(uid: String) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userLiveData.value = mAuth.currentUser
                } else {
                    errorMessage.value = "User data not found"
                }
            }
            .addOnFailureListener {
                errorMessage.value = "Failed to fetch user data"
            }
    }
}
