package com.sarftec.messiwallpapers.data.cache

import android.net.Uri
import com.sarftec.coolmemes.data.cache.UriCache
import com.sarftec.coolmemes.data.room.MemeDatabase
import com.sarftec.coolmemes.data.room.model.RoomImage
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class UriCacheImpl @Inject constructor(
    private val appDatabase: MemeDatabase
) : UriCache {

    override suspend fun getUri(id: String): Resource<Uri> {
        return appDatabase.roomImageDao().getImage(id)
            ?.let { Resource.success(it.uri) }
            ?: Resource.error("Error => Image \'$id\' not found in database!")
    }

    override suspend fun setUri(id: String, uri: Uri) {
        appDatabase.roomImageDao().insertImage(
            RoomImage(id, uri)
        )
    }
}