package com.sarftec.coolmemes.view.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.ActivityDetailBinding
import com.sarftec.coolmemes.view.adapter.MemeDetailAdapter
import com.sarftec.coolmemes.view.dialog.LoadingDialog
import com.sarftec.coolmemes.view.handler.ReadWriteHandler
import com.sarftec.coolmemes.view.handler.ToolingHandler
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.parcel.MemeToDetail
import com.sarftec.coolmemes.view.utils.downloadGlideImage
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivityDetailBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel by viewModels<DetailViewModel>()

    private val detailAdapter by lazy {
        MemeDetailAdapter(lifecycleScope, viewModel)
    }

    private lateinit var readWriteHandler: ReadWriteHandler

    private val toolingHandler by lazy {
        ToolingHandler(this, readWriteHandler)
    }

    private val loadingDialog by lazy {
        LoadingDialog(this, layoutBinding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        readWriteHandler = ReadWriteHandler(this)
        getParcelFromIntent<MemeToDetail>(intent)?.let {
            viewModel.setParcel(it)
        }
        viewModel.loadFlow()
        setupButtonListeners()
        setupViewPager()
        setupLoadingIndicator()
        observeFlow()
    }

    private fun observeFlow() {
        viewModel.memeFlow.observe(this) { resources ->
            if (resources.isLoading()) showLayout(false)
            if (resources.isError()) Log.v("TAG", "${resources.message}")
            if (resources.isSuccess()) resources.data?.let { flow ->
                lifecycleScope.launchWhenCreated {
                    flow.collect {
                        detailAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun runCurrentBitmapCallback(callback: (Bitmap) -> Unit) {
        loadingDialog.show()
        viewModel.getAtPosition(layoutBinding.viewPager.currentItem)?.let { image ->
            lifecycleScope.launch {
                viewModel.getImage(image).let {
                    if (it.isSuccess()) this@DetailActivity.downloadGlideImage(it.data!!)
                        .let { result ->
                            if (result.isSuccess()) {
                                loadingDialog.dismiss()
                                callback(result.data!!)
                            } else toast("Action Failed!")
                        }
                    else {
                        loadingDialog.dismiss()
                        toast("Action Failed!")
                    }
                    //  if (it.isSuccess()) callback(it.data!!)
                }
            }
        }
    }

    private fun setupButtonListeners() {
        layoutBinding.include.download.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.saveImage(it) }
        }
        layoutBinding.include.share.setOnClickListener {
            runCurrentBitmapCallback { toolingHandler.shareImage(it) }
        }
        layoutBinding.include.like.setOnClickListener {
            setFavorite(true)
        }
        layoutBinding.include.dislike.setOnClickListener {
            setFavorite(false)
        }
    }

    private fun updateLikes(memeUI: MemeUI.Model) {
        layoutBinding.likes.text = "+${String.format("%,d", memeUI.meme.likes)}"
    }

    private fun setFavorite(isFavorite: Boolean) {
        viewModel.getMemeAtPosition(layoutBinding.viewPager.currentItem)?.let {
            if (isFavorite) viewModel.saveFavorite(it)
            else viewModel.removeFavorite(it)
            updateLikes(it)
        }
        changeViewsOnFavorite(isFavorite)
    }

    private fun clearButtons() {
        layoutBinding.include.apply {
            dislike.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@DetailActivity,
                    R.color.color_detail_bottom_section
                )
            )
            dislikeIcon.setImageResource(R.drawable.ic_dislike_grey)

            like.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@DetailActivity,
                    R.color.color_detail_bottom_section
                )
            )
            likeIcon.setImageResource(R.drawable.ic_like_grey)
        }
    }

    private fun changeViewsOnFavorite(isFavorite: Boolean) {
        layoutBinding.include.apply {
            dislike.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@DetailActivity,
                    if (!isFavorite) R.color.color_detail_bottom_dislike else R.color.color_detail_bottom_section
                )
            )
            dislikeIcon.setImageResource(
                if (!isFavorite) R.drawable.ic_dislike_white
                else R.drawable.ic_dislike_grey
            )

            like.setCardBackgroundColor(
                ContextCompat.getColor(
                    this@DetailActivity,
                    if (isFavorite) R.color.color_detail_bottom_like else R.color.color_detail_bottom_section
                )
            )
            likeIcon.setImageResource(
                if (isFavorite) R.drawable.ic_like_white
                else R.drawable.ic_like_grey
            )
        }
    }

    private fun setupViewPager() {
        layoutBinding.viewPager.adapter = detailAdapter
        layoutBinding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    clearButtons()
                    viewModel.getAtPosition(position)?.let {
                        layoutBinding.likes.text = "+${String.format("%,d", it.meme.likes)}"
                    }
                }
            }
        )
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.detailLayout.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
    }

    private fun setupLoadingIndicator() {
        detailAdapter.addLoadStateListener {
            showLayout(
                it.refresh != LoadState.Loading
            )
        }
    }
}