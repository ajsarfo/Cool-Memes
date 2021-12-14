package com.sarftec.coolmemes.data.room.mapper

import com.sarftec.coolmemes.data.room.model.RoomFavorite
import com.sarftec.coolmemes.domain.model.Meme
import javax.inject.Inject

class RoomFavoriteMapper @Inject constructor() {
    fun toRoomFavorite(meme: Meme) : RoomFavorite {
        return RoomFavorite(
            meme.id,
            meme.image,
            meme.likes
        )
    }

    fun toMeme(roomFavorite: RoomFavorite) : Meme {
        return Meme(
            roomFavorite.id,
            roomFavorite.image,
            roomFavorite.likes
        )
    }
}