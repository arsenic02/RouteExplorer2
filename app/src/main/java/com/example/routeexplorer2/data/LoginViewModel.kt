package com.example.routeexplorer2.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val TAG =LoginViewModel::class.simpleName
    var registrationUIState= mutableStateOf(RegistrationUIState())//cuvaju se inputi za registraciju

    fun onEvent(event:UIEvent){
        when(event){
            is UIEvent.UsernameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    userName = event.username
                )
                printState()
            }
            is UIEvent.FirstNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    firstName=event.firstName
                )
                printState()
            }
            is UIEvent.LastNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }
            is UIEvent.EmailChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    email=event.email
                )
                printState()
            }
            is UIEvent.PasswordChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    password =event.password
                )
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
    }
    private fun printState(){
        Log.d(TAG,"INside_printState")
        Log.d(TAG,registrationUIState.value.toString())
    }
}