package com.awaken.appm.android.repository

import android.content.Context
import android.content.res.Resources.Theme
import androidx.compose.foundation.isSystemInDarkTheme

class SharedPrefsHelper(val context:Context) {

    val PREF_NAME = "pref"

    val LINK_NAME = "link"
    val THEME_NAME = "theme"

    fun getLink() : String{
        //context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(LINK_NAME, "") ?: ""
    }

    fun setLink(link: String){
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit().putString(LINK_NAME, link).apply()
    }

    fun deleteLink(){
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().remove(LINK_NAME).apply()
    }

    fun setTheme(theme: String){
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit().putString(THEME_NAME, theme).apply()
    }

    fun getTheme(isSystemTheme: Boolean): String{
        val theme = when(isSystemTheme){
            true -> ThemesTypes.DARK
            false -> ThemesTypes.LIGHT
        }
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(THEME_NAME, theme) ?: theme
    }
}

object ThemesTypes {
    const val DARK = "dark"
    const val LIGHT = "light"
}