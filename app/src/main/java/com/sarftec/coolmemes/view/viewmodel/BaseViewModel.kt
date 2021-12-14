package com.sarftec.coolmemes.view.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI

abstract class BaseViewModel(
    private val getImage: GetImage
) : ViewModel() {

    suspend fun getImage(memeUI: MemeUI.Model): Resource<Uri> {
        return getImage.execute(GetImage.ImageParam(memeUI.meme.image)).result
    }
}