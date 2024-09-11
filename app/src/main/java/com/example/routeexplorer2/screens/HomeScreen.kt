package com.example.routeexplorer2.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routeexplorer2.components.AppToolbar
import com.example.routeexplorer2.components.NavigationDrawerBody
import com.example.routeexplorer2.components.NavigationDrawerHeader
import com.example.routeexplorer2.data.home.HomeViewModel
import com.example.routeexplorer2.data.signup.SignupViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    homeViewModel.getUserData()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled=drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(homeViewModel.emailId.value)
            NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList,
                onNavigationItemClicked={
                    Log.d("Coming here","Inside_onNavigationItemClicked")
                    Log.d("Coming here","${it.itemId} ${it.title}")
                } )
        }
    ) {
        Scaffold(
            //gesturesEnabled=drawerState.isOpen,
            topBar = {
                AppToolbar(
                    toolbarTitle = "Home",
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    onMenuClicked = {
                        scope.launch { drawerState.open() } // Otvara meni
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Dodaj svoj sadržaj ekrana
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}