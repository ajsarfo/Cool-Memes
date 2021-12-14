package com.sarftec.coolmemes.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.paging.FirebasePagingManager
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseBuildSource
import com.sarftec.coolmemes.data.firebase.source.meme.FirebaseViewUploadMemeSource
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ViewUploadRepository
import com.sarftec.coolmemes.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewUploadRepositoryImpl @Inject constructor(
    private val mapper: FirebaseMemeMapper,
    private val firebaseBuildSource: FirebaseBuildSource,
    private val firebasePagingManager: FirebasePagingManager,
    @ApplicationContext private val context: Context
) : ViewUploadRepository {

    private val firebaseSource = FirebaseViewUploadMemeSource(mapper, context)

    override suspend fun getMemes(): Resource<Flow<PagingData<Meme>>> {
        return Resource.success(
            firebasePagingManager.getMemesForId(-3, firebaseSource)
        )
        // return Resource.error("Error => View Uploads Get not yet implemented!")
    }

    override suspend fun deleteMeme(meme: Meme): Resource<Unit> {
        return firebaseBuildSource.deleteMeme(mapper.toFirebaseMeme(meme))
        //return Resource.error("Error => View uploads Delete not yet implemented!")
    }

    override suspend fun hasMemes(): Resource<Boolean> {
        return firebaseSource.hasMemes()
    }
}