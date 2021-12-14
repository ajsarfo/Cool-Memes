package com.sarftec.coolmemes.domain.usecase.upload

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ViewUploadRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class DeleteMeme @Inject constructor(
    private val repository: ViewUploadRepository
) : UseCase<DeleteMeme.DeleteParam, DeleteMeme.DeleteResult>() {

    override suspend fun execute(param: DeleteParam?): DeleteResult {
        if (param == null) return DeleteResult(Resource.error("Error => Delete Meme Param NULL!"))
        return DeleteResult(
            repository.deleteMeme(param.meme)
        )
    }

    class DeleteParam(val meme: Meme) : Param
    class DeleteResult(val result: Resource<Unit>) : Response
}