package com.sarftec.coolmemes.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.paging.FirebasePagingManager
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseBuildSource
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseMemeSource
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.MemeRepository
import com.sarftec.coolmemes.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemeRepositoryImpl @Inject constructor(
    private val mapper: FirebaseMemeMapper,
    private val firebaseBuildSource: FirebaseBuildSource,
    private val firebasePagingManager: FirebasePagingManager,
    @ApplicationContext private val context: Context
) : MemeRepository {

    override suspend fun createMeme(imageInfo: ImageInfo): Resource<Meme> {
        return withContext(Dispatchers.IO) {
            firebaseBuildSource.createMeme(imageInfo)
        }
        //return Resource.error("Error => Create Meme not yet implemented!")
    }

    override suspend fun getMemes(
        position: MemeRepository.Position
    ): Resource<Flow<PagingData<Meme>>> {
        val id = when (position) {
            is MemeRepository.Position.From -> position.position
            else -> -3
        }
        return Resource.success(
            firebasePagingManager.getMemesForId(id, FirebaseMemeSource(mapper, context))
        )
        //return Resource.error("Error => Get Meme not yet implemented!")
    }

    override suspend fun increaseLikes(meme: Meme, value: Int): Resource<Unit> {
        return firebaseBuildSource.increaseLikes(mapper.toFirebaseMeme(meme), value)
    }
}