package com.sarftec.coolmemes.view.activity.user

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.databinding.ActivityViewUploadBinding
import com.sarftec.coolmemes.view.adapter.MemeViewUploadAdapter
import com.sarftec.coolmemes.view.adapter.viewholder.MemeViewUploadViewHolder
import com.sarftec.coolmemes.view.viewmodel.ViewUploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewUploadActivity : BaseUserActivity<ViewUploadViewModel, MemeViewUploadViewHolder>() {

    private val layoutBinding by lazy {
        ActivityViewUploadBinding.inflate(
            layoutInflater
        )
    }

    override  val viewModel by viewModels<ViewUploadViewModel>()

    override val approveUploadAdapter by lazy {
        MemeViewUploadAdapter(lifecycleScope, viewModel)
    }

    override fun getCountTextView(): TextView = layoutBinding.itemCount

    override fun getRecyclerView(): RecyclerView = layoutBinding.recyclerView

    override fun getRoot(): ViewGroup = layoutBinding.root


    override fun setupButtonListeners() {
        layoutBinding.clear.setOnClickListener { viewModel.clearMemes() }
        layoutBinding.delete.setOnClickListener {
            lifecycleScope.launch {
                performAction(viewModel.getSelectedMemes()) {
                    viewModel.deleteMeme(it)
                }
            }
        }
    }

    override fun showLayout(isShown: Boolean) {
        layoutBinding.buttonsContainer.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.recyclerContainer.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
    }

    override fun showNoMeme(isShown: Boolean) {
        layoutBinding.buttonsContainer.visibility = if (isShown) View.GONE else View.VISIBLE
        layoutBinding.recyclerContainer.visibility = if (isShown) View.GONE else View.VISIBLE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
        layoutBinding.showNoMeme.visibility = if(isShown) View.VISIBLE else View.GONE
    }
}