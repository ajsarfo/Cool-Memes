package com.sarftec.coolmemes.view.fragment.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarftec.coolmemes.databinding.FragmentUploadBinding
import com.sarftec.coolmemes.view.listener.MemeFragmentListener
import com.sarftec.coolmemes.view.adapter.MemeUploadAdapter
import com.sarftec.coolmemes.view.dialog.ProgressDialog
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.UploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UploadFragment : Fragment() {

    private lateinit var layoutBinding: FragmentUploadBinding

    private val viewModel by viewModels<UploadViewModel>()

    private lateinit var listener: MemeFragmentListener

    private var uploadJob: Job? = null

    private val uploadDialog by lazy {
        ProgressDialog(
            layoutBinding.root,
            requireActivity(),
            onCancel = {
                uploadJob = null
                requireContext().toast("Upload failed")
            },
            onFinished = {
                uploadJob = null
                requireContext().toast("Upload success")
            }
        )
    }

    private val uploadAdapter by lazy {
        MemeUploadAdapter(lifecycleScope, viewModel)
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
        layoutBinding = FragmentUploadBinding.inflate(
            layoutInflater,
            container,
            false
        )
        setupAdapter()
        setupButtons()
        observeLiveData()
        return layoutBinding.root
    }

    private fun observeLiveData() {
        viewModel.uploadMemes.observe(viewLifecycleOwner) {
            uploadAdapter.submitData(it)
            setButtonContainerVisibility(it.isNotEmpty())
        }
        viewModel.updateAdapterPosition.observe(viewLifecycleOwner) { event ->
            event.getIfNotHandled()?.let {
                uploadAdapter.notifyItemChanged(it)
            }
        }
    }

    private fun getWallpapersFromDevice() {
        listener.getFetchPictureHandler().getImagesFromDevice {
            viewModel.setUploadMemes(it)
        }
    }

    private fun setButtonContainerVisibility(isVisible: Boolean) {
        layoutBinding.buttonsContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
        layoutBinding.loadWallpapers.visibility = if (isVisible) View.GONE else View.VISIBLE
        layoutBinding.uploadText.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    private fun uploadWallpapers(items: List<UploadViewModel.UploadInfo>) {
        if(items.isEmpty()) return
        uploadDialog.show()
        val progressDiff = 1 / items.size.toFloat()
        uploadJob = lifecycleScope.launch {
            items.forEachIndexed { index, overlay ->
                val result = viewModel.uploadMeme(overlay)
                if (result.isError()) Log.v("TAG", "Error => ${result.message}")
                uploadDialog.setProgress((progressDiff * (index + 1) * 100).toInt())
            }
        }
    }

    private fun setupButtons() {
        layoutBinding.loadWallpapers.setOnClickListener {
            getWallpapersFromDevice()
        }
        layoutBinding.upload.setOnClickListener {
            listener.getReadWriteHandler().requestReadWrite {
                uploadWallpapers(viewModel.getUploadMemes())
            }
        }
        layoutBinding.clear.setOnClickListener {
            viewModel.clearUploadMemes()
        }
    }

    private fun setupAdapter() {
        layoutBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = uploadAdapter
        }
    }
}