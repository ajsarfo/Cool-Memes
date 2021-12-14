package com.sarftec.coolmemes.view.mapper

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.view.model.MemeUI
import javax.inject.Inject

class MemeUIMapper @Inject constructor(){
    fun toMeme(memeUI: MemeUI.Model) : Meme {
        return memeUI.meme
    }

    fun toMemeUI(meme: Meme) : MemeUI.Model {
        return MemeUI.Model(meme)
    }
}