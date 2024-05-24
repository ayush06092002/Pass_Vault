package com.who.passvault.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.who.passvault.models.Password

@Database(entities = [Password::class], version = 1, exportSchema = false)
abstract class PasswordDatabase: RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}