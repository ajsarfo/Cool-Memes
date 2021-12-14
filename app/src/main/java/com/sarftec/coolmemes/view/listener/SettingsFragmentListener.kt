package com.sarftec.coolmemes.view.listener

import com.sarftec.coolmemes.view.handler.NightModeHandler

interface SettingsFragmentListener {
    fun getNightModeHandler() : NightModeHandler
    fun navigateToApprove()
    fun navigateToViewUpload()
}