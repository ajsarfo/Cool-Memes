package com.sarftec.coolmemes.domain.usecase.approve

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ApproveRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class AlterMeme @Inject constructor(
    private val repository: ApproveRepository
): UseCase<AlterMeme.AlterParam, AlterMeme.AlterResult>() {

    override suspend fun execute(param: AlterParam?): AlterResult {
        if(param == null) return AlterResult(Resource.error("Error => Alter Meme param NULL!"))
        val option = when(param.option) {
            Option.APPROVE -> ApproveRepository.Option.APPROVE
            else -> ApproveRepository.Option.DELETE
        }
        return AlterResult(
            repository.alterMeme(option, param.meme)
        )
    }

    class AlterParam(val option: Option, val meme: Meme) : Param
    class AlterResult(val result: Resource<Unit>) : Response

    enum class Option {
        APPROVE, DELETE
    }
}