package com.sarftec.coolmemes.data.firebase.source.meme

import android.content.Context
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.model.FirebaseMeme

class FirebaseViewUploadMemeSource(
    mapper: FirebaseMemeMapper,
    private val context: Context
) : FirebaseBaseMemeSource(mapper, context) {

    override suspend fun getOption(): Option {
        return Option.QueryOption(
            collectionRef.whereEqualTo(
                FirebaseMeme.USER_ID, context.getId()
            )
        )
    }
}