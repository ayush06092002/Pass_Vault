package com.who.passvault.di

import android.content.Context
import androidx.room.Room
import com.who.passvault.data.PasswordDao
import com.who.passvault.data.PasswordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesPasswordDao(passwordDatabase: PasswordDatabase) : PasswordDao {
        return passwordDatabase.passwordDao()
    }

    @Singleton
    @Provides
    fun providesPasswordDatabase(@ApplicationContext context: Context) : PasswordDatabase {
        return Room.databaseBuilder(
            context,
            PasswordDatabase::class.java,
            "password_database"
        ).fallbackToDestructiveMigration().build()
    }
}