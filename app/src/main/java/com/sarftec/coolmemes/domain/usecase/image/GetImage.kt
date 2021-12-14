package com.sarftec.coolmemes.domain.usecase.image

import android.net.Uri
import com.sarftec.coolmemes.domain.repository.ImageRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class GetImage @Inject constructor(
    private val repository: ImageRepository
) : UseCase<GetImage.ImageParam, GetImage.ImageResult>() {

    override suspend fun execute(param: ImageParam?): ImageResult {
       if(param == null) return ImageResult(Resource.error("Error => GetImage Param NULL!"))
        return ImageResult(
            repository.getImageUri(param.param)
        )
    }

    class ImageParam(val param: String) : Param
    class ImageResult(val result: Resource<Uri>) : Response
}