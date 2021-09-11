package com.terranullius.gitsearch.framework.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.terranullius.gitsearch.framework.presentation.MainViewModel
import com.terranullius.gitsearch.framework.presentation.util.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Set up Navigation
 * */

@ExperimentalCoroutinesApi
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
    ){

        composable(Screen.Main.route){
            MainScreen(navController = navController, modifier = modifier, viewModel = viewModel)
        }

        composable(Screen.ImageDetail.route){
            ImageDetailScreen(modifier = modifier, viewModel = viewModel)
        }

    }

}