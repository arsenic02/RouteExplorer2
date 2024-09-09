package com.example.routeexplorer2.data.rules

object Validator {

    fun validateUserName(uName:String):ValidationResult{
         return ValidationResult(
             (!uName.isNullOrEmpty() && uName.length >=6 )
        )
    }

    fun validateFirstName(fName:String):ValidationResult{
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >=6 )
        )
    }

    fun validateLastName(lName:String):ValidationResult{
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >=6 )
        )
    }

    fun validateEmail(email:String):ValidationResult{
        return ValidationResult(
            (!email.isNullOrEmpty() )
        )
    }

    fun validatePassword(password:String):ValidationResult{
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length>=4 )
        )
    }
}

data class ValidationResult(
    val status:Boolean=false
)