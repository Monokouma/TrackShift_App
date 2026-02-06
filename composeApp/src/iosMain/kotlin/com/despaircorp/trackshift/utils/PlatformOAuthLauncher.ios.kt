package com.despaircorp.trackshift.utils

import platform.AuthenticationServices.ASWebAuthenticationSession
import platform.AuthenticationServices.ASWebAuthenticationPresentationContextProvidingProtocol
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.darwin.NSObject

actual class PlatformOAuthLauncher actual constructor() {

    actual fun launch(url: String, callbackScheme: String, onResult: (String?) -> Unit) {
        val nsUrl = NSURL.URLWithString(url) ?: run {
            onResult(null)
            return
        }

        val session = ASWebAuthenticationSession(
            uRL = nsUrl,
            callbackURLScheme = callbackScheme
        ) { callbackUrl, error ->
            if (error != null) {
                onResult(null)
            } else {
                onResult(callbackUrl?.absoluteString)
            }
        }

        session.presentationContextProvider = PresentationContextProvider()
        session.prefersEphemeralWebBrowserSession = false
        session.start()
    }
}

private class PresentationContextProvider : NSObject(), ASWebAuthenticationPresentationContextProvidingProtocol {
    override fun presentationAnchorForWebAuthenticationSession(
        session: ASWebAuthenticationSession
    ): UIWindow {
        return UIApplication.sharedApplication.keyWindow
            ?: UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow
            ?: UIWindow()
    }
}
