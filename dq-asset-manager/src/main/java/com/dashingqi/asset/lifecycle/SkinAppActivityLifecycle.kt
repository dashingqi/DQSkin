package com.dashingqi.asset.lifecycle

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.dashingqi.asset.config.isDebug
import com.dashingqi.asset.constant.SKIN_LOG_TAG
import com.dashingqi.asset.factory.SkinFactory
import com.dashingqi.asset.utils.resetFactorySetState

/**
 * 皮肤-Activity生命周期监听
 * @author zhangqi
 * @since 2022/6/23
 */
class SkinAppActivityLifecycle : ApplicationActivityLifecycle() {
    /** 存储Activity 与SkinFactory 对应的关系*/
    private val mLayoutInflaterFactories = mutableMapOf<Activity, SkinFactory>()
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        super.onActivityCreated(activity, bundle)
        // 获取到Activity的布局加载器
        val layoutInflater = activity.layoutInflater
        val resetFactorySetState = resetFactorySetState(layoutInflater)
        if (isDebug) {
            Log.d(SKIN_LOG_TAG, "resetFactorySetState is $resetFactorySetState")
        }
        if (resetFactorySetState) {
            // 皮肤自定义的布局加载
            val skinFactory = SkinFactory(activity)
            mLayoutInflaterFactories[activity] = skinFactory
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        mLayoutInflaterFactories.remove(activity)
    }
}