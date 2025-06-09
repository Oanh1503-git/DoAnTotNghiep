package com.example.laptopstore.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khởi tạo DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USERNAME = stringPreferencesKey("username")
        private val CUSTOMER_ID = stringPreferencesKey("customer_id")
        private val USER_TOKEN = stringPreferencesKey("user_token")
    }

    // Lưu trạng thái đăng nhập và tên người dùng
    suspend fun saveLoginState(username: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USERNAME] = username
        }
    }

    // Lưu mã khách hàng
    suspend fun saveCustomerId(customerId: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOMER_ID] = customerId
        }
    }

    // Lưu token người dùng
    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    // Xóa toàn bộ dữ liệu khi đăng xuất
    suspend fun clearLoginState() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Lấy trạng thái đăng nhập
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    // Lấy tên người dùng
    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME]
        }

    // Lấy mã khách hàng
    val customerId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOMER_ID]
        }

    // Lấy token người dùng
    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_TOKEN]
        }
}
