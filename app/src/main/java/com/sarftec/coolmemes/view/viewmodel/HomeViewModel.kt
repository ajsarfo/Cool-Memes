package com.sarftec.coolmemes.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.domain.usecase.meme.GetMemes
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.manager.NetworkManager
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.utils.isCacheEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mapper: MemeUIMapper,
    private val getMemes: GetMemes,
    private val networkManager: NetworkManager,
    getImage: GetImage,
) : BaseViewModel(getImage) {

    private val _memeFlow = MutableLiveData<Resource<Flow<PagingData<MemeUI>>>>()
    val memeFlow: LiveData<Resource<Flow<PagingData<MemeUI>>>>
        get() = _memeFlow

    fun loadMemeFlow() {
        _memeFlow.value = Resource.loading()
        viewModelScope.launch {
            getMemes.execute(GetMemes.GetParam(GetMemes.Position.Empty)).result.let {
                _memeFlow.value = if (it.isSuccess()) mapFlowToViewUI(it.data!!)
                else Resource.error("${it.message}")
            }
        }
    }

    suspend fun hasNetwork(context: Context): Boolean {
        if(context.isCacheEnabled()) return true
        return networkManager.isNetworkAvailable()
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
}