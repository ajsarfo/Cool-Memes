package com.sarftec.coolmemes.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.usecase.image.UploadImage
import com.sarftec.coolmemes.domain.usecase.meme.CreateMeme
import com.sarftec.coolmemes.utils.Event
import com.sarftec.coolmemes.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val uploadImage: UploadImage,
    private val createMeme: CreateMeme
) : ViewModel() {

    private val _uploadMemes = MutableLiveData<List<UploadInfo>>()
    val uploadMemes: LiveData<List<UploadInfo>>
        get() = _uploadMemes

    private val _updateAdapterPosition = MutableLiveData<Event<Int>>()
    val updateAdapterPosition: LiveData<Event<Int>>
        get() = _updateAdapterPosition

    private fun updateAdapterPosition(uploadInfo: UploadInfo) {
        _uploadMemes.value?.let { list ->
            list.indexOfFirst { it.imageInfo == uploadInfo.imageInfo }
        }?.let { _updateAdapterPosition.value = Event(it) }
    }

    suspend fun uploadMeme(uploadInfo: UploadInfo): Resource<Unit> {
        if (uploadInfo.isUploaded) return Resource.success(Unit)
        createMeme(uploadInfo).let {
            if (it.isError()) return Resource.error("${it.message}")
        }
        createImage(uploadInfo.imageInfo).let {
            if (it.isError()) return Resource.error("${it.message}")
        }
        uploadInfo.isUploaded = true
        updateAdapterPosition(uploadInfo)
        return Resource.success(Unit)
    }

    private suspend fun createTempFile(imageInfo: ImageInfo): File {
        val stream = context.contentResolver.openInputStream(imageInfo.uri)
            ?: throw Exception("Cannot open input stream => ${imageInfo.uri}")
        return withContext(Dispatchers.IO) {
            File.createTempFile(
                imageInfo.name,
                ".${imageInfo.extension}",
                context.cacheDir
            ).also { file ->
                file.outputStream().use {
                    stream.copyTo(it)
                }
            }
        }
    }

    private suspend fun createImage(imageInfo: ImageInfo): Resource<Unit> {
        return try {
            uploadImage.execute(
                UploadImage.UploadParam(
                    createTempFile(imageInfo),
                    imageInfo
                )
            ).result
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }

    private suspend fun createMeme(uploadInfo: UploadInfo): Resource<Unit> {
        val resource = createMeme.execute(
            CreateMeme.CreateParam(uploadInfo.imageInfo)
        ).result
        return if (resource.isSuccess()) Resource.success(Unit)
        else Resource.error(resource.message)
    }

    fun setUploadMemes(images: List<ImageInfo>) {
        _uploadMemes.value = images.map {
            UploadInfo(it)
        }
    }

    fun clearUploadMemes() {
        _uploadMemes.value = emptyList()
    }

    fun getUploadMemes(): List<UploadInfo> {
        return _uploadMemes.value?.filter { !it.isUploaded }
            ?: emptyList()
    }

    class UploadInfo(val imageInfo: ImageInfo, var isUploaded: Boolean = false)
}