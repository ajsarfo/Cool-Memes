package com.sarftec.coolmemes.data.firebase.mapper

import com.sarftec.coolmemes.data.firebase.model.FirebaseMeme
import com.sarftec.coolmemes.domain.model.Meme
import javax.inject.Inject

class FirebaseMemeMapper @Inject constructor() {

    fun toFirebaseMeme(meme: Meme) : FirebaseMeme {
        return FirebaseMeme(
            id = meme.id.toString(),
            imageLocation = meme.image,
            voteCount = meme.likes
        )
    }

    fun toMeme(meme: FirebaseMeme) : Meme {
        return Meme(meme.id!!.toLong(), meme.imageLocation!!, meme.voteCount ?: 0)
    }
}