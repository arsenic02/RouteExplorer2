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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.routeexplorer2.R
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.components.ButtonComponent
import com.example.routeexplorer2.components.ClickableLoginTextComponent
import com.example.routeexplorer2.components.HeadingTextComponent
import com.example.routeexplorer2.components.MyTextFieldComponent
import com.example.routeexplorer2.components.NormalTextComponent
import com.example.routeexplorer2.components.PasswordFieldComponent
import com.example.routeexplorer2.data.login.LoginUIEvent
import com.example.routeexplorer2.data.login.LoginViewModel
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel /*= viewModel()*/,
    navController: NavController
    ){
//ova 2 dodata
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    //
    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            //color = Color.White,
            modifier= Modifier
                .fillMaxSize()
                // .background(Color.White)
                .padding(28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier=Modifier
                .fillMaxSize()) {
                NormalTextComponent(value = stringResource(id = R.string.login))
                HeadingTextComponent(value = stringResource(id = R.string.routeExpl ))

                Spacer(modifier =Modifier.height(20.dp))

//                MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
//                    painterResource(id =R.drawable.ic_mail_24 ),
//                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
//                    },
//                    errorStatus = loginViewModel.loginUiState.value.emailError
//                )
//
//
//                PasswordFieldComponent(labelValue = stringResource(id = R.string.password),
//                    painterResource(id =R.drawable.ic_lock_24 ),
//                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
//                    },
//                    errorStatus = loginViewModel.loginUiState.value.passwordError
//
//                )

                //modifikovana verzija mejla i passworda
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.ic_mail_24),
                    textValue = loginViewModel.email,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                                     loginViewModel.email=it
                    },
                    errorStatus = loginViewModel.loginUiState.value.emailError
                )

                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource = painterResource(id = R.drawable.ic_lock_24),
                    password = loginViewModel.password,
                    onTextSelected = {
//                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        loginViewModel.password=it
                    },
                    errorStatus = loginViewModel.loginUiState.value.passwordError
                )

                Spacer(modifier =Modifier.height(40.dp))

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    RouterExplorerAppRouter.navigateTo(Screen.SignUpScreen)
                })

                Spacer(modifier =Modifier.height(40.dp))

                ButtonComponent(value = stringResource(id=R.string.login),
                    onButtonClicked = {
                        //loginViewModel.onEvent(LoginUIEvent.LoginButtonCLicked)
                        loginViewModel.loginUserWithEmailAndPassword(
                            loginViewModel.email,
                            loginViewModel.password
                        ) { success ->
                            isLoading = false
                            if (success) {
                                navController.navigate(Screens.GoogleMap.route)
                               // RouterExplorerAppRouter.navigateTo(Screen.HomeScreen)
                                Log.d(TAG,"Logovao sam se")
                               // loginViewModel.resetState()
                            } else {
                                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                   // isEnabled = loginViewModel.allValidationsPassed.value//true
//                    isEnabled=loginViewModel.validateDataWithRules()
                )
            }

        }

        if(loginViewModel.loginInProgress.value){
            CircularProgressIndicator()
        }

    }

}

//@Preview //sluzi tako da imamo preview odnosno gledamo kako na telefnou izgleda
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}