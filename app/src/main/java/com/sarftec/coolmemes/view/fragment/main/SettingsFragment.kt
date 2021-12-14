package com.sarftec.coolmemes.view.fragment.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.FragmentSettingsBinding
import com.sarftec.coolmemes.utils.Resource
import com.sarftec.coolmemes.view.handler.NightModeHandler
import com.sarftec.coolmemes.view.listener.SettingsFragmentListener
import com.sarftec.coolmemes.view.manager.BillingManager
import com.sarftec.coolmemes.view.utils.toast
import com.sarftec.coolmemes.view.viewmodel.MainViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var layoutBinding: FragmentSettingsBinding

    private lateinit var listener: SettingsFragmentListener

    private val activityViewModel by activityViewModels<MainViewModel>()

    private val inAppBillingManager by lazy {
        BillingManager(requireActivity() as AppCompatActivity)
    }

    override fun onAttach(context: Context) {
        if (context is SettingsFragmentListener) listener = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )
        lifecycleScope.launchWhenCreated {
            setApproveStatus(
                activityViewModel.isUserRegistered(requireContext())
            )
        }
        setupButtonListeners()
        setupSwitch()
        setupSwitchListeners()
        return layoutBinding.root
    }

    private fun setupSwitch() {
        layoutBinding.offlineMode.isChecked = activityViewModel.isCacheEnabled()
        /*
        layoutBinding.nightMode.isChecked =
            listener.getNightModeHandler().getMode() == NightModeHandler.Mode.NIGHT
         */
    }

    private fun setupSwitchListeners() {
        layoutBinding.nightMode.setOnCheckedChangeListener { _, isChecked ->
            listener.getNightModeHandler().changeMode(
                if (isChecked) NightModeHandler.Mode.NIGHT else NightModeHandler.Mode.DAY
            )
        }
        layoutBinding.offlineMode.setOnCheckedChangeListener { _, isChecked ->
            activityViewModel.enableCache(requireContext(), isChecked)
        }
    }

    private fun setupButtonListeners() {
        layoutBinding.approveRegister.setOnClickListener {
            requireContext().toast("Not available")
           /*
            lifecycleScope.launch {
                if (activityViewModel.isUserRegistered(requireContext())) listener.navigateToApprove()
                else registerUser()
            }
            */
        }
        layoutBinding.uploaded.setOnClickListener {
            listener.navigateToViewUpload()
        }
    }

    private fun setApproveStatus(isApproved: Boolean) {
        layoutBinding.apply {
            approveRegisterText.text = if (isApproved) "Approve" else "Register"
            approveRegisterText.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isApproved) R.color.color_on_primary else R.color.black
                )
            )
            approveRegister.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isApproved) R.color.color_primary else R.color.color_settings_register
                )
            )
        }
    }

    private suspend fun registerUser() {
        val deferred = CompletableDeferred<Resource<Boolean>>()
        inAppBillingManager.launchFlow {
            if (it.isSuccess()) {
                activityViewModel.registerUser(requireContext())
                setApproveStatus(true)
                requireContext().toast("User registered!")
            }
            deferred.complete(Resource.success(it.isSuccess()))
        }
        deferred.await()
    }
}