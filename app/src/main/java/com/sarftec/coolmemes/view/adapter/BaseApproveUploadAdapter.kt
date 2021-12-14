package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.BaseApproveUploadViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.BaseApproveUploadViewModel
import kotlinx.coroutines.CoroutineScope

abstract class BaseApproveUploadAdapter<U : BaseApproveUploadViewModel, T : BaseApproveUploadViewHolder<U>>(
    protected val coroutineScope: CoroutineScope,
    protected val viewModel: U
) : PagingDataAdapter<MemeUI, T>(MemeDiffUtil) {

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()

    private val viewHolders = hashSetOf<T>()

    abstract fun createViewHolder(parent: ViewGroup) : T

    override fun onBindViewHolder(holder: T, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return createViewHolder(parent).also { viewHolders.add(it) }
    }

    fun updateViewHolders() {
        viewHolders.forEach { it.rebind() }
    }
}