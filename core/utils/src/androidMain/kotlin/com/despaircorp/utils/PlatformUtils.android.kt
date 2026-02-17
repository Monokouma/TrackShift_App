package com.despaircorp.utils

import android.content.Intent
import androidx.core.net.toUri
import okhttp3.internal.platform.PlatformRegistry.applicationContext

actual class PlatformUtils actual constructor() {
    actual fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext?.startActivity(intent) ?: return
    }

}