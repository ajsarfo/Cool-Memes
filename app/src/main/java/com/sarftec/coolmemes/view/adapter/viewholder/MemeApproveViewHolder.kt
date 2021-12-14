package com.sarftec.coolmemes.view.adapter.viewholder

import android.net.Uri
import android.view.ViewGroup
import com.sarftec.coolmemes.databinding.LayoutApproveItemBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.ApproveViewModel
import kotlinx.coroutines.CoroutineScope

class MemeApproveViewHolder(
    layoutBinding: LayoutApproveItemBinding,
    dependency: MemeApproveDependency
) : BaseApproveUploadViewHolder<ApproveViewModel>(layoutBinding, dependency) {

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: MemeApproveDependency
        ): MemeApproveViewHolder {
            return MemeApproveViewHolder(getLayoutBinding(parent), dependency)
        }
    }

    class MemeApproveDependency(
        coroutineScope: CoroutineScope,
        viewModel: ApproveViewModel,
        taskManager: TaskManager<MemeUI.Model, Resource<Uri>>
    ) : ViewHolderDependency<ApproveViewModel>(coroutineScope, viewModel, taskManager)
}