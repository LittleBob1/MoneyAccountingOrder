package com.awaken.appm.android.navigation

sealed class Routes(val route: String) {

    data object Home : Routes("homePage")
    data object AddSheet : Routes("addSheetPage")
    data object Information : Routes("informationPage")
    data object Manual : Routes("manualPage")

}