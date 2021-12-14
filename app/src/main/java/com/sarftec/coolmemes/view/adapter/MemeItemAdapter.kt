package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.MemeItemViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope

class MemeItemAdapter(
    coroutineScope: CoroutineScope,
    viewModel: HomeViewModel,
    onClick: (MemeUI.Model) -> Unit
) : PagingDataAdapter<MemeUI, MemeItemViewHolder>(MemeDiffUtil) {

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()


    private val dependency = MemeItemViewHolder.ViewHolderDependency(
        viewModel,
        coroutineScope,
        taskManager,
        onClick
    )

    override fun onBindViewHolder(holder: MemeItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.viewType ?: -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeItemViewHolder {
        return MemeItemViewHolder.getInstance(parent, dependency)
    }
}