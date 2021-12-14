package com.sarftec.coolmemes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.favorite.GetFavoriteList
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteList: GetFavoriteList,
    private val mapper: MemeUIMapper,
    getImage: GetImage
) : BaseViewModel(getImage) {

    private val _favoriteMemes = MutableLiveData<Resource<List<MemeUI.Model>>>()
    val favoriteMemes: LiveData<Resource<List<MemeUI.Model>>>
        get() = _favoriteMemes

    fun loadMemes() {
        _favoriteMemes.value = Resource.loading()
        viewModelScope.launch {
            val result = getFavoriteList.execute(GetFavoriteList.FavoriteParam).result
            _favoriteMemes.value = result.takeIf { it.isSuccess() }
                ?.let { Resource.success(mapToViewUI(it.data!!)) }
                ?: Resource.error("${result.message}")
        }
    }

    private fun mapToViewUI(items: List<Meme>): List<MemeUI.Model> {
        return items.map { mapper.toMemeUI(it) }
    }
}