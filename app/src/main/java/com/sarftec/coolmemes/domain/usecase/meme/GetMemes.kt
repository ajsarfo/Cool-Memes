package com.sarftec.coolmemes.domain.usecase.meme

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.MemeRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemes @Inject constructor(
    private val repository: MemeRepository
): UseCase<GetMemes.GetParam, GetMemes.GetResult>() {

    override suspend fun execute(param: GetParam?): GetResult {
        if(param == null) return GetResult(Resource.error("Error => GetMemes Param NULL!"))
        val position = when(param.param) {
            is Position.From -> MemeRepository.Position.From(param.param.position)
            else -> MemeRepository.Position.Empty
        }
        return GetResult(
            repository.getMemes(position)
        )
    }

    class GetParam(val param: Position) : Param
    class GetResult(val result: Resource<Flow<PagingData<Meme>>>) : Response

    sealed class Position {
        class From(val position: Long) : Position()
        object Empty : Position()
    }
}