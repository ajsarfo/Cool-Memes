package com.sarftec.coolmemes.data.repository

import android.net.Uri
import com.sarftec.coolmemes.data.cache.UriCache
import com.sarftec.coolmemes.data.firebase.source.image.FirebaseImageSource
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.repository.ImageRepository
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageSource: FirebaseImageSource,
    private val uriCache: UriCache
): ImageRepository {

    override suspend fun uploadImage(file: File, imageInfo: ImageInfo): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            imageSource.uploadWallpaper(file, imageInfo.toFullName())
        }
    }

    override suspend fun getImageUri(imageName: String): Resource<Uri> {
        val resource = uriCache.getUri(imageName)
        return if(resource.isError()) {
            imageSource.getImageUri(imageName).also {
                if(it.isSuccess()) uriCache.setUri(imageName, it.data!!)
            }
        }
        else resource
    }

    override suspend fun deleteImage(imageName: String): Resource<Unit> {
        return imageSource.deleteImage(imageName)
    }
}