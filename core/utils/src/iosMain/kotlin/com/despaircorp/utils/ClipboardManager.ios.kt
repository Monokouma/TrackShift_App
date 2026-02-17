package com.despaircorp.utils

import platform.UIKit.UIPasteboard

actual class ClipboardManager {
    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}