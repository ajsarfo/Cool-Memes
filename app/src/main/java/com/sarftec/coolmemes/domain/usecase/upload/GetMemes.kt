package com.sarftec.coolmemes.domain.usecase.upload

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.ViewUploadRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemes @Inject constructor(
    private val repository: ViewUploadRepository
) : UseCase<GetMemes.GetParam, GetMemes.GetResult>() {

    override suspend fun execute(param: GetParam?): GetResult {
        if (param == null) return GetResult(Resource.error("Error => Get Upload Param NULL!"))
        return GetResult(repository.getMemes())
    }

    object GetParam : Param
    class GetResult(val result: Resource<Flow<PagingData<Meme>>>) : Response
}