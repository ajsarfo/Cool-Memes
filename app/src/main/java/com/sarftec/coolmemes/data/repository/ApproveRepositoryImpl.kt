package com.sarftec.coolmemes.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.paging.FirebasePagingManager
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseApproveMemeSource
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseBuildSource
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ApproveRepository
import com.sarftec.coolmemes.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApproveRepositoryImpl @Inject constructor(
    private val mapper: FirebaseMemeMapper,
    private val firebaseBuildSource: FirebaseBuildSource,
    private val firebasePagingManager: FirebasePagingManager,
    @ApplicationContext private val context: Context
) : ApproveRepository {

    private val firebaseSource = FirebaseApproveMemeSource(mapper, context)

    override suspend fun getMemes(): Resource<Flow<PagingData<Meme>>> {
        return Resource.success(
            firebasePagingManager.getMemesForId(-3, firebaseSource)
        )
    }

    override suspend fun alterMeme(option: ApproveRepository.Option, meme: Meme): Resource<Unit> {
        return when (option) {
            ApproveRepository.Option.APPROVE -> {
                firebaseBuildSource.approveMeme(mapper.toFirebaseMeme(meme))
            }
            else -> firebaseBuildSource.deleteMeme(mapper.toFirebaseMeme(meme))
        }
    }

    override suspend fun hasMemes(): Resource<Boolean> {
        return firebaseSource.hasMemes()
    }
}