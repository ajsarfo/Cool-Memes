package com.sarftec.coolmemes.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.utils.Event
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseApproveUploadViewModel(
    getImage: GetImage,
    private val mapper: MemeUIMapper,
) : BaseViewModel(getImage) {

    private val stateHolder = hashMapOf<Meme, State>()

    private val _memeFlow = MutableLiveData<ViewHolderResult>()
    val memeFlow: LiveData<ViewHolderResult>
        get() = _memeFlow

    private val _adapterOption = MutableLiveData<Event<AdapterEvent>>()
    val adapterOption: LiveData<Event<AdapterEvent>>
        get() = _adapterOption

    private val _itemSelected = MutableLiveData<Event<Int>>()
    val itemSelected: LiveData<Event<Int>>
        get() = _itemSelected


    private val _showToast = MutableLiveData<Event<Int>>()
    val showToast: LiveData<Event<Int>>
        get() = _showToast

    abstract suspend fun getFlow(): Resource<Flow<PagingData<Meme>>>

    abstract suspend fun hasMeme(): Resource<Boolean>

    fun loadFlow() {
        _memeFlow.value = ViewHolderResult.MemeResult(Resource.loading())
        viewModelScope.launch {
            _memeFlow.value = hasMeme().let {
                if (it.isError()) ViewHolderResult.MemeResult(
                    Resource.error("${it.message}")
                )
                else if(it.isSuccess() && it.data!!) ViewHolderResult.MemeResult(
                    processFlow()
                )
                else ViewHolderResult.NoMemes
            }
        }
    }

    private suspend fun processFlow(): Resource<Flow<PagingData<MemeUI>>> {
        return getFlow().let {
            if (it.isSuccess()) mapFlowToViewUI(it.data!!)
            else Resource.error("${it.message}")
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

    private fun runStateAction(memeUI: MemeUI.Model, state: State) {
        stateHolder[memeUI.meme] = state
        _adapterOption.value = Event(AdapterEvent)
        _itemSelected.value =  Event(
            stateHolder.filter { it.value == State.SELECTED }.size
        )
    }

    fun getSelectedMemes() : List<MemeUI.Model> {
        return stateHolder.filter { it.value == State.SELECTED }
            .map { mapper.toMemeUI(it.key) }
    }

    protected fun runDeleteMeme(memeUI: MemeUI.Model) {
        runToast()
        runStateAction(memeUI, State.DELETED)
    }

    protected fun runApproveMeme(memeUI: MemeUI.Model) {
        runToast()
        runStateAction(memeUI, State.APPROVED)
    }

   /*
    protected fun filterSetState(state: State) {
        stateHolder
            .filter { it.value == State.SELECTED }
            .forEach { stateHolder[it.key] = state }
        _adapterOption.value = Event(AdapterEvent)
        _itemSelected.value = Event(0)
    }
    */

    protected fun runToast() {
        _showToast.value = Event(
            stateHolder.filter { it.value == State.SELECTED }.size
        )
    }

    fun clearMemes() {
        stateHolder
            .filter { it.value == State.SELECTED }
            .forEach { stateHolder.remove(it.key) }
        _adapterOption.value = Event(AdapterEvent)
        _itemSelected.value = Event(0)
    }

    fun getStateForMeme(meme: MemeUI.Model): State? {
        return stateHolder[meme.meme]
    }

    fun memeSelected(meme: MemeUI.Model): State? {
        val result = when (val state = stateHolder[meme.meme]) {
            null -> {
                stateHolder[meme.meme] = State.SELECTED
                State.SELECTED
            }
            State.SELECTED -> {
                stateHolder.remove(meme.meme)
                null
            }
            else -> state
        }
        _itemSelected.value = Event(
            stateHolder.filter { it.value == State.SELECTED }.size
        )
        return result
    }

    enum class State { SELECTED, APPROVED, DELETED }

    sealed class ViewHolderResult {
        object NoMemes : ViewHolderResult()
        class MemeResult(val resource: Resource<Flow<PagingData<MemeUI>>>) : ViewHolderResult()
    }

    object AdapterEvent
}