package com.sarftec.coolmemes.view.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.coolmemes.utils.editSettings
import com.sarftec.coolmemes.utils.readSettings
import com.sarftec.coolmemes.view.utils.isCacheEnabled
import com.sarftec.coolmemes.view.utils.setCacheEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    private var isRegistered: Boolean? = null

    private var isCacheEnabled = true

    suspend fun isUserRegistered(context: Context): Boolean {
        return isRegistered ?: context.readSettings(IS_USER_REGISTERED, false).first().also {
            isRegistered = it
        }
    }

    fun isCacheEnabled() : Boolean {
        return this.isCacheEnabled
    }

    fun enableCache(context: Context, isEnabled: Boolean) {
        isCacheEnabled = isEnabled
        viewModelScope.launch {
            context.setCacheEnabled(isEnabled)
        }
    }

    suspend fun registerUser(context: Context): Boolean {
        val value = true
        context.editSettings(IS_USER_REGISTERED,value)
        isRegistered = value
        return value
    }

    suspend fun loadData(context: Context) {
        isUserRegistered(context)
        isCacheEnabled = context.isCacheEnabled()
    }

    companion object {
        val IS_USER_REGISTERED = booleanPreferencesKey("is_user_registered")
    }
}