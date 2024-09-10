package com.example.routeexplorer2.data.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.routeexplorer2.data.rules.Validator
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel(){

    private val TAG = LoginViewModel::class.simpleName

    var loginUiState= mutableStateOf(LoginUIState())

    var allValidationsPassed= mutableStateOf(false)

    var loginInProgress =mutableStateOf(false)

    fun onEvent(event: LoginUIEvent){
        when(event){
            is LoginUIEvent.EmailChanged ->{
                loginUiState.value=loginUiState.value.copy(
                    email=event.email
                )
            }
            is LoginUIEvent.PasswordChanged ->{
                loginUiState.value=loginUiState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonCLicked ->{
                login()
            }
        }
        validateDataWithRules()
    }

    private fun login() {
        loginInProgress.value=true
        val email=loginUiState.value.email
        val password=loginUiState.value.password
      FirebaseAuth
          .getInstance()
          .signInWithEmailAndPassword(email,password)
          .addOnCompleteListener{

              Log.d(TAG,"Inside login success")
              Log.d(TAG,"${it.isSuccessful}")

              if(it.isSuccessful){
                  loginInProgress.value=false
                  RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
              }
          }
          .addOnFailureListener{
              Log.d(TAG,"Inside failure listener")
              Log.d(TAG,"${it.localizedMessage}")
              loginInProgress.value=false
          }
    }

    private fun validateDataWithRules(){

        val emailResult= Validator.validateEmail(
            email = loginUiState.value.email
        )

        val passwordResult= Validator.validatePassword(
            password = loginUiState.value.password
        )

        loginUiState.value=loginUiState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value=emailResult.status && passwordResult.status

    }
}