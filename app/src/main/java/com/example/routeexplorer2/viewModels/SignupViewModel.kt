package com.example.routeexplorer2.viewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.data.repository.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository): ViewModel() {

    private val TAG = SignupViewModel::class.simpleName


    var signUpInProgress = mutableStateOf(false) //onaj kruzic sto se vrti kada se ucitava

    var username by  mutableStateOf("")
    var firstName by  mutableStateOf("")
    var lastName by  mutableStateOf("")
    var phoneNumber by  mutableStateOf("")
    var imageUri by  mutableStateOf<Uri?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun registerUser(
        callback: (Boolean, String?) -> Unit
    ) {
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            callback(false, "Molimo popunite sva polja")
            return
        }

        ///////
        if (!isValidPhoneNumber(phoneNumber)) {
            callback(false, " The phone number is not in a valid format.")
            return
        }

        if (!isValidEmail(email)) {
            callback(false, "The email is not valid.")
            return
        }

        if (!isValidPassword(password)) {
            callback(false, "The password must contain at least 6 characters.")
            return
        }

        if (!isImageUriValid(imageUri)) {
            callback(false, "Please, upload an image.")
            return
        }

        viewModelScope.launch {
            userRepository.registerUser(
                email,
                password,
                username,
                firstName,
                lastName,
                phoneNumber,
                imageUri
            ) { success ->
                Log.d(TAG,"succes=${success}")
                if (success) {
                    callback(true, "Uspesno ste se registrovali")
                }
                else {
                    callback(false, "Neuspesna registracija")
                }

            }
        }
    }

}

class RegisterViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+"
    return email.matches(emailPattern.toRegex())
}

private fun isValidPassword(password: String): Boolean {
    // Firebase zahteva minimalno 6 karaktera
    val minLength = 6
    return password.length >= minLength
}

private fun isImageUriValid(uri: Uri?): Boolean {
    return uri != null
}

private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val regex = "^((\\+\\d{1,3}(-| )?)?(\\(?\\d{3}\\)?[-. ]?)?\\d{3}[-. ]?\\d{4})\$".toRegex()
    return regex.matches(phoneNumber)
}