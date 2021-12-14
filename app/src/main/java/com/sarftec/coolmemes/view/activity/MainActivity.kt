package com.sarftec.coolmemes.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.ActivityMainBinding
import com.sarftec.coolmemes.view.activity.user.ViewUploadActivity
import com.sarftec.coolmemes.view.activity.user.ApproveActivity
import com.sarftec.coolmemes.view.listener.MemeFragmentListener
import com.sarftec.coolmemes.view.handler.FetchPictureHandler
import com.sarftec.coolmemes.view.handler.NightModeHandler
import com.sarftec.coolmemes.view.handler.ReadWriteHandler
import com.sarftec.coolmemes.view.listener.SettingsFragmentListener
import com.sarftec.coolmemes.view.parcel.MemeToDetail
import com.sarftec.coolmemes.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), MemeFragmentListener, SettingsFragmentListener {

    private val layoutBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var readWrite: ReadWriteHandler

    private lateinit var fetchPicture: FetchPictureHandler

    private lateinit var nightHandler: NightModeHandler

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        readWrite = ReadWriteHandler(this)
        fetchPicture = FetchPictureHandler(this)
        nightHandler = NightModeHandler(this)
        layoutBinding.bottomNavigation.setupWithNavController(getNavController())
        lifecycleScope.launchWhenCreated {
            viewModel.loadData(this@MainActivity)
        }
    }

    private fun getNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(
            R.id.nav_container
        ) as NavHostFragment
        return navHost.navController
    }

    override fun getReadWriteHandler(): ReadWriteHandler {
       return readWrite
    }

    override fun getFetchPictureHandler(): FetchPictureHandler {
        return fetchPicture
    }

    override fun navigateToDetail(memeToDetail: MemeToDetail) {
        navigateToWithParcel(DetailActivity::class.java, parcel = memeToDetail)
    }

    override fun getNightModeHandler(): NightModeHandler {
        return nightHandler
    }

    override fun navigateToApprove() {
        navigateTo(ApproveActivity::class.java)
    }

    override fun navigateToViewUpload() {
        navigateTo(ViewUploadActivity::class.java)
    }
}