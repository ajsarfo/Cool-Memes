package com.sarftec.coolmemes.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.view.adapter.viewholder.MemeUploadViewHolder
import com.sarftec.coolmemes.view.viewmodel.UploadViewModel
import kotlinx.coroutines.CoroutineScope

class MemeUploadAdapter(
    coroutineScope: CoroutineScope,
    viewModel: UploadViewModel
) : RecyclerView.Adapter<MemeUploadViewHolder>() {

    private var items: List<UploadViewModel.UploadInfo> = emptyList()

    private val dependency = MemeUploadViewHolder.ViewHolderDependency(
        coroutineScope,
        viewModel
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeUploadViewHolder {
        return MemeUploadViewHolder.getInstance(
            parent,
            dependency
        )
    }

    override fun onBindViewHolder(holder: MemeUploadViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(items: List<UploadViewModel.UploadInfo>) {
        this.items = items
        notifyDataSetChanged()
    }
}