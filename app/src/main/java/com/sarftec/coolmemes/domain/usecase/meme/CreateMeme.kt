package com.sarftec.coolmemes.domain.usecase.meme

import com.sarftec.coolmemes.domain.model.ImageInfo
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.MemeRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class CreateMeme @Inject constructor(
    private val repository: MemeRepository
) : UseCase<CreateMeme.CreateParam, CreateMeme.CreateResult>() {

    override suspend fun execute(param: CreateParam?): CreateResult {
        if (param == null) return CreateResult(Resource.error("Error => Create Meme Param NULL!"))
        return CreateResult(
            repository.createMeme(param.param)
        )
    }

    class CreateParam(val param: ImageInfo) : Param
    class CreateResult(val result: Resource<Meme>) : Response
}