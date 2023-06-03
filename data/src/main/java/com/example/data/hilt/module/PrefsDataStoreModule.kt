package com.example.data.hilt.module

import android.content.Context
import com.example.data.common.utils.PrefsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PrefsDataStoreModule {

    @Provides
    fun providePrefsDataStore(
        @ApplicationContext context:Context
    ): PrefsDataStore {
        return PrefsDataStore(context)
    }
}