package com.sarftec.coolmemes.view.viewmodel

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.domain.usecase.upload.DeleteMeme
import com.sarftec.coolmemes.domain.usecase.upload.GetMemes
import com.sarftec.coolmemes.domain.usecase.upload.HasMeme
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewUploadViewModel @Inject constructor(
    mapper: MemeUIMapper,
    getImage: GetImage,
    private val deleteMeme: DeleteMeme,
    private val getMemes: GetMemes,
    private val hasMeme: HasMeme
): BaseApproveUploadViewModel(getImage, mapper) {

    override suspend fun getFlow(): Resource<Flow<PagingData<Meme>>> {
        return getMemes.execute(GetMemes.GetParam).result
    }

    override suspend fun hasMeme(): Resource<Boolean> {
        return hasMeme.execute(HasMeme.HasParam).result
    }

    suspend fun deleteMeme(memeUI: MemeUI.Model) : Resource<Unit> {
        return deleteMeme.execute(DeleteMeme.DeleteParam(memeUI.meme)).result.also {
            if(it.isSuccess()) runDeleteMeme(memeUI)
        }
    }
}