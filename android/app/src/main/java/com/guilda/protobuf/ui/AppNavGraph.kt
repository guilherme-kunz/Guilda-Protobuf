package com.guilda.protobuf.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private sealed class Screen(val route: String) {
    data object UserList   : Screen("user_list")
    data object CreateUser : Screen("create_user")
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController    = navController,
        startDestination = Screen.UserList.route,
    ) {
        composable(Screen.UserList.route) {
            UserListScreen(
                onNavigateToCreate = { navController.navigate(Screen.CreateUser.route) }
            )
        }
        composable(Screen.CreateUser.route) {
            CreateUserScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
