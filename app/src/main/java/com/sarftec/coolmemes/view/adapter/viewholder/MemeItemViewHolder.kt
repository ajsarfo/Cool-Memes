package com.sarftec.coolmemes.view.adapter.viewholder

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.LayoutMemeItemBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.task.Task
import com.sarftec.coolmemes.view.task.TaskManager
import com.sarftec.coolmemes.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.*
import java.util.concurrent.TimeUnit

class MemeItemViewHolder(
    private val layoutBinding: LayoutMemeItemBinding,
    private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root) {

    private val uuid = UUID.randomUUID().toString()

    private fun clearLayout(model: MemeUI.Model) {
        layoutBinding.memeCard.setOnClickListener { dependency.onClick(model) }
        layoutBinding.voteCount.text = "+${String.format("%,d", model.meme.likes)}"
        layoutBinding.image.apply {
            //  scaleType = ImageView.ScaleType.FIT_CENTER
            setImageResource(R.drawable.ic_image_placeholder)
        }
    }

    private fun setLayout(model: MemeUI.Model, resource: Resource<Uri>) {
        val strokeColor = if ((Date().time - model.meme.id) > TimeUnit.DAYS.toMillis(4))
            R.color.color_settings_item_divider
        else R.color.color_primary

        layoutBinding.memeCard.strokeColor = ContextCompat.getColor(itemView.context, strokeColor)
        if (resource.isSuccess()) {
            /*
             layoutBinding.image.apply {
                 layoutBinding.image.setImageBitmap(null)
                 scaleType = ImageView.ScaleType.FIT_XY
             }
             */
            Glide.with(itemView)
                .load(resource.data!!)
                .placeholder(R.drawable.ic_image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(layoutBinding.image)
        }
        if (resource.isError()) Log.v("TAG", "${resource.message}")
        dependency.taskManager.removeTask(uuid)
    }

    fun bind(memeUI: MemeUI) {
        if (memeUI !is MemeUI.Model) return
        clearLayout(memeUI)
        val task = Task.createTask<MemeUI.Model, Resource<Uri>>(
            dependency.coroutineScope,
            memeUI
        )
        task.addExecution { input -> dependency.viewModel.getImage(input) }
        task.addCallback { setLayout(memeUI, it) }
        dependency.taskManager.addTask(uuid, task.build())
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ): MemeItemViewHolder {
            val binding = LayoutMemeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MemeItemViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        val viewModel: BaseViewModel,
        val coroutineScope: CoroutineScope,
        val taskManager: TaskManager<MemeUI.Model, Resource<Uri>>,
        val onClick: (MemeUI.Model) -> Unit
    )
}