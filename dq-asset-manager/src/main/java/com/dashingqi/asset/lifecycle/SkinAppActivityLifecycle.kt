package com.dashingqi.asset.lifecycle

import android.app.Activity
import android.os.Bundle

/**
 * 皮肤-Activity生命周期监听
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinAppActivityLifecycle : ApplicationActivityLifecycle() {
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        super.onActivityCreated(p0, p1)
    }

    override fun onActivityDestroyed(p0: Activity) {
        super.onActivityDestroyed(p0)
    }
}