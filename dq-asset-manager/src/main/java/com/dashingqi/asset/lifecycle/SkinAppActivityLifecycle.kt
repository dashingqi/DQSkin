package com.dashingqi.asset.lifecycle

import android.app.Activity
import android.os.Bundle

/**
 * 皮肤-Activity生命周期监听
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinAppActivityLifecycle : ApplicationActivityLifecycle() {
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        super.onActivityCreated(activity, bundle)
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
    }
}