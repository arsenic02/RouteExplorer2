package com.example.routeexplorer2.screens



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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routeexplorer2.R
import com.example.routeexplorer2.components.ButtonComponent
import com.example.routeexplorer2.components.ClickableLoginTextComponent

import com.example.routeexplorer2.components.HeadingTextComponent
import com.example.routeexplorer2.components.MyTextFieldComponent
import com.example.routeexplorer2.components.NormalTextComponent
import com.example.routeexplorer2.components.PasswordFieldComponent
import com.example.routeexplorer2.data.SignupViewModel
import com.example.routeexplorer2.data.SignupUIEvent
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen

@Composable
fun SignUpScreen (signupViewModel: SignupViewModel=viewModel()){

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
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.UsernameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.usernameError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id=R.drawable.ic_person_24),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource(id=R.drawable.ic_person_24),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.lastNameError
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource(id=R.drawable.ic_mail_24),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.emailError
                )

                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id=R.drawable.ic_lock_24),
                    onTextSelected = {
                        signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.passwordError
                )

//                CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
//                    onTextSelected = {
//                        RouterExplorerAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
//                    })
                Spacer(modifier=Modifier.height(40.dp))

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                    RouterExplorerAppRouter.navigateTo(Screen.LoginScreen)
                })

                Spacer(modifier=Modifier.height(40.dp))

                ButtonComponent(
                    value =stringResource(id=R.string.register),
                    onButtonClicked = {
                        signupViewModel.onEvent(SignupUIEvent.RegisterButtonCLicked)
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value

                )

                Spacer(modifier=Modifier.height(20.dp))


            }

        }

        if(signupViewModel.signUpInProgress.value){ //NZM ZASTO je CRASHOVALA APLIKACIJA  KADA SE KLIKNE REGISTER
            CircularProgressIndicator()
        }

    }

}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    SignUpScreen()
}