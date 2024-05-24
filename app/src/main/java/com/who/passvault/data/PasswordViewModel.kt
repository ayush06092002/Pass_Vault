package com.who.passvault.data

import EncryptionHelper
import androidx.lifecycle.ViewModel
import com.who.passvault.models.Password
import com.who.passvault.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordRepository) : ViewModel() {
    private val _passList = MutableStateFlow<List<Password>>(emptyList())
    val passList = _passList.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch{
            repository.getAllPasswords().distinctUntilChanged().collect{
                if(it.isNotEmpty()){
                    _passList.value = it
                }
            }
        }
    }
    fun addPassword(accountType: String, username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val encryptedPassword = EncryptionHelper.encrypt(password)
            repository.insertPassword(Password(accountType = accountType, username = username, password = encryptedPassword))
        }
    }

    fun updatePassword(password: Password) {
        CoroutineScope(Dispatchers.IO).launch {
            val encryptedPassword = EncryptionHelper.encrypt(password.password)
            repository.updatePassword(password.copy(
                accountType = password.accountType,
                username = password.username,
                password = encryptedPassword)
            )
        }
    }

    fun deletePassword(password: Password) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deletePassword(password)
            _passList.value = _passList.value.filter { it.id != password.id }
        }
    }

}