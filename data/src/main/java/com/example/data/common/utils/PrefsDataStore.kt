package com.example.data.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefsDataStore(private val context:Context) {
    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val accessFlagKey = booleanPreferencesKey("ACCESS_FLAG")

    /**
     * 앱 최초 접속 플래그를 가져오는 메소드
     * 최초 접속은 권한 허용 홈 화면에 들어가는 순간 적용
     */
    fun getAccessFlag(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[accessFlagKey] ?: false
        }

    suspend fun setAccessFlag(flag:Boolean) {
        context.dataStore.edit {preferences ->
            preferences[accessFlagKey] = flag
        }
    }
}