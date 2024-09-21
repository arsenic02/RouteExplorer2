package com.example.routeexplorer2.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routeexplorer2.data.login.LoginUIEvent
import com.example.routeexplorer2.data.login.LoginUIState
import com.example.routeexplorer2.data.repository.UserRepository
import com.example.routeexplorer2.data.rules.Validator
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

//dodato private val userRepository: UserRepository):ViewModel(), a bilo je samo
//class LoginViewModel:ViewModel()
class LoginViewModel(private val userRepository: UserRepository):ViewModel(){

    private val TAG = LoginViewModel::class.simpleName

    var loginUiState= mutableStateOf(LoginUIState())

    var loginInProgress =mutableStateOf(false)

//ova dodato
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)

    fun resetState() {
        email = ""
        password = ""
        passwordVisible = false
    }


    fun loginUserWithEmailAndPassword(
        email: String, password: String, callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.loginWithEmailAndPassword(email, password) { success ->
                if (success) {
                    callback(true)
                } else {
                    callback(false)
                }

            }
        }

    }

    fun signOut(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            userRepository.signOut { success ->
                if (success) {
                    callback(true)
                } else {
                    callback(false)
                }

            }
        }
    }
}

//dodato
class LoginViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}