package com.dicoding.capstone.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData for managing visibility or other UI states if needed
    val welcomeMessage = MutableLiveData<String>()

    init {
        // Example: you could initialize some welcome message based on user data
        welcomeMessage.value = "Welcome to the App!"
    }
}
