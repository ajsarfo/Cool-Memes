package com.sarftec.coolmemes.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sarftec.coolmemes.data.room.converter.UriConverter
import com.sarftec.coolmemes.data.room.dao.RoomFavoriteDao
import com.sarftec.coolmemes.data.room.dao.RoomImageDao
import com.sarftec.coolmemes.data.room.model.RoomFavorite
import com.sarftec.coolmemes.data.room.model.RoomImage

@Database(
    entities = [RoomImage::class, RoomFavorite::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UriConverter::class)
abstract class MemeDatabase : RoomDatabase() {

    abstract fun roomImageDao() : RoomImageDao
    abstract fun roomFavoriteDao() : RoomFavoriteDao

    companion object {
        private const val ROOM_DB = "room_database"
        private val lock = Unit
        @Volatile
        private var INSTANCE: MemeDatabase? = null

        fun getInstance(context: Context): MemeDatabase {
            return INSTANCE ?: synchronized(lock) {
                INSTANCE ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): MemeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MemeDatabase::class.java,
                ROOM_DB
            ).fallbackToDestructiveMigration().build()
        }
    }
}