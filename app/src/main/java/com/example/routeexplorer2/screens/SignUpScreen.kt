package com.example.routeexplorer2.screens



import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.routeexplorer2.R
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.components.ButtonComponent
import com.example.routeexplorer2.components.ClickableLoginTextComponent

import com.example.routeexplorer2.components.HeadingTextComponent
import com.example.routeexplorer2.components.ImagePicker
import com.example.routeexplorer2.components.MyTextFieldComponent
import com.example.routeexplorer2.components.NormalTextComponent
import com.example.routeexplorer2.components.NumberFieldComponent
import com.example.routeexplorer2.components.PasswordFieldComponent
import com.example.routeexplorer2.viewModels.SignupViewModel
import com.example.routeexplorer2.data.signup.SignupUIEvent
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen


@Composable
fun SignUpScreen (
    signupViewModel: SignupViewModel =viewModel(),//viewModel je bio zakomentarisan
    navController:NavController
){
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
        ){
        Surface(
            //color = Color.White,
            modifier= Modifier
                .fillMaxSize()
                // .background(Color.White)
                .padding(28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier =Modifier.fillMaxSize()){
                //NormalTextComponent(value = stringResource(id= R.string.hello) )
                NormalTextComponent(value = stringResource(id = R.string.register))
                HeadingTextComponent(value = stringResource(id = R.string.routeExpl) )
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.username),
                      painterResource(id=R.drawable.ic_person_24),
                    textValue = signupViewModel.username,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        signupViewModel.username=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.usernameError//.value.emailError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id=R.drawable.ic_person_24),
                    textValue = signupViewModel.firstName,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        signupViewModel.firstName=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError//.value.emailError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource(id=R.drawable.ic_person_24),
                    textValue = signupViewModel.lastName,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        signupViewModel.lastName=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.lastNameError//.value.emailError
                )

                NumberFieldComponent(
                    labelValue = stringResource(id=R.string.phone),
                    painterResource = painterResource(id = R.drawable.ic_phone_android_24),
                    textValue = signupViewModel.phoneNumber,
                    onTextSelected = {
                        signupViewModel.phoneNumber=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.phoneError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.ic_mail_24),
                    textValue = signupViewModel.email,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        signupViewModel.email=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.emailError//.value.emailError
                )

                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource = painterResource(id = R.drawable.ic_lock_24),
                    password = signupViewModel.password,
                    onTextSelected = {

                        signupViewModel.password=it
                    },
                    errorStatus = signupViewModel.registrationUIState.value.passwordError
                )
                Spacer(modifier=Modifier.height(40.dp))

                ImagePicker(signupViewModel.imageUri, !isLoading) { newUri ->
                    signupViewModel.imageUri = newUri // Ažurira sliku kada korisnik izabere novu
                }

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
//                    RouterExplorerAppRouter.navigateTo(Screen.LoginScreen)
                    navController.navigate(Screens.Login.name)
                })

                Spacer(modifier=Modifier.height(40.dp))
                val context = LocalContext.current
                ButtonComponent(
                    value =stringResource(id=R.string.register),
                    onButtonClicked = {
                        //signupViewModel.onEvent(SignupUIEvent.RegisterButtonCLicked)
                        //isLoading = true
                        Log.d(TAG, "Kliknuto na dugme register")
                        signupViewModel.registerUser()
                        { success, toastMsg ->
                            //isLoading = false
                            if (success) {
                                navController.navigate(Screens.GoogleMap.name)
                                signupViewModel.resetState()
                                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    //isEnabled = signupViewModel.allValidationsPassed.value

                )

                Spacer(modifier=Modifier.height(20.dp))


            }

        }

        if(signupViewModel.signUpInProgress.value){
            CircularProgressIndicator()
        }

    }

}

//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen(){
//    SignUpScreen()
//}