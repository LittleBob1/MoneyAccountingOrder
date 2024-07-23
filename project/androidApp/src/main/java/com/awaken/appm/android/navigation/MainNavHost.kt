package com.awaken.appm.android.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.awaken.appm.android.navigation.home.HomePage
import com.awaken.appm.android.navigation.viewmodels.MainNavHostViewModel

@Composable
fun MainNavHost(onThemeUpdated: () -> Unit) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val mainNavHostViewModel = viewModel{
        MainNavHostViewModel(context)
    }

    NavHost(navController, startDestination = if(mainNavHostViewModel.linkIsGet.collectAsState().value) Routes.Home.route else Routes.AddSheet.route) {
        composable(Routes.Home.route) {
            HomePage(navController, onThemeUpdated)
            BackHandler(true) {}
        }
        composable(Routes.AddSheet.route) {
            AddSheetPage(navController)
            BackHandler(true) {}
        }
        composable(Routes.Information.route) {
            InformationPage()
            BackHandler(false) {}
        }
        composable(Routes.Manual.route) {
            ManualPage()
            BackHandler(false) {}
        }
    }
}
