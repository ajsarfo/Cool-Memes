package com.sarftec.coolmemes.data.firebase.source.meme

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sarftec.coolmemes.utils.editSettings
import com.sarftec.coolmemes.utils.readSettings
import kotlinx.coroutines.flow.first
import java.util.*

private val  USER_ID = stringPreferencesKey("user_id")

suspend fun Context.getId() : String {
    var id = readSettings(USER_ID, "").first()
    if(id.isEmpty()) {
        id = UUID.randomUUID().toString()
        editSettings(USER_ID, id)
    }
    return id
}