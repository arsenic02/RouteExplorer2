package com.example.routeexplorer2.data

sealed class SignupUIEvent {

    data class UsernameChanged(val username:String):SignupUIEvent()
    data class FirstNameChanged(val firstName:String):SignupUIEvent()
    data class LastNameChanged(val lastName:String):SignupUIEvent()
    data class EmailChanged(val email:String):SignupUIEvent()
    data class PasswordChanged(val password:String):SignupUIEvent()

    object RegisterButtonCLicked: SignupUIEvent()
}