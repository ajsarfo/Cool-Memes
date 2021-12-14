package com.sarftec.coolmemes.domain.usecase.upload

import com.sarftec.coolmemes.domain.repository.ViewUploadRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class HasMeme @Inject constructor(
    private val repository: ViewUploadRepository
): UseCase<HasMeme.HasParam, HasMeme.HasResult>() {


    override suspend fun execute(param: HasParam?): HasResult {
       if(param == null) return HasResult(Resource.error("Error => Has Meme Param NULL!"))
        return HasResult(
            repository.hasMemes()
        )
    }

    object HasParam : UseCase.Param
    class HasResult(val result: Resource<Boolean>) : UseCase.Response
}