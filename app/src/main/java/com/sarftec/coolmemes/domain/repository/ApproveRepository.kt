package com.sarftec.coolmemes.domain.repository

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ApproveRepository {
    suspend fun getMemes() : Resource<Flow<PagingData<Meme>>>
    suspend fun alterMeme(option: Option, meme: Meme) : Resource<Unit>
    suspend fun hasMemes() : Resource<Boolean>

    enum class Option {
        APPROVE, DELETE
    }
}