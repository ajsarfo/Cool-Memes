package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.MemeViewUploadViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.ViewUploadViewModel
import kotlinx.coroutines.CoroutineScope

class MemeViewUploadAdapter(
    coroutineScope: CoroutineScope,
    viewModel: ViewUploadViewModel
) : BaseApproveUploadAdapter<ViewUploadViewModel, MemeViewUploadViewHolder>(
    coroutineScope,
    viewModel
) {

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()

    private val dependency = MemeViewUploadViewHolder.MemeViewUploadDependency(
        coroutineScope,
        viewModel,
        taskManager,
    )

    override fun createViewHolder(parent: ViewGroup): MemeViewUploadViewHolder {
        return MemeViewUploadViewHolder.getInstance(parent, dependency)
    }
}