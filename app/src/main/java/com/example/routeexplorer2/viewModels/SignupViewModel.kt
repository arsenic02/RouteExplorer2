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
import com.example.routeexplorer2.data.rules.Validator
import com.example.routeexplorer2.data.signup.RegistrationUIState
import com.example.routeexplorer2.data.signup.SignupUIEvent
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository): ViewModel() {

    private val TAG = SignupViewModel::class.simpleName
//    var registrationUIState= mutableStateOf(RegistrationUIState())//cuvaju se inputi za registraciju

//    var allValidationsPassed= mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false) //onaj kruzic sto se vrti kada se ucitava

    var username by  mutableStateOf("")
    var firstName by  mutableStateOf("")
    var lastName by  mutableStateOf("")
    var phoneNumber by  mutableStateOf("")
    var imageUri by  mutableStateOf<Uri?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
//    var passwordVisible by mutableStateOf(false)

//    fun resetState() {
//        email = ""
//        password = ""
//        username = ""
//        firstName = ""
//        lastName = ""
//        phoneNumber = ""
//        imageUri = null
//        passwordVisible = false
//    }
    fun registerUser(
        callback: (Boolean, String?) -> Unit
    ) {
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            callback(false, "Molimo popunite sva polja")
            return
        }

//        if (!isValidPhoneNumber(phoneNumber)) {
//            callback(false, " The phone number is not in a valid format.")
//            return
//        }
        // Proveri da li e-mail ima validan format
//        if (!isValidEmail(email)) {
//            // Obavestite korisnika o nevažećem e-mailu
//            callback(false, "The email is not valid.")
//            return
//        }

        // Proveri da li lozinka zadovoljava minimalne kriterijume
        if (!isValidPassword(password)) {
            // Obavestite korisnika o nevalidnoj lozinci
            callback(false, "The password must contain at least 6 characters.")
            return
        }

        // Proveri da li je imageUri prisutan
//        if (!isImageUriValid(imageUri)) {
//            // Obavestite korisnika da nije odabrana slika
//            callback(false, "Please, upload an image.")
//            return
//        }

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

    //nzm zasto je zakomentarisao metodu
//    fun logout(){
//        val firebaseAuth = FirebaseAuth.getInstance()
//
//        firebaseAuth.signOut()
//        val authStateListener = AuthStateListener{
//            if(it.currentUser == null){
//                Log.d(TAG, "Inside sign out complete")
//                RouterExplorerAppRouter.navigateTo(Screen.LoginScreen)
//            }
//            else {
//                Log.d(TAG, "Inside sign out NOT completed")
//            }
//        }
//
//        firebaseAuth.addAuthStateListener (authStateListener)
//    }
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

/*private fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+"
    return email.matches(emailPattern.toRegex())
}*/

private fun isValidPassword(password: String): Boolean {
    // Firebase zahteva minimalno 6 karaktera
    val minLength = 6
    return password.length >= minLength
}

private fun isImageUriValid(uri: Uri?): Boolean {
    return uri != null
}

private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    // Regularni izraz za validaciju broja telefona
    val regex = "^((\\+\\d{1,3}(-| )?)?(\\(?\\d{3}\\)?[-. ]?)?\\d{3}[-. ]?\\d{4})\$".toRegex()
    return regex.matches(phoneNumber)
}