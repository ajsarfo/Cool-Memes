package com.sarftec.coolmemes.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.coolmemes.data.DATA_PAGE_SIZE
import com.sarftec.coolmemes.data.room.MemeDatabase
import com.sarftec.coolmemes.data.room.mapper.RoomFavoriteMapper
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.FavoriteRepository
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val appDatabase: MemeDatabase,
    private val mapper: RoomFavoriteMapper
) : FavoriteRepository {

    override suspend fun getFavoriteList(): Resource<List<Meme>> {
        return Resource.success(
            appDatabase.roomFavoriteDao().getFavorites().map {
                mapper.toMeme(it)
            }
        )
        //return Resource.error("Error => Get Favorites not yet implemented!")
    }

    override suspend fun getFavoritePaging(startId: Long): Resource<Flow<PagingData<Meme>>> {
        val flow = Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            appDatabase.roomFavoriteDao().pagingSource(startId)
        }.flow.map { paging ->
            paging.map { mapper.toMeme(it) }
        }
        return Resource.success(flow)
       // return Resource.error("Error => Get Favorites Paging not yet implemented!")

    }

    override suspend fun removeFavorite(meme: Meme): Resource<Unit> {
        appDatabase.roomFavoriteDao().removeFavorite(meme.id)
        return Resource.success(Unit)
       // return Resource.error("Error => Remove Favorite not yet implemented!")
    }

    override suspend fun addFavorite(meme: Meme): Resource<Unit> {
        appDatabase.roomFavoriteDao().addFavorite(
            mapper.toRoomFavorite(meme)
        )
        return Resource.success(Unit)
        //return Resource.error("Error => Add Favorite not yet implemented!")
    }
}