package com.sarftec.coolmemes.view.listener

import com.sarftec.coolmemes.view.handler.FetchPictureHandler
import com.sarftec.coolmemes.view.handler.ReadWriteHandler
import com.sarftec.coolmemes.view.parcel.MemeToDetail

interface MemeFragmentListener {
    fun getReadWriteHandler() : ReadWriteHandler
    fun getFetchPictureHandler() : FetchPictureHandler
    fun navigateToDetail(memeToDetail: MemeToDetail)
}