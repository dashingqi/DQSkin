package com.dashingqi.hook.instru

import android.app.Instrumentation
import android.content.Context
import com.dashingqi.hook.instru.DQInstrumentationProxy

/**
 * @desc : Hook Instrumentation
 * @author : zhangqi
 * @time : 2023/6/11 14:56
 */


/**
 * Hook Activity Thread 中 Instrumentation
 */
fun hookInstrumentation(context: Context) {
    runCatching {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread")
        activityThreadField.isAccessible = true
        val activityThreadInstance = activityThreadField.get(null)

        val instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
        instrumentationField.isAccessible = true
        val originInstrumentation = instrumentationField.get(activityThreadInstance) as Instrumentation
        val proxyInstrumentation = DQInstrumentationProxy(originInstrumentation, context.packageManager)
        instrumentationField.set(activityThreadInstance, proxyInstrumentation)
    }.onFailure {
        it.printStackTrace()
    }
}