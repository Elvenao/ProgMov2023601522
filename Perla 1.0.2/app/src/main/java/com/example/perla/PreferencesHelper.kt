package com.example.perla;

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val THEME_KEY = "theme_key"
        private const val PRIMARY_COLOR_KEY = "primary_color_key"
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, enabled).apply()
    }

    fun setPrimaryColorKey(colorKey: String) {
        sharedPreferences.edit().putString(PRIMARY_COLOR_KEY, colorKey).apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    fun getPrimaryColorKey(): String {
        return sharedPreferences.getString(PRIMARY_COLOR_KEY, "Yellow") ?: "Yellow"
    }
}
