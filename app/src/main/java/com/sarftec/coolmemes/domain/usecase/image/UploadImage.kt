package com.sarftec.coolmemes.domain.usecase.image

import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.repository.ImageRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val imageRepository: ImageRepository
): UseCase<UploadImage.UploadParam, UploadImage.UploadResult>()  {

    override suspend fun execute(param: UploadParam?): UploadResult {
        if(param == null) return UploadResult(
            Resource.error("Null => upload image param!")
        )
        return UploadResult(imageRepository.uploadImage(param.file, param.imageInfo))
    }

    class UploadResult(val result: Resource<Unit>) : Response
    class UploadParam(val file: File, val imageInfo: ImageInfo) : Param
}