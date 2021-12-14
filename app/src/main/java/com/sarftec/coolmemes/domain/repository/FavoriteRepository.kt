package com.sarftec.coolmemes.domain.repository

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun getFavoriteList() : Resource<List<Meme>>
    suspend fun getFavoritePaging(startId: Long) : Resource<Flow<PagingData<Meme>>>
    suspend fun removeFavorite(meme: Meme) : Resource<Unit>
    suspend fun addFavorite(meme: Meme) : Resource<Unit>
}