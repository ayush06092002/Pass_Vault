package com.who.passvault.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.who.passvault.data.PasswordViewModel
import com.who.passvault.models.Password

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(){
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val viewModel = hiltViewModel<PasswordViewModel>()
    Scaffold(modifier = Modifier.padding(16.dp),
        containerColor = Color.Transparent,
        topBar = {
            Text(
                text = "Pass Vault", style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isSheetOpen = true
            },
                containerColor = Color(0xFF3F7DE2)
            ){
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Password",
                    tint = Color.White
                )
            }
        }
    ) {
        val passwords = viewModel.passList.collectAsState().value
        LazyColumn(
            modifier = Modifier.padding(top = 40.dp)
        ) {
            items(passwords) { password ->
                PasswordItem(password = password, onClick = {
                    Log.d("HomeScreen", "Password clicked")
                })
            }
        }

        if(isSheetOpen){
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                },
                containerColor = Color(0xFFF8F8F8)
            ) {
                AddNewAccountScreen { isSheetOpen = false }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordItem(password: Password, onClick: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {
                isSheetOpen = true
                onClick()
            }),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = password.accountType,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF000000)
                )

                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "•••••••", fontSize = 16.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Add Password")
        }
    }
    val viewModel = hiltViewModel<PasswordViewModel>()
    if(isSheetOpen){
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
            },
            containerColor = Color(0xFFF8F8F8)) {
            AccountDetailsScreen(
                password = password,
                onEdit = {
                    viewModel.updatePassword(it)
                },
                { viewModel.deletePassword(password) },
                { isSheetOpen = false }
            )
        }
    }

}
