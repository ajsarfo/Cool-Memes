package com.sarftec.coolmemes.domain.model

class Meme(
    val id: Long,
    val image: String,
    var likes: Long,
    var isFavorite: Boolean = false
)