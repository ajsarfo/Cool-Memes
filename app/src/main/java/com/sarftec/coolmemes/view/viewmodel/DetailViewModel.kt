package com.sarftec.coolmemes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.favorite.AddFavorite
import com.sarftec.coolmemes.domain.usecase.favorite.GetFavoritePaging
import com.sarftec.coolmemes.domain.usecase.favorite.RemoveFavorite
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.domain.usecase.meme.GetMemes
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.parcel.MemeToDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mapper: MemeUIMapper,
    private val getMemes: GetMemes,
    private val getFavoritePaging: GetFavoritePaging,
    private val addFavorite: AddFavorite,
    private val removeFavorite: RemoveFavorite,
    private val stateHandle: SavedStateHandle,
    getImage: GetImage
) : BaseRetainedViewModel<MemeUI.Model>(getImage) {

    private val _memeFlow = MutableLiveData<Resource<Flow<PagingData<MemeUI>>>>()
    val memeFlow: LiveData<Resource<Flow<PagingData<MemeUI>>>>
        get() = _memeFlow

    fun loadFlow() {
        _memeFlow.value = Resource.loading()
        val parcel = stateHandle.get<MemeToDetail>(PARCEL) ?: let {
            _memeFlow.value = Resource.error("Error => No MemeToDetail Parcel Found!")
            return
        }
        viewModelScope.launch {
            val pagingResult = if (parcel.isFavorite) getFavoritePaging.execute(
                GetFavoritePaging.FavoriteParam(parcel.memeId)
            ).result
            else getMemes.execute(
                GetMemes.GetParam(GetMemes.Position.From(parcel.memeId))
            ).result
            _memeFlow.value = if (pagingResult.isError()) Resource.error("${pagingResult.message}")
            else mapFlowToViewUI(pagingResult.data!!)
        }
    }

    fun getMemeAtPosition(position: Int): MemeUI.Model? {
        return getAtPosition(position)
    }

    fun saveFavorite(memeUI: MemeUI.Model) {
        memeUI.meme.likes += 1
        viewModelScope.launch {
            addFavorite.execute(
                AddFavorite.FavoriteParam(mapper.toMeme(memeUI))
            )
        }
    }

    fun removeFavorite(memeUI: MemeUI.Model) {
        memeUI.meme.likes += -1
        viewModelScope.launch {
            removeFavorite.execute(
                RemoveFavorite.FavoriteParam(mapper.toMeme(memeUI))
            )
        }
    }

    private fun mapFlowToViewUI(flow: Flow<PagingData<Meme>>)
            : Resource<Flow<PagingData<MemeUI>>> {
        return Resource.success(
            flow.map { pagingData ->
                pagingData.map { wallpaper ->
                    mapper.toMemeUI(wallpaper)
                }
            }
        )
    }

    fun setParcel(parcel: MemeToDetail) {
        stateHandle.set<MemeToDetail>(PARCEL, parcel)
    }

    companion object {
        const val PARCEL = "parcel"
    }
}