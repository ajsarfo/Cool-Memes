package com.sarftec.coolmemes.view.fragment.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.sarftec.coolmemes.databinding.FragmentHomeBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.adapter.MemeItemAdapter
import com.sarftec.coolmemes.view.listener.MemeFragmentListener
import com.sarftec.coolmemes.view.model.MemeUI
import com.sarftec.coolmemes.view.parcel.MemeToDetail
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var layoutBinding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var listener: MemeFragmentListener

    private val memeAdapter by lazy {
        MemeItemAdapter(lifecycleScope, viewModel) {
            listener.navigateToDetail(
                MemeToDetail(it.meme.id, false)
            )
        }
    }

    private var showNetworkToast = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as MemeFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel.loadMemeFlow()
        setupButtonListeners()
        setupAdapter()
        setupLoadingState()
        viewModel.memeFlow.observe(viewLifecycleOwner) {
            lifecycleScope.launchWhenCreated {
                observeFlow(it)
            }
        }

        return layoutBinding.root
    }

    private suspend fun observeFlow(resources: Resource<Flow<PagingData<MemeUI>>>) {
        if (resources.isSuccess()) resources.data?.collect {
            memeAdapter.submitData(it)
        }
        if (resources.isError()) Log.v("TAG", "Error => ${resources.message}")
    }

    private fun setupAdapter() {
        layoutBinding.recyclerView.apply {
            adapter = memeAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                requireContext(),
                3
            )
        }
    }

    private fun setupButtonListeners() {
        layoutBinding.networkErrorLayout.reload.setOnClickListener {
            showNetworkToast = true
            viewModel.loadMemeFlow()
        }
    }

    private fun setupLoadingState() {
        memeAdapter.addLoadStateListener {
            lifecycleScope.launch {
                if (viewModel.hasNetwork(requireContext())) showLayout(it.refresh != LoadState.Loading)
                else showNetworkError()
            }
        }
    }

    private fun showNetworkError() {
        layoutBinding.apply {
            recyclerView.visibility = View.GONE
            circularProgress.visibility = View.GONE
            networkErrorLayout.parent.visibility = View.VISIBLE
        }
        if (showNetworkToast) {
            showNetworkToast = false
            requireContext().toast(
                "Error retrieving data, please check your internet connection.",
                Toast.LENGTH_SHORT
            )
        }
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.recyclerView.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
        layoutBinding.networkErrorLayout.parent.visibility = View.GONE
    }
}