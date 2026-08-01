package com.palak.jcauth.presentation.screens.compose

import android.R
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palak.jcauth.presentation.navigation.Routes
import com.palak.jcauth.presentation.screens.components.OtpTextField
import com.palak.jcauth.presentation.screens.components.SignUpButton
import com.palak.jcauth.presentation.viewModel.PhoneLogInViewModel

@Composable
fun OTPScreen(navController: NavHostController,viewModel: PhoneLogInViewModel) {

    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val activity = context as Activity


    LaunchedEffect(state.otpSent) {
        if (state.otpSent){
            Toast.makeText(context,"OTP Sent Successfully", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess){
            navController.navigate(Routes.HOME){
                popUpTo(Routes.LOGIN){
                    inclusive = true
                }
            }
            viewModel.clearVerification()
        }
    }
    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp), contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Verify OTP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp, color = MaterialTheme.colorScheme.surface
                )

                Text(
                    "Enter 6-digit verification code",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    "+91 ${state.phoneNumber}",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.surfaceDim
                )

                OtpTextField(otp = state.otp, onOtpChange = viewModel::updateOTP)

                state.errorMessage?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(42.dp)
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 1.dp,
                    )
                } else {
                    SignUpButton(
                        "Verify Otp",
                        icon = Icons.Default.Check,
                        onClick = viewModel::verifyOTP
                    )
                }

                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(state.resendLoading){

                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )

                    }else {
                        Text(
                            "Didn't receive OTP? ",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            "Resend", fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                viewModel.sendOTP(activity, isResend = true)

                            }
                        )
                    }
                }
                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Change Phone Number",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            viewModel.clearVerification()
                            navController.popBackStack()
                        }
                    )
                }


            }
        }
    }
}

