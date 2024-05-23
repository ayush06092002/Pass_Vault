package com.who.passvault.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun HomeScreen(){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("HomeScreen", "Floating Action Button clicked")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Password")
            }
        }
    ) {
        var passwords = remember { listOf("password1", "password2", "password3") }
        LazyColumn(
        ) {
            items(passwords) { password ->
                PasswordItem(password = password, onClick = {
                    Log.d("HomeScreen", "Password clicked")
                })
            }
        }
    }
}
@Composable
fun PasswordItem(password: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = password,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF000000)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "•••••••", fontSize = 16.sp)
            }

            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Add Password")
        }
    }
}
