package com.who.passvault

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.who.passvault.screens.HomeScreen
import com.who.passvault.ui.theme.PassVaultTheme
import com.who.passvault.utils.BiometricPromptManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PassVaultTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF2F4F9)
                ) {
                    BioAuth()
                }
            }
        }
    }

    @Composable
    private fun BioAuth() {
        var showDefaultScreen by remember { mutableStateOf(true) }
        if (showDefaultScreen) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PassVault",
                )
                Button(
                    onClick = {
                        promptManager.showBiometricPrompt(
                            title = "Authenticate",
                            description = "Please authenticate to continue"
                        )
                    }
                ) {
                    Text("Authenticate")
                }
            }
        }
        val biometricResult by promptManager.promptResult.collectAsState(
            initial = null
        )
        val enrollLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                println("Activity result: $it")
            }
        )

        // Launch biometric enrollment if not set
        LaunchedEffect(biometricResult) {
            if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
                if (Build.VERSION.SDK_INT >= 30) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    enrollLauncher.launch(enrollIntent)
                }
            }
        }

        // Only show the biometric prompt once
        var promptShown by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            if (!promptShown) {
                promptShown = true
                promptManager.showBiometricPrompt(
                    title = "Authenticate",
                    description = "Please authenticate to continue"
                )
            }
        }

        biometricResult?.let { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    promptShown = false
                }

                BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    promptShown = false
                }

                BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                    Toast.makeText(this, "Authentication not set", Toast.LENGTH_SHORT).show()
                    promptShown = false
                }

                BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                    Toast.makeText(this, "Feature unavailable", Toast.LENGTH_SHORT).show()
                    promptShown = false
                }

                BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                    Toast.makeText(this, "Hardware unavailable", Toast.LENGTH_SHORT).show()
                    promptShown = false
                }

                BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    showDefaultScreen = false
                    HomeScreen()
                }
            }
        }
    }
}

