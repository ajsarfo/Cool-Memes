package com.sarftec.coolmemes.view.adapter.viewholder

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.LayoutApproveItemBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.Task
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.BaseApproveUploadViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*

abstract class BaseApproveUploadViewHolder<T : BaseApproveUploadViewModel>(
    private val layoutBinding: LayoutApproveItemBinding,
    private val dependency: ViewHolderDependency<T>
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val uuid = UUID.randomUUID().toString()

    private var meme: MemeUI.Model? = null

    private fun clearLayout(model: MemeUI.Model) {
        rebind()
        layoutBinding.wallpaperCard.setOnClickListener {
            val state = dependency.viewModel.memeSelected(model)
            bindState(state)
        }
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

    fun bind(meme: MemeUI) {
        if (meme !is MemeUI.Model) return
        this.meme = meme
        clearLayout(meme)
        val task = Task.createTask<MemeUI.Model, Resource<Uri>>(
            dependency.coroutineScope,
            meme
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(it) }
        dependency.taskManager.addTask(uuid, task.build())
    }

    fun rebind() = meme?.let {
        val state = dependency.viewModel.getStateForMeme(it)
        bindState(state)
    }

    private fun bindState(state: BaseApproveUploadViewModel.State?) {
        when (state) {
            BaseApproveUploadViewModel.State.SELECTED -> layoutBinding.apply {
                checkOverlay.visibility = View.VISIBLE
                selectionIndicator.visibility = View.VISIBLE
                status.visibility = View.GONE
            }

            BaseApproveUploadViewModel.State.APPROVED -> layoutBinding.apply {
                checkOverlay.visibility = View.VISIBLE
                status.visibility = View.VISIBLE
                selectionIndicator.visibility = View.GONE
                status.setImageResource(
                    R.drawable.ic_check
                )
            }

            BaseApproveUploadViewModel.State.DELETED -> layoutBinding.apply {
                checkOverlay.visibility = View.VISIBLE
                status.visibility = View.VISIBLE
                selectionIndicator.visibility = View.GONE
                status.setImageResource(
                    R.drawable.ic_delete
                )
            }

            null -> layoutBinding.apply {
                checkOverlay.visibility = View.GONE
                status.visibility = View.GONE
                selectionIndicator.visibility = View.GONE
            }
        }
    }

    companion object {
        fun getLayoutBinding(parent: ViewGroup) : LayoutApproveItemBinding {
            return LayoutApproveItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
    }

    abstract class ViewHolderDependency<T : BaseApproveUploadViewModel> (
        val coroutineScope: CoroutineScope,
        val viewModel: T,
        val taskManager: TaskManager<MemeUI.Model, Resource<Uri>>
    )
}