package com.sarftec.coolmemes.view.viewmodel

import com.sarftec.coolmemes.domain.usecase.image.GetImage

abstract class BaseRetainedViewModel<T>(
    getImage: GetImage
) : BaseViewModel(getImage){

    private val cacheImageMap = hashMapOf<Int, T>()

    fun setAtPosition(position: Int, wallpaper: T) {
        cacheImageMap[position] = wallpaper
    }

    fun getAtPosition(position: Int): T? {
        return cacheImageMap[position]
    }
}