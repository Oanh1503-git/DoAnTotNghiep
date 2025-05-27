package com.example.laptopstore.viewmodels


import android.content.Context

class SessionManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveLoggedInUsername(username: String) {
        prefs.edit().putString("logged_in_user", username).apply()
    }

    fun getLoggedInUsername(): String? {
        return prefs.getString("logged_in_user", null)
    }

    fun clearLoggedInUser() {
        prefs.edit().remove("logged_in_user").apply()
    }
}
