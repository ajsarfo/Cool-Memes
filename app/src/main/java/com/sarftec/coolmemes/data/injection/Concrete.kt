package com.sarftec.coolmemes.data.injection

import android.content.Context
import com.sarftec.coolmemes.data.room.MemeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Concrete {

    @Singleton
    @Provides
    fun appDatabase(@ApplicationContext context: Context): MemeDatabase {
        return MemeDatabase.getInstance(context)
    }
}