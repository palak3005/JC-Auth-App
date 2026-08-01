package com.palak.jcauth.presentation.screens.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.palak.jcauth.presentation.screens.components.SignUpButton
import com.palak.jcauth.presentation.screens.components.SignUpTextField
import com.palak.jcauth.presentation.navigation.Routes
import com.palak.jcauth.presentation.viewModel.SignUpViewModel
import com.palak.jcauth.ui.theme.background
import com.palak.jcauth.ui.theme.gray


@Composable
fun SignupScreen(navController: NavHostController) {

    val viewModel: SignUpViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.signupSuccess) {
        if (state.signupSuccess) {
            Toast.makeText(
                context,
                "Signup Successful",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.SIGNUP) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold{ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize().padding(innerPadding)
                .background(Color(0xff0A0C0E))
                .padding( 24.dp).imePadding().verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)

                    .border(0.5.dp, shape = RoundedCornerShape(16.dp), color = gray)
                    .clip(RoundedCornerShape(16.dp))
                    .background(background)
                    .padding(32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Create Account",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Initialize your secure profile to\n"+ "continue.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    SignUpTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "enter email address",
                        icon = Icons.Outlined.Email,
                        label = "Email Address",
                        showTrailingIcon = false,
                        onPasswordVisibilityChange = {},
                        keyboardOption = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    SignUpTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = "enter password",
                        icon = Icons.Outlined.Lock,
                        label = "Password",
                        showTrailingIcon = true,
                        passwordVisible = state.passwordVisible,
                        onPasswordVisibilityChange = viewModel::togglePasswordVisibility,
                        keyboardOption = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )
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
                            text = "Create Account ",
                            icon = Icons.Default.ArrowForward,
                            onClick = viewModel::signUp
                        )
                    }
                    state.errorMessage?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                append("Already have an account? ")
                            }

                            withLink(
                                LinkAnnotation.Clickable(
                                    tag = "login",
                                    linkInteractionListener = {
                                        navController.navigate(Routes.LOGIN)
                                    }
                                )
                            ) {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Log In")
                                }
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
