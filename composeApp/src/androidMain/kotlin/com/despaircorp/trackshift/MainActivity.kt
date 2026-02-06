package com.despaircorp.trackshift

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.despaircorp.design_system.theme.TrackShiftTheme
import com.despaircorp.trackshift.screen.TrackShiftApp
import com.despaircorp.trackshift.utils.ActivityProvider
import com.despaircorp.trackshift.utils.OAuthCallbackHolder
import com.despaircorp.trackshift.utils.SupabaseAuthHelper
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        ActivityProvider.currentActivity = WeakReference(this)

        handleIntent(intent)

        setContent {
            TrackShiftTheme {
                TrackShiftApp()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data ?: return
        val url = uri.toString()

        if (uri.scheme == "trackshift" && uri.host == "callback") {
            if (OAuthCallbackHolder.pendingCallback != null) {
                OAuthCallbackHolder.handleCallback(url)
            } else {
                SupabaseAuthHelper.handleDeepLink(url)
            }
        }
    }
}
