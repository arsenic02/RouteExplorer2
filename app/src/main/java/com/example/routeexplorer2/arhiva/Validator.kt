package com.example.routeexplorer2.arhiva

object Validator {

    fun validateUserName(uName:String): ValidationResult {
         return ValidationResult(
             (!uName.isNullOrEmpty() && uName.length >=5 )
        )
    }

    fun validateFirstName(fName:String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >=3 )
        )
    }

    fun validateLastName(lName:String): ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >=2 )
        )
    }

    fun validateEmail(email:String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty() )
        )
    }

    fun validatePassword(password:String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length>=4 )
        )
    }
}

data class ValidationResult(
    val status:Boolean=false
)