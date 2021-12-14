package com.sarftec.coolmemes.view.activity.user

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.activity.BaseActivity
import com.sarftec.coolmemes.view.adapter.BaseApproveUploadAdapter
import com.sarftec.coolmemes.view.adapter.viewholder.BaseApproveUploadViewHolder
import com.sarftec.coolmemes.view.advertisement.RewardVideoManager
import com.sarftec.coolmemes.view.dialog.ProgressDialog
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.BaseApproveUploadViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseUserActivity<U: BaseApproveUploadViewModel, T : BaseApproveUploadViewHolder<U>>
    : BaseActivity() {

    protected var uploadJob: Job? = null

    protected val uploadDialog by lazy {
        ProgressDialog(
            getRoot(),
            this,
            onCancel = {
                uploadJob = null
                toast("Action failed")
            },
            onFinished = {
                uploadJob = null
                toast("Action success")
            }
        )
    }

    protected abstract val viewModel : U

    protected abstract val approveUploadAdapter : BaseApproveUploadAdapter<U, T>

    protected abstract fun getRecyclerView() : RecyclerView

    protected abstract fun getCountTextView() : TextView

    protected abstract fun getRoot() : ViewGroup

    protected abstract fun setupButtonListeners()

    protected abstract fun showLayout(isShown: Boolean)

    protected abstract fun showNoMeme(isShown: Boolean)

    override fun canShowInterstitial(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getRoot())
        setupAdapter()
        setupButtonListeners()
        viewModel.loadFlow()
        viewModel.memeFlow.observe(this) {
            lifecycleScope.launchWhenCreated {
                if(it is BaseApproveUploadViewModel.ViewHolderResult.MemeResult) {
                    showNoMeme(false)
                    observeFlow(it.resource)
                }
                else showNoMeme(true)
            }
        }
        viewModel.adapterOption.observe(this) { event ->
            event.getIfNotHandled()?.let {
                approveUploadAdapter.updateViewHolders()
            }
        }
        viewModel.itemSelected.observe(this) { event ->
            getCountTextView().text = if (event.peek() > 0) "${event.peek()} items selected"
            else ""
        }
        viewModel.showToast.observe(this) { event ->
            event.getIfNotHandled()?.let {
                if(it == 0) toast("Select at least ONE meme!")
            }
        }
    }

    protected suspend fun performAction(items: List<MemeUI.Model>,action: suspend (MemeUI.Model) -> Resource<Unit>) {
        if(items.isEmpty()) return
        uploadDialog.show()
        val progressDiff = 1 / items.size.toFloat()
        uploadJob = lifecycleScope.launch {
            items.forEachIndexed { index, item ->
                val result = action(item)
                if (result.isError()) Log.v("TAG", "Error => ${result.message}")
                uploadDialog.setProgress((progressDiff * (index + 1) * 100).toInt())
            }
        }
    }


    private suspend fun observeFlow(resources: Resource<Flow<PagingData<MemeUI>>>) {
        showLayout(!resources.isLoading())
        if (resources.isSuccess()) resources.data?.collect {
            approveUploadAdapter.submitData(it)
        }
        if (resources.isError()) Log.v("TAG", "Error => ${resources.message}")
    }


    private fun setupAdapter() {
        getRecyclerView().apply {
            adapter = approveUploadAdapter
            layoutManager = LinearLayoutManager(
                this@BaseUserActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }
}