package com.despaircorp.trackshift.utils


object OAuthCallbackHolder {
    var pendingCallback: ((String?) -> Unit)? = null

    fun handleCallback(url: String) {
        pendingCallback?.invoke(url)
        pendingCallback = null
    }

    fun cancel() {
        pendingCallback?.invoke(null)
        pendingCallback = null
    }
}
