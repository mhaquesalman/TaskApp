package com.example.taskapp.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taskapp.models.Priority
import com.example.taskapp.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME)

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.dataStore

    suspend fun storeSortState(priority: Priority) {
        dataStore.edit { preference ->
            preference[sortKey] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { exp ->
            if (exp is IOException) emit(emptyPreferences())
            else throw exp
        }
        .map { preferences ->
            val sortState = preferences[sortKey] ?: Priority.NONE.name
            sortState
        }

    suspend fun storeThemeState(isDark: Boolean) {
        dataStore.edit { preference ->
            preference[themeKey] = isDark
        }
    }

    val readThemeState: Flow<Boolean> = dataStore.data
        .catch { exp->
            if (exp is IOException) emit(emptyPreferences())
            else throw exp
        }
        .map { preferences ->
            val themeState = preferences[themeKey] ?: false
            themeState
        }

    private companion object {
        val sortKey = stringPreferencesKey(name = Constants.SORT_BY_PRIORITY)
        val themeKey = booleanPreferencesKey(name = Constants.THEME_STATE)
    }

}