package com.awaken.appm.android.navigation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.awaken.appm.android.repository.SharedPrefsHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainNavHostViewModel(context: Context) : ViewModel() {

    private val sharedPreferencesHelper = SharedPrefsHelper(context)

    private val _linkIsGet: MutableStateFlow<Boolean> = MutableStateFlow(checkLinkIsGet())
    val linkIsGet: StateFlow<Boolean> = _linkIsGet

    private fun checkLinkIsGet() : Boolean{
        val tableLink = sharedPreferencesHelper.getLink()
        return tableLink.isNotEmpty()
    }
}