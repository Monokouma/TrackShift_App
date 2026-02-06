package com.despaircorp.trackshift.utils

import android.app.Activity
import java.lang.ref.WeakReference


object ActivityProvider {
    var currentActivity: WeakReference<Activity>? = null
}
