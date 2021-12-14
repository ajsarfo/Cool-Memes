package com.sarftec.coolmemes.view.model

import com.sarftec.coolmemes.domain.model.Meme

sealed class MemeUI(val viewType: Int) {
    class Model(val meme: Meme) : MemeUI(UI_MODEL)
    class Ad() : MemeUI(UI_AD)

    companion object {
        const val UI_MODEL = 0
        const val UI_AD = 1
    }
}