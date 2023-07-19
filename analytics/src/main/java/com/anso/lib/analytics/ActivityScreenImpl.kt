package com.anso.lib.analytics

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityScreenImpl : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //不需要实现
    }

    override fun onActivityStarted(activity: Activity) {
        //不需要实现
    }

    override fun onActivityResumed(activity: Activity) {
        AEvent.screen(activity::class.java.name, "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        //不需要实现
    }

    override fun onActivityStopped(activity: Activity) {
        AEvent.screen(activity::class.java.name, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //不需要实现
    }

    override fun onActivityDestroyed(activity: Activity) {
        //不需要实现
    }
}