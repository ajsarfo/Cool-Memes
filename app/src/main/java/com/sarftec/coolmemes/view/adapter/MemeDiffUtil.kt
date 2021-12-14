package com.sarftec.coolmemes.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sarftec.coolmemes.view.model.MemeUI

object MemeDiffUtil : DiffUtil.ItemCallback<MemeUI>() {

    override fun areItemsTheSame(oldItem: MemeUI, newItem: MemeUI): Boolean {
        return if(oldItem is MemeUI.Model && newItem is MemeUI.Model) {
            oldItem.meme.id == newItem.meme.id
        }
        else oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: MemeUI, newItem: MemeUI): Boolean {
        return if(oldItem is MemeUI.Model && newItem is MemeUI.Model) {
            oldItem.meme == newItem.meme
        }
        else oldItem.viewType == newItem.viewType
    }
}