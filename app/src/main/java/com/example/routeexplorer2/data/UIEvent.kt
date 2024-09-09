package com.example.routeexplorer2.data

sealed class UIEvent {

    data class UsernameChanged(val username:String):UIEvent()
    data class FirstNameChanged(val firstName:String):UIEvent()
    data class LastNameChanged(val lastName:String):UIEvent()
    data class EmailChanged(val email:String):UIEvent()
    data class PasswordChanged(val password:String):UIEvent()

    object RegisterButtonCLicked: UIEvent()
}