package com.sarftec.coolmemes.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sarftec.coolmemes.databinding.LayoutUploadItemBinding
import com.sarftec.coolmemes.view.viewmodel.UploadViewModel
import kotlinx.coroutines.CoroutineScope

class MemeUploadViewHolder private constructor(
    private val layoutBinding: LayoutUploadItemBinding,
    private val dependency: ViewHolderDependency
) : RecyclerView.ViewHolder(layoutBinding.root) {

    fun bind(overlay: UploadViewModel.UploadInfo) {
        layoutBinding.checkOverlay.visibility = if(overlay.isUploaded) View.VISIBLE else View.GONE
        Glide.with(itemView)
            .load(overlay.imageInfo.uri)
            .into(layoutBinding.image)
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            dependency: ViewHolderDependency
        ) : MemeUploadViewHolder {

            val binding = LayoutUploadItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MemeUploadViewHolder(binding, dependency)
        }
    }

    class ViewHolderDependency(
        private val coroutineScope: CoroutineScope,
        private val viewModel: UploadViewModel
    )
}