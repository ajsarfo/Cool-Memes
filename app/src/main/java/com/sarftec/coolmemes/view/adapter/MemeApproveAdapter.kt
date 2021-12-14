package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.MemeApproveViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.ApproveViewModel
import kotlinx.coroutines.CoroutineScope

class MemeApproveAdapter(
    coroutineScope: CoroutineScope,
    viewModel: ApproveViewModel
) : BaseApproveUploadAdapter<ApproveViewModel, MemeApproveViewHolder>(
    coroutineScope,
    viewModel
) {

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()

    private val dependency = MemeApproveViewHolder.MemeApproveDependency(
        coroutineScope,
        viewModel,
        taskManager,
    )

    override fun createViewHolder(parent: ViewGroup): MemeApproveViewHolder {
        return MemeApproveViewHolder.getInstance(parent, dependency)
    }
}