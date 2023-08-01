package com.example.data.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefsDataStore(context:Context) {
    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    fun <T> get(key: String, type: Class<T>): Flow<T?>{
        return dataStore.data.map { pref ->
            pref[getKey(key, type)]
        } as Flow<T?>
    }

    suspend fun <T> put(key: String, data: T){
        dataStore.edit { preferences ->
            when (data) {
                is Int -> preferences[intPreferencesKey(key)] = data
                is Long -> preferences[longPreferencesKey(key)] = data
                is Float -> preferences[floatPreferencesKey(key)] = data
                is Double -> preferences[doublePreferencesKey(key)] = data
                is String -> preferences[stringPreferencesKey(key)] = data
                is Boolean -> preferences[booleanPreferencesKey(key)] = data
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        }
    }

    private fun <T> getKey(key: String, type: Class<T>) =
        when(type){
            Int::class.java -> intPreferencesKey(key)
            Long::class.java -> longPreferencesKey(key)
            Float::class.java -> floatPreferencesKey(key)
            Double::class.java -> doublePreferencesKey(key)
            String::class.java -> stringPreferencesKey(key)
            Boolean::class.java -> booleanPreferencesKey(key)
            else -> throw IllegalArgumentException("Unsupported data type")
        }
}