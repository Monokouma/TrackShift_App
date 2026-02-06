package com.despaircorp.trackshift.utils

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

actual class PlatformOAuthLauncher actual constructor() {

    actual fun launch(url: String, callbackScheme: String, onResult: (String?) -> Unit) {
        val activity = ActivityProvider.currentActivity?.get()
        if (activity == null) {
            onResult(null)
            return
        }

        OAuthCallbackHolder.pendingCallback = onResult

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        try {
            customTabsIntent.launchUrl(activity, url.toUri())
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            activity.startActivity(intent)
        }
    }
}
