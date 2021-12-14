package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.MemeDetailViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope

class MemeDetailAdapter(
    coroutineScope: CoroutineScope,
    viewModel: DetailViewModel
) : PagingDataAdapter<MemeUI, MemeDetailViewHolder>(MemeDiffUtil) {

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()

    private val dependency = MemeDetailViewHolder.ViewHolderDependency(
        viewModel,
        coroutineScope,
        taskManager
    )

    override fun onBindViewHolder(holder: MemeDetailViewHolder, position: Int) {
       getItem(position)?.let {
           holder.bind(position, it)
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeDetailViewHolder {
        return MemeDetailViewHolder.getInstance(parent, dependency)
    }
}