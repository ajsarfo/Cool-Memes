package com.sarftec.coolmemes.view.activity.user

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.ActivityApproveBinding
import com.sarftec.coolmemes.view.adapter.MemeApproveAdapter
import com.sarftec.coolmemes.view.adapter.viewholder.MemeApproveViewHolder
import com.sarftec.coolmemes.view.advertisement.BannerManager
import com.sarftec.coolmemes.view.viewmodel.ApproveViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApproveActivity : BaseUserActivity<ApproveViewModel, MemeApproveViewHolder>() {

    private val layoutBinding by lazy {
        ActivityApproveBinding.inflate(
            layoutInflater
        )
    }

    override  val viewModel by viewModels<ApproveViewModel>()

    override val approveUploadAdapter by lazy {
        MemeApproveAdapter(lifecycleScope, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*************** Admob Configuration ********************/
        BannerManager(this, adRequestBuilder).attachBannerAd(
            getString(R.string.admob_banner_review),
            layoutBinding.mainBanner
        )
        /**********************************************************/
    }

    override fun getCountTextView(): TextView = layoutBinding.itemCount

    override fun getRecyclerView(): RecyclerView = layoutBinding.recyclerView

    override fun getRoot(): ViewGroup = layoutBinding.root

    override fun setupButtonListeners() {
        layoutBinding.clear.setOnClickListener { viewModel.clearMemes() }
        layoutBinding.delete.setOnClickListener {
            rewardVideoManager.showRewardVideo {
                lifecycleScope.launch {
                    performAction(viewModel.getSelectedMemes()) {
                        viewModel.deleteMeme(it)
                    }
                }
            }
        }
        layoutBinding.approve.setOnClickListener {
            rewardVideoManager.showRewardVideo {
                lifecycleScope.launch {
                    performAction(viewModel.getSelectedMemes()) {
                        viewModel.approveMeme(it)
                    }
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