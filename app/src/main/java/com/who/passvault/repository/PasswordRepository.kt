package com.who.passvault.repository

import com.who.passvault.data.PasswordDao
import com.who.passvault.models.Password
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val passwordDao: PasswordDao){
    suspend fun insertPassword(password: Password) = passwordDao.insertPassword(password)
    suspend fun updatePassword(password: Password) = passwordDao.updatePassword(password)
    suspend fun deletePassword(password: Password) = passwordDao.deletePassword(password)
    fun getAllPasswords() = passwordDao.getAllPasswords()

}