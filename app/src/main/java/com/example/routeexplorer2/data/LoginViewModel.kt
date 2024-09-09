package com.example.routeexplorer2.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.routeexplorer2.data.rules.Validator

class LoginViewModel: ViewModel() {

    private val TAG =LoginViewModel::class.simpleName
    var registrationUIState= mutableStateOf(RegistrationUIState())//cuvaju se inputi za registraciju

    fun onEvent(event:UIEvent){
        validateDataWithRules()
        when(event){

            is UIEvent.UsernameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    userName = event.username
                )
//                validateDataWithRules()
                printState()
            }
            is UIEvent.FirstNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    firstName=event.firstName
                )
//                validateDataWithRules()
                printState()
            }
            is UIEvent.LastNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    lastName = event.lastName
                )
//                validateDataWithRules()
                printState()
            }
            is UIEvent.EmailChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    email=event.email
                )
//                validateDataWithRules()
                printState()
            }
            is UIEvent.PasswordChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    password =event.password
                )
//                validateDataWithRules()
                printState()
            }

            is UIEvent.RegisterButtonCLicked ->{
                signUp()
            }
        }
    }

    private fun signUp(){
        Log.d(TAG,"INside_signUp")
        printState()

        validateDataWithRules()


    }

    private fun validateDataWithRules(){

        val uNameResult=Validator.validateUserName(
            uName = registrationUIState.value.userName
        )
        val fNameResult= Validator.validateFirstName(
            fName=registrationUIState.value.firstName
        )

        val lNameResult=Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult=Validator.validateEmail(
            email = registrationUIState.value.email
        )

        val passwordResult=Validator.validatePassword(
            password = registrationUIState.value.password
        )
        Log.d(TAG,"Inside_printState")
        Log.d(TAG,"uNameResult=$uNameResult")
        Log.d(TAG,"fNameResult=$fNameResult")
        Log.d(TAG,"lNameResult=$lNameResult")
        Log.d(TAG,"emailResult=$emailResult")
        Log.d(TAG,"passwordResult=$passwordResult")

        registrationUIState.value=registrationUIState.value.copy(
            usernameError =uNameResult.status ,
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError =passwordResult.status
        )
    }
    private fun printState(){
        Log.d(TAG,"Inside_printState")
        Log.d(TAG,registrationUIState.value.toString())
    }
}