package com.who.passvault.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.who.passvault.R
import com.who.passvault.data.PasswordViewModel
import java.security.SecureRandom


@Composable
fun AddNewAccountScreen(
    onCancel: () -> Unit = {}
) {
    var accountType by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel = hiltViewModel<PasswordViewModel>()

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account Name") },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Email")},
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        var context = LocalContext.current
        Row {
            Button(modifier = Modifier.width(250.dp),
                onClick = {
                    if(accountType.isEmpty() || username.isEmpty() || password.isEmpty()){
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.addPassword(accountType, username, password)
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C2C2C)
                )) {
                Text("Add New Account", color = Color.White)
            }
        }
    }
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Generate Random Password") },
            text = { Text("Do you want to generate a random strong password?") },
            confirmButton = {
                Button(onClick = {
                    val newPassword = generateRandomPassword()
                    onPasswordChange(newPassword)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    TextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                R.drawable.passwordhide
            else R.drawable.passwordshow

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = image),
                    contentDescription = "Password Visibility"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

fun generateRandomPassword(length: Int = 12): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()_-+=<>?"
    val random = SecureRandom()
    return (1..length)
        .map { chars[random.nextInt(chars.length)] }
        .joinToString("")
}


