package com.who.passvault.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.who.passvault.models.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords")
    fun getAllPasswords(): Flow<List<Password>>

    @Insert
    fun insertPassword(password: Password)

    @Update
    fun updatePassword(password: Password)

    @Delete
    fun deletePassword(password: Password)
}