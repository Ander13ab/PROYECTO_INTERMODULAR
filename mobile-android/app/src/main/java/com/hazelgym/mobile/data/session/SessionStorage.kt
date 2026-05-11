package com.hazelgym.mobile.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hazelgym.mobile.data.model.SessionUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "hazelgym_session")

class SessionStorage(private val context: Context) {
    private object Keys {
        val token = stringPreferencesKey("token")
        val tokenType = stringPreferencesKey("token_type")
        val userId = longPreferencesKey("user_id")
        val nombre = stringPreferencesKey("nombre")
        val email = stringPreferencesKey("email")
        val role = stringPreferencesKey("role")
    }

    val session: Flow<SessionUser?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val token = preferences[Keys.token] ?: return@map null
            SessionUser(
                token = token,
                tokenType = preferences[Keys.tokenType] ?: "Bearer",
                userId = preferences[Keys.userId] ?: 0L,
                nombre = preferences[Keys.nombre].orEmpty(),
                email = preferences[Keys.email].orEmpty(),
                role = preferences[Keys.role].orEmpty()
            )
        }

    suspend fun save(sessionUser: SessionUser) {
        context.dataStore.edit { preferences ->
            preferences[Keys.token] = sessionUser.token
            preferences[Keys.tokenType] = sessionUser.tokenType
            preferences[Keys.userId] = sessionUser.userId
            preferences[Keys.nombre] = sessionUser.nombre
            preferences[Keys.email] = sessionUser.email
            preferences[Keys.role] = sessionUser.role
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
