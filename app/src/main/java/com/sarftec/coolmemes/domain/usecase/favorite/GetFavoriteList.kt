package com.sarftec.coolmemes.domain.usecase.favorite

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.FavoriteRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class GetFavoriteList @Inject constructor(
    private val repository: FavoriteRepository
) : UseCase<GetFavoriteList.FavoriteParam, GetFavoriteList.FavoriteResult>() {

    override suspend fun execute(param: FavoriteParam?): FavoriteResult {
        if(param == null) return FavoriteResult(
            Resource.error("Error => Get Favorites Param NULL!")
        )
        return FavoriteResult(repository.getFavoriteList())
    }

    object FavoriteParam : Param
    class FavoriteResult(val result: Resource<List<Meme>>) : Response
}