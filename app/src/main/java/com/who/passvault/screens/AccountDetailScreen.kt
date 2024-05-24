package com.who.passvault.screens

import EncryptionHelper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.who.passvault.models.Password

@Composable
fun AccountDetailsScreen(
    password: Password,
    onEdit: (password: Password) -> Unit = {},
    onDelete: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val decryptedPassword = EncryptionHelper.decrypt(password.password)
    var accountType by remember { mutableStateOf(password.accountType) }
    var username by remember { mutableStateOf(password.username) }
    var passwordValue by remember { mutableStateOf(decryptedPassword) }
    Log.d("AccountDetailsScreen", "AccountDetailsScreen: $decryptedPassword")
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
            label = { Text("Account Type") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            label = { Text("Password") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                onEdit(password.copy(accountType = accountType, username = username, password = passwordValue))
                onCancel()
            }) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onDelete()
                             onCancel()
                             }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Delete")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

