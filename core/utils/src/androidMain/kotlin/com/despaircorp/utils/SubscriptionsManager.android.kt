package com.despaircorp.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

actual class SubscriptionsManager(private val context: Context) {

    actual fun openSubscriptionManagement() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "https://play.google.com/store/account/subscriptions".toUri()
            setPackage("com.android.vending")
        }
        context.startActivity(intent)
    }
}