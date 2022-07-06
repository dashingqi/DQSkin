package com.dashingqi.asset.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Activity生命周期监听
 * @author zhangqi
 * @since 2022/6/23
 */
open  class BaseApplicationActivityLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}