package com.sarftec.coolmemes.view.fragment.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarftec.coolmemes.databinding.FragmentHomeBinding
import com.sarftec.coolmemes.view.adapter.FavoriteAdapter
import com.sarftec.coolmemes.view.listener.MemeFragmentListener
import com.sarftec.coolmemes.view.parcel.MemeToDetail
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var layoutBinding: FragmentHomeBinding

    private val viewModel by viewModels<FavoriteViewModel>()

    private lateinit var listener: MemeFragmentListener

    private val favoriteAdapter by lazy {
        FavoriteAdapter(lifecycleScope, viewModel) {
            listener.navigateToDetail(
                MemeToDetail(it.meme.id, true)
            )
        }
    }

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
        viewModel.loadMemes()
        viewModel.favoriteMemes.observe(viewLifecycleOwner) {
            if(it.isLoading()) showLayout(false)
            if(it.isSuccess()) {
                favoriteAdapter.submitData(it.data!!)
                showLayout(true)
            }
            if(it.isError()) {
                Log.v("TAG", "${it.message}")
                requireActivity().toast("Error Occurred!")
                showLayout(true)
            }
        }
        return layoutBinding.root
    }

    private fun showLayout(isShown: Boolean) {
        layoutBinding.recyclerView.visibility = if (isShown) View.VISIBLE else View.GONE
        layoutBinding.circularProgress.visibility = if (isShown) View.GONE else View.VISIBLE
        layoutBinding.networkErrorLayout.parent.visibility = View.GONE
    }
}