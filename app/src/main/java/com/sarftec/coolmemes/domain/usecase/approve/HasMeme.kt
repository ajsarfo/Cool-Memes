package com.sarftec.coolmemes.domain.usecase.approve

import com.sarftec.coolmemes.domain.repository.ApproveRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class HasMeme @Inject constructor(
    private val repository: ApproveRepository
): UseCase<HasMeme.HasParam, HasMeme.HasResult>() {

    override suspend fun execute(param: HasParam?): HasResult {
        if(param == null) return HasResult(
            Resource.error("Error => Has Param NULL!")
        )
        return HasResult(repository.hasMemes())
    }

    object HasParam : Param
    class HasResult(val result: Resource<Boolean>) : Response
}