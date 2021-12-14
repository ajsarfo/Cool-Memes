package com.sarftec.coolmemes.view.activity

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sarftec.coolmemes.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val layoutBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun canShowInterstitial(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        statusColor(Color.BLACK)
        setSplashImage()
        lifecycleScope.launchWhenCreated {
            delay(TimeUnit.SECONDS.toMillis(2))
            navigateTo(MainActivity::class.java, finish = true)
        }
    }


    private fun setSplashImage() {
        val imageName = "splash_icon.png"
        assets.open(imageName).use {
            layoutBinding.splashImageView.setImageBitmap(
                BitmapFactory.decodeStream(it)
            )
        }
        // val uri = Uri.parse("file:///android_asset/$imageName")
    }
}