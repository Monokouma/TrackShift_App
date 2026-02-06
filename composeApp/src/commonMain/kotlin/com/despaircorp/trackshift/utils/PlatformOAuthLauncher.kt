package com.despaircorp.trackshift.utils


expect class PlatformOAuthLauncher() {

    fun launch(url: String, callbackScheme: String, onResult: (String?) -> Unit)
}
