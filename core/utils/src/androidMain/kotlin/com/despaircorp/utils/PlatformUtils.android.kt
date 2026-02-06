package com.despaircorp.utils

import android.content.Intent
import android.net.Uri
import okhttp3.internal.platform.PlatformRegistry.applicationContext
import androidx.core.net.toUri

actual class PlatformUtils actual constructor() {
    actual fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext?.startActivity(intent) ?: return
    }
}