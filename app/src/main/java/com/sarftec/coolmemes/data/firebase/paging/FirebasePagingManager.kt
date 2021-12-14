package com.sarftec.coolmemes.data.firebase.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sarftec.coolmemes.data.DATA_PAGE_SIZE
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseBaseMemeSource
import com.sarftec.coolmemes.domain.model.Meme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebasePagingManager @Inject constructor(){

    fun getMemesForId(
        id: Long,
        repository: FirebaseBaseMemeSource
    ): Flow<PagingData<Meme>> {
        return Pager(PagingConfig(DATA_PAGE_SIZE.toInt(), enablePlaceholders = false)) {
            FirebaseMemePagingSource(repository, id)
        }.flow
    }
}