package com.sarftec.coolmemes.domain.usecase.favorite

import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.repository.FavoriteRepository
import com.sarftec.coolmemes.domain.usecase.UseCase
import com.sarftec.coolmemes.utils.Resource
import javax.inject.Inject

class AddFavorite @Inject constructor(
    private val repository: FavoriteRepository
): UseCase<AddFavorite.FavoriteParam, AddFavorite.FavoriteResult>() {

    override suspend fun execute(param: FavoriteParam?): FavoriteResult? {
       if(param == null) return FavoriteResult(
           Resource.error("Error => Add Favorite Param NULL!")
       )
        return FavoriteResult(repository.addFavorite(param.param))
    }

    class FavoriteParam(val param: Meme) : Param
    class FavoriteResult(val result: Resource<Unit>) : Response
}