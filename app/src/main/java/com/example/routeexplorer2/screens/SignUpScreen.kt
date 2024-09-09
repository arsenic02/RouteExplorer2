package com.example.routeexplorer2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routeexplorer2.R
import com.example.routeexplorer2.components.ButtonComponent
import com.example.routeexplorer2.navigation.RouterExplorerAppRouter
import com.example.routeexplorer2.navigation.Screen
import com.example.routeexplorer2.components.CheckboxComponent
import com.example.routeexplorer2.components.ClickableLoginTextComponent

import com.example.routeexplorer2.components.HeadingTextComponent
import com.example.routeexplorer2.components.MyTextFieldComponent
import com.example.routeexplorer2.components.NormalTextComponent
import com.example.routeexplorer2.components.PasswordFieldComponent

@Composable
fun SignUpScreen (){

    Surface(
        color = Color.White,
        modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
        ) {
            Column(modifier =Modifier.fillMaxSize()){
                NormalTextComponent(value = stringResource(id= R.string.hello) )
                HeadingTextComponent(value = stringResource(id = R.string.create_account) )
                Spacer(modifier = Modifier.height(20.dp))
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id=R.drawable.ic_launcher_background) //treba ikonica za profil
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource(id=R.drawable.ic_launcher_background) //treba ikonica za profil
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource(id=R.drawable.ic_launcher_background)//treba ikonica za mejl
                )

                PasswordFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id=R.drawable.ic_launcher_background)//treba ikonica za sifru
                )
                
//                CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
//                    onTextSelected = {
//                        RouterExplorerAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
//                    })
                Spacer(modifier=Modifier.height(20.dp))

                ClickableLoginTextComponent(onTextSelected = {

                })

                Spacer(modifier=Modifier.height(20.dp))

                ButtonComponent(value =stringResource(id=R.string.register) )

                Spacer(modifier=Modifier.height(20.dp))


            }

    }
}

@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    SignUpScreen()
}