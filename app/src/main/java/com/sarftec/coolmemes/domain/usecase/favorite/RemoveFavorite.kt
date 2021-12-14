package com.sarftec.coolmemes.domain.usecase.favorite

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.FavoriteRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class RemoveFavorite @Inject constructor(
    private val repository: FavoriteRepository
): UseCase<RemoveFavorite.FavoriteParam, RemoveFavorite.FavoriteResult>() {

    override suspend fun execute(param: FavoriteParam?): FavoriteResult {
        if(param == null) return FavoriteResult(
            Resource.error("Error => Remove Favorite Param NULL!")
        )
        return FavoriteResult(repository.removeFavorite(param.param))
    }

    class FavoriteParam(val param: Meme) : Param
    class FavoriteResult(val result: Resource<Unit>) : Response
}