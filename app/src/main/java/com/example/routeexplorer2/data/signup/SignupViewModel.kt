package com.example.routeexplorer2.data.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.routeexplorer2.data.rules.Validator
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class SignupViewModel: ViewModel() {

    private val TAG = SignupViewModel::class.simpleName
    var registrationUIState= mutableStateOf(RegistrationUIState())//cuvaju se inputi za registraciju

    var allValidationsPassed= mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false) //onaj kruzic sto se vrti kada se ucitava
    fun onEvent(event: SignupUIEvent){
        validateDataWithRules()
        when(event){

            is SignupUIEvent.UsernameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    userName = event.username
                )
//                validateDataWithRules()
                printState()
            }
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    firstName=event.firstName
                )
//                validateDataWithRules()
                printState()
            }
            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    lastName = event.lastName
                )
//                validateDataWithRules()
                printState()
            }
            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    email=event.email
                )
//                validateDataWithRules()
                printState()
            }
            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value=registrationUIState.value.copy(
                    password =event.password
                )
//                validateDataWithRules()
                printState()
            }

            is SignupUIEvent.RegisterButtonCLicked ->{
                signUp()
            }
        }
    }

    private fun signUp(){
        Log.d(TAG,"INside_signUp")
        printState()

        createUserInFirebase(
            email=registrationUIState.value.email,
            password=registrationUIState.value.password
        )
        //validateDataWithRules()


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

        allValidationsPassed.value=fNameResult.status && lNameResult.status && uNameResult.status &&
                emailResult.status && passwordResult.status
        //zakomentarisano je zapravo duzi zapis ovoga iznad
//        if(fNameResult.status && lNameResult.status && uNameResult.status &&
//            emailResult.status && passwordResult.status
//            ){
//            allValidationsPassed.value=true
//        }
//        else
//            allValidationsPassed.value=false
    }
    private fun printState(){
        Log.d(TAG,"Inside_printState")
        Log.d(TAG,registrationUIState.value.toString())
    }

    private fun createUserInFirebase(email:String,password:String){

        signUpInProgress.value=true

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                Log.d(TAG,"Inside_OnCompleteListener")
                Log.d(TAG," isSuccessful = ${it.isSuccessful}")

                signUpInProgress.value=false
                if(it.isSuccessful){
                    RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener{
                Log.d(TAG,"Inside_OnFailureListener")
                Log.d(TAG," Exception = ${it.message}")
                Log.d(TAG," Exception = ${it.localizedMessage}")
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