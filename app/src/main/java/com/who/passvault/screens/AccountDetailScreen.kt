package com.who.passvault.screens

import EncryptionHelper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.who.passvault.MainActivity
import com.who.passvault.R
import com.who.passvault.models.Password
import com.who.passvault.utils.BiometricPromptManager

@Composable
fun AccountDetailsScreen(
    password: Password,
    onEdit: (password: Password) -> Unit = {},
    onDelete: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val activity = LocalContext.current as MainActivity
    val promptManager by lazy {
        BiometricPromptManager(activity)
    }
    val decryptedPassword = EncryptionHelper.decrypt(password.password)
    var accountType by remember { mutableStateOf(password.accountType) }
    var username by remember { mutableStateOf(password.username) }
    var passwordValue by remember { mutableStateOf(decryptedPassword) }
    var passwordVisible by remember { mutableStateOf(false) }
    var unlockedOnce by remember { mutableStateOf(false) }
    Log.d("AccountDetailsScreen", "AccountDetailsScreen: $decryptedPassword")
    Text(
        modifier = Modifier.padding(start = 24.dp),
        text = "Account Details",
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        color = Color(0xFF3F7DE2)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account Type") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Email") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val biometricResult by promptManager.promptResult.collectAsState(
            initial = null
        )
        LaunchedEffect(biometricResult) {
            if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
                passwordVisible = !passwordVisible
                unlockedOnce = true
            }else if(biometricResult != null){
                Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = { passwordValue = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    R.drawable.passwordhide
                else R.drawable.passwordshow

                IconButton(onClick = {
                    if(!unlockedOnce) {
                        promptManager.showBiometricPrompt(
                            title = "Authenticate",
                            description = "Please authenticate to continue"
                        )
                    }
                    else{
                        passwordVisible = !passwordVisible
                    }
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = image),
                        contentDescription = "Password Visibility"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        var context = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(modifier = Modifier.width(150.dp),
                onClick = {
                    if(accountType.isNotEmpty() && username.isNotEmpty() && passwordValue.isNotEmpty()) {
                        onEdit(
                            password.copy(
                                accountType = accountType,
                                username = username,
                                password = passwordValue
                            )
                        )
                        onCancel()
                    }
                    else{
                        Toast.makeText(context, "Please don't leave fields empty", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C2C2C)
                )) {
                Text("Edit", color = Color.White, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier.width(150.dp),
                onClick = { onDelete()
                             onCancel()
                             }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4646))
            ) {
                Text("Delete", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

