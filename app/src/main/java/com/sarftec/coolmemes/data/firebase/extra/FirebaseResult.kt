package com.sarftec.coolmemes.data.firebase.extra

import com.sarftec.coolmemes.domain.model.Meme

class FirebaseResult(
    val data: List<Meme>,
    var nextKey: FirebaseKey? = null,
    var previousKey: FirebaseKey? = null
)