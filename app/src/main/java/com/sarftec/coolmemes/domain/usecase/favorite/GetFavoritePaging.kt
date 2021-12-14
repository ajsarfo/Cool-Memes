package com.sarftec.coolmemes.domain.usecase.favorite

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.FavoriteRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePaging @Inject constructor(
  private val repository: FavoriteRepository
) : UseCase<GetFavoritePaging.FavoriteParam, GetFavoritePaging.FavoriteResult>(){

    class FavoriteParam(val id: Long) : Param
    class FavoriteResult(val result: Resource<Flow<PagingData<Meme>>>) : Response

    override suspend fun execute(param: FavoriteParam?): FavoriteResult {
        if(param == null) return FavoriteResult(
            Resource.error("Error => Get Favorite Paging Param NULL!")
        )
        return FavoriteResult(
            repository.getFavoritePaging(param.id)
        )
    }
}