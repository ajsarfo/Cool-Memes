package com.sarftec.coolmemes.data.firebase.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sarftec.coolmemes.data.firebase.extra.FirebaseKey
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseBaseMemeSource
import com.sarftec.coolmemes.domain.model.Meme

class FirebaseMemePagingSource(
    private val repository: FirebaseBaseMemeSource,
    private val startId: Long
) : PagingSource<FirebaseKey, Meme>() {

    override fun getRefreshKey(state: PagingState<FirebaseKey, Meme>): FirebaseKey? {
        return null
    }

    override suspend fun load(params: LoadParams<FirebaseKey>): LoadResult<FirebaseKey, Meme> {
        return try {
            val nextKey = params.key ?: return getFirstPage(startId)
            val result = repository.getMemes(nextKey)

            LoadResult.Page(
                data = result.data,
                nextKey = result.nextKey,
                prevKey = result.previousKey
            )
        } catch (e: Exception) {
            Log.v("TAG", "${e.message}")
            LoadResult.Error(e)
        }
    }

    private suspend fun getFirstPage(id: Long): LoadResult<FirebaseKey, Meme> {
        return try {
            val key = if (isInitialId(id)) FirebaseKey.getInitialKey()
            else FirebaseKey.ID(FirebaseKey.Direction.NEXT, id)

            val result = repository.loadFirstPage(key)
            LoadResult.Page(
                data = result.data,
                nextKey = result.nextKey,
                prevKey = result.previousKey
            )
        } catch (e: Exception) {
            Log.v("TAG", "${e.message}")
            LoadResult.Error(e)
        }
    }

    companion object {
        fun isInitialId(id: Long): Boolean = id < 0
    }
}