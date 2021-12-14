package com.sarftec.coolmemes.view.viewmodel

import androidx.paging.PagingData
import com.sarftec.coolmemes.domain.model.Meme
import com.sarftec.coolmemes.domain.usecase.approve.AlterMeme
import com.sarftec.coolmemes.domain.usecase.approve.GetMemes
import com.sarftec.coolmemes.domain.usecase.approve.HasMeme
import com.sarftec.coolmemes.domain.usecase.image.GetImage
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.mapper.MemeUIMapper
import com.sarftec.coolmemes.view.model.MemeUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ApproveViewModel @Inject constructor(
    getImage: GetImage,
    mapper: MemeUIMapper,
    private val alterMeme: AlterMeme,
    private val getMemes: GetMemes,
    private val hasMeme: HasMeme
) : BaseApproveUploadViewModel(getImage, mapper) {

    override suspend fun getFlow(): Resource<Flow<PagingData<Meme>>> {
        return getMemes.execute(GetMemes.GetParam).result
    }

    override suspend fun hasMeme(): Resource<Boolean> {
        return hasMeme.execute(HasMeme.HasParam).result
    }

    suspend fun approveMeme(memeUI: MemeUI.Model): Resource<Unit> {
        return alterMeme.execute(
            AlterMeme.AlterParam(
                AlterMeme.Option.APPROVE,
                memeUI.meme
            )
        ).result.also {
            if (it.isSuccess()) runApproveMeme(memeUI)
        }
    }

    suspend fun deleteMeme(memeUI: MemeUI.Model): Resource<Unit> {
        return alterMeme.execute(
            AlterMeme.AlterParam(
                AlterMeme.Option.DELETE,
                memeUI.meme
            )
        ).result.also {
            if (it.isSuccess()) runDeleteMeme(memeUI)
        }
    }
}