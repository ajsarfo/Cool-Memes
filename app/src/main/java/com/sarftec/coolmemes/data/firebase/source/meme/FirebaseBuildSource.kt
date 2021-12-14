package com.sarftec.coolmemes.data.firebase.source.meme

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.sarftec.coolmemes.data.FIREBASE_IMAGE_FOLDER
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.model.FirebaseMeme
import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

open class FirebaseBuildSource @Inject constructor(
    protected val mapper: FirebaseMemeMapper,
   @ApplicationContext private val context: Context
) {

    private val firebaseRef = FirebaseFirestore.getInstance()

    protected val collectionRef = firebaseRef.collection("memes")

    suspend fun createMeme(imageInfo: ImageInfo): Resource<Meme> {
        return try {
            val wallpaper = FirebaseMeme()
                .also {
                    it.id = Date().time.toString()
                    it.voteCount = (800L until 4000L).random()
                    it.imageLocation = "$FIREBASE_IMAGE_FOLDER/${imageInfo.toFullName()}"
                    it.inReview = false
                    it.userId = context.getId()
                }
            collectionRef.document(wallpaper.id.toString())
                .set(wallpaper)
                .await()
            Resource.success(mapper.toMeme(wallpaper))
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }

    suspend fun deleteMeme(firebaseMeme: FirebaseMeme): Resource<Unit> {
        return try {
            collectionRef.document(firebaseMeme.id.toString())
                .delete()
                .await()
            Resource.success(Unit)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }

    suspend fun approveMeme(firebaseMeme: FirebaseMeme) : Resource<Unit> {
        return try {
            collectionRef.document(firebaseMeme.id.toString())
                .update(FirebaseMeme.FIELD_IN_REVIEW, false)
                .await()
            Resource.success(Unit)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }

    suspend fun increaseLikes(firebaseMeme: FirebaseMeme, value: Int): Resource<Unit> {
        return try {
            firebaseRef.runTransaction { transaction ->
                val docRef = collectionRef.document(firebaseMeme.id!!.toString())
                val newViews = transaction.get(docRef)
                    .getLong(FirebaseMeme.FIELD_LIKES)
                    ?.plus(value)
                transaction.update(docRef, FirebaseMeme.FIELD_LIKES, newViews)
            }
            Resource.success(Unit)
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }
}