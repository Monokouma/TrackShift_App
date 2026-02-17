package com.despaircorp.utils

import android.content.ClipData
import android.content.Context

actual class ClipboardManager(private val context: Context) {
    actual fun copyToClipboard(text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}