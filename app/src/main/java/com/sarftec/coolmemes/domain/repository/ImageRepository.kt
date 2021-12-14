package com.sarftec.coolmemes.domain.repository

import android.net.Uri
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.utils.Resource
import java.io.File

interface ImageRepository {

    suspend fun uploadImage(file: File, imageInfo: ImageInfo): Resource<Unit>

    /*
    * Note => GetImage and deleteImage is irrespective of folder name
     */
    suspend fun getImageUri(imageName: String): Resource<Uri>
    suspend fun deleteImage(imageName: String): Resource<Unit>
}