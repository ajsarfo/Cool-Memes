package com.sarftec.coolmemes.domain.usecase.approve

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ApproveRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemes @Inject constructor(
    private val approveRepository: ApproveRepository
) : UseCase<GetMemes.GetParam, GetMemes.GetResult>() {

    object GetParam : Param
    class GetResult(val result: Resource<Flow<PagingData<Meme>>>) : Response

    override suspend fun execute(param: GetParam?): GetResult {
        if (param == null) return GetResult(Resource.error("Error => Get Unapproved param NULL!"))
        return GetResult(approveRepository.getMemes())
    }
}