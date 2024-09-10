package com.example.routeexplorer2.data.login

sealed class LoginUIEvent {

    data class EmailChanged(val email:String): LoginUIEvent()
    data class PasswordChanged(val password:String): LoginUIEvent()

    object LoginButtonCLicked: LoginUIEvent()
}