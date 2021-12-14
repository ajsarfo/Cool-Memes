package com.sarftec.coolmemes.view.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.sarftec.coolmemes.utils.editSettings
import com.sarftec.coolmemes.utils.readSettings
import kotlinx.coroutines.flow.first

private val IS_CACHE_ENABLED = booleanPreferencesKey("is_cache_enabled")

suspend fun Context.isCacheEnabled() : Boolean {
    return readSettings(IS_CACHE_ENABLED, true).first()
}

suspend fun Context.setCacheEnabled(isEnabled: Boolean) {
    editSettings(IS_CACHE_ENABLED, isEnabled)
}