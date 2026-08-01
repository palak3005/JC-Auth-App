package com.palak.jcauth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.palak.jcauth.data.repository.AuthRepository
import com.palak.jcauth.presentation.screens.compose.ForgotPasswordScreen
import com.palak.jcauth.presentation.screens.compose.HomeScreen
import com.palak.jcauth.presentation.screens.compose.LogInScreen
import com.palak.jcauth.presentation.screens.compose.OTPScreen
import com.palak.jcauth.presentation.screens.compose.PhoneLoginScreen
import com.palak.jcauth.presentation.screens.compose.SignupScreen
import com.palak.jcauth.presentation.viewModel.PhoneLogInViewModel


@Composable
fun AppNavigation(){
    val  repository = remember {AuthRepository()}
    val navController = rememberNavController()
    val phoneLogInViewModel: PhoneLogInViewModel = viewModel()

    NavHost(navController = navController, startDestination = if(repository.isUserLoggedIn()){Routes.HOME}else{
        Routes.LOGIN}) {
        composable(route = Routes.LOGIN){
            LogInScreen(navController)
        }
        composable (Routes.SIGNUP){
            SignupScreen(navController)
        }
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.FORGOT_PASS){
            ForgotPasswordScreen(navController)
        }

        composable (Routes.PHONE){
            PhoneLoginScreen(navController = navController,
               viewModel = phoneLogInViewModel )
        }
        composable(Routes.OTP) {
            OTPScreen(navController = navController,
                viewModel = phoneLogInViewModel)
        }
    }
}