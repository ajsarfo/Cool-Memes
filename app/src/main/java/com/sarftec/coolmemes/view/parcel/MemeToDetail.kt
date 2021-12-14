package com.sarftec.coolmemes.view.parcel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MemeToDetail(
    val memeId: Long,
    val isFavorite: Boolean
) : Parcelable