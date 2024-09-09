package com.example.routeexplorer2.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.example.routeexplorer2.data.LoginViewModel
import com.example.routeexplorer2.data.UIEvent
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen

@Composable
fun SignUpScreen (loginViewModel: LoginViewModel=viewModel()){

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
                        loginViewModel.onEvent(UIEvent.UsernameChanged(it))
                    }
                )

                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id=R.drawable.ic_person_24),
                    onTextSelected = {
                        loginViewModel.onEvent(UIEvent.FirstNameChanged(it))
                    }
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource(id=R.drawable.ic_person_24),
                    onTextSelected = {
                        loginViewModel.onEvent(UIEvent.LastNameChanged(it))
                    }
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource(id=R.drawable.ic_mail_24),
                    onTextSelected = {
                        loginViewModel.onEvent(UIEvent.EmailChanged(it))
                    }
                )

                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id=R.drawable.ic_lock_24),
                    onTextSelected = {
                        loginViewModel.onEvent(UIEvent.PasswordChanged(it))
                    }
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
                        loginViewModel.onEvent(UIEvent.RegisterButtonCLicked)
                    },
                    //isEnabled = false

                    )

                Spacer(modifier=Modifier.height(20.dp))


            }

    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    SignUpScreen()
}