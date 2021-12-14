package com.sarftec.coolmemes.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sarftec.coolmemes.data.ROOM_FAVORITE_TABLE
import com.sarftec.coolmemes.data.room.model.RoomFavorite

@Dao
interface RoomFavoriteDao {
    @Query("select * from $ROOM_FAVORITE_TABLE")
    suspend fun getFavorites() : List<RoomFavorite>

    @Query("delete from $ROOM_FAVORITE_TABLE where id = :id")
    suspend fun removeFavorite(id: Long)

    @Query("select * from $ROOM_FAVORITE_TABLE where id >= :startId")
    fun pagingSource(startId: Long) : PagingSource<Int, RoomFavorite>
  
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(meme: RoomFavorite)
}