package com.sarftec.coolmemes.domain.repository

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MemeRepository {
    suspend fun createMeme(imageInfo: ImageInfo) : Resource<Meme>
    suspend fun getMemes(position: Position) : Resource<Flow<PagingData<Meme>>>
    suspend fun increaseLikes(meme: Meme, value: Int) : Resource<Unit>

    sealed class Position {
        class From(val position: Long) : Position()
        object Empty : Position()
    }
}