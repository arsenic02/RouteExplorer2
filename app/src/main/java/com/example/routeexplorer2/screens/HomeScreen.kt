package com.example.routeexplorer2.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routeexplorer2.components.ButtonComponent
import com.example.routeexplorer2.components.HeadingTextComponent
import com.example.routeexplorer2.data.SignupViewModel

@Composable
fun HomeScreen(signupViewModel: SignupViewModel=viewModel()){

    //privremeno, homescreen ce zameniti ekran sa mapom
    Surface(
        //color = Color.White,
        modifier= Modifier
            .fillMaxSize()
            // .background(Color.White)
            .padding(28.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(value = "Home" )
            ButtonComponent(value = "Logout", onButtonClicked = {
                signupViewModel.logout()
            },
                isEnabled = true
                )
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}