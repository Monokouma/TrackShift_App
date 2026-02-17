package com.despaircorp.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class PlatformUtils actual constructor() {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl, emptyMap<Any?, Any?>()) { _ -> }
    }

}