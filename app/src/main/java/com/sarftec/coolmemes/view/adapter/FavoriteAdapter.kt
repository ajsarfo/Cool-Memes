package com.sarftec.coolmemes.view.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.viewholder.MemeItemViewHolder
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope

class FavoriteAdapter(
    coroutineScope: CoroutineScope,
    viewModel: FavoriteViewModel,
    onClick: (MemeUI.Model) -> Unit
) : RecyclerView.Adapter<MemeItemViewHolder>() {

    private var items = listOf<MemeUI.Model>()

    private val taskManager = TaskManager<MemeUI.Model, Resource<Uri>>()

    private val dependency = MemeItemViewHolder.ViewHolderDependency(
        viewModel,
        coroutineScope,
        taskManager,
        onClick
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeItemViewHolder {
        return MemeItemViewHolder.getInstance(parent, dependency)
    }

    override fun onBindViewHolder(holder: MemeItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(items: List<MemeUI.Model>) {
        this.items = items
        notifyDataSetChanged()
    }
}