package com.sarftec.coolmemes.domain.repository

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ViewUploadRepository {
    suspend fun getMemes() : Resource<Flow<PagingData<Meme>>>
    suspend fun deleteMeme(meme: Meme) : Resource<Unit>
    suspend fun hasMemes() : Resource<Boolean>
}