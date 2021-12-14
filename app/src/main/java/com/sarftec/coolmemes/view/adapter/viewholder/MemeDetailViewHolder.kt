package com.sarftec.coolmemes.view.adapter.viewholder

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sarftec.coolmemes.databinding.LayoutDetailItemBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.Task
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

class MemeDetailViewHolder private constructor(
    private val layoutBinding: LayoutDetailItemBinding,
    private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val uuid = UUID.randomUUID().toString()

    private fun clearLayout(model: MemeUI.Model) {
        layoutBinding.image.setImageBitmap(null)
    }

    private fun setLayout(resource: Resource<Uri>) {
        if (resource.isSuccess()) {
            layoutBinding.image.apply {
                layoutBinding.image.setImageBitmap(null)
                scaleType = ImageView.ScaleType.FIT_XY
            }
            Glide.with(itemView)
                .load(resource.data!!)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(layoutBinding.image)
        }
        if (resource.isError()) Log.v("TAG", "${resource.message}")
        dependency.taskManager.removeTask(uuid)
    }

    fun bind(position: Int, memeUI: MemeUI) {
        if(memeUI !is MemeUI.Model) return
        dependency.viewModel.setAtPosition(position, memeUI)
        clearLayout(memeUI)
        val task = Task.createTask<MemeUI.Model, Resource<Uri>>(
            dependency.coroutineScope,
            memeUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        dependency.taskManager.addTask(uuid, task.build())
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): MemeDetailViewHolder {
            val binding = LayoutDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MemeDetailViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        val viewModel: DetailViewModel,
        val coroutineScope: CoroutineScope,
        val taskManager: TaskManager<MemeUI.Model, Resource<Uri>>
    )
}