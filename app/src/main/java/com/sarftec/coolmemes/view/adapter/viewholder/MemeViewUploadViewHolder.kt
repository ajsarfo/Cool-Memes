package com.sarftec.coolmemes.view.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.databinding.LayoutApproveItemBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.ApproveViewModel
import com.sarftec.coolmemes.view.viewmodel.ViewUploadViewModel
import kotlinx.coroutines.CoroutineScope

class MemeViewUploadViewHolder private constructor(
    layoutBinding: LayoutApproveItemBinding,
    dependency: MemeViewUploadDependency
) : BaseApproveUploadViewHolder<ViewUploadViewModel>(layoutBinding, dependency) {

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: MemeViewUploadDependency
        ): MemeViewUploadViewHolder {
            return MemeViewUploadViewHolder(getLayoutBinding(parent), dependency)
        }
    }

    class MemeViewUploadDependency(
        coroutineScope: CoroutineScope,
        viewModel: ViewUploadViewModel,
        taskManager: TaskManager<MemeUI.Model, Resource<Uri>>
    ) : ViewHolderDependency<ViewUploadViewModel>(coroutineScope, viewModel, taskManager)
}