package com.despaircorp.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class SubscriptionsManager {
    actual fun openSubscriptionManagement() {
        val url = NSURL(string = "https://apps.apple.com/account/subscriptions")
        UIApplication.sharedApplication.openURL(
            url,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}