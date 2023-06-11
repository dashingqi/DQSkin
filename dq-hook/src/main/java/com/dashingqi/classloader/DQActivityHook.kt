package com.dashingqi.classloader

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import java.lang.reflect.Proxy

/**
 * Activity的Hook
 * @author zhangqi61A
 * @since 2023/6/6
 */

internal const val TARGET_INTENT: String = "targetIntent"

fun hookAMS() {
    runCatching {
        val activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager")
        val singletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton")
        singletonField.isAccessible = true
        val singleton = singletonField.get(null)
        val iActivityTaskManagerClass = Class.forName("android.app.IActivityTaskManager")

        val singletonClass = Class.forName("android.util.Singleton")
        val mInstanceField = singletonClass.getDeclaredField("mInstance")
        mInstanceField.isAccessible = true
//        val mInstance = mInstanceField.get(singleton) ?: return
        val getMethod = singletonClass.getDeclaredMethod("get")
        val mInstance = getMethod.invoke(singleton) ?: return
        val classLoader = Thread.currentThread().contextClassLoader

        // 动态代理
        // 代理的接口
        val proxyInstance = Proxy.newProxyInstance(
            classLoader, arrayOf<Class<*>>(iActivityTaskManagerClass), DQHookInvocationHandler(mInstance)
        )
        mInstanceField.set(singleton, proxyInstance)
    }.onFailure {
        it.printStackTrace()
    }
}

fun hookInstrumentation(activity: Activity) {
    runCatching {
        val activityClass = Activity::class.java
        val instrumentationField = activityClass.getDeclaredField("mInstrumentation")
        instrumentationField.isAccessible = true
        val instrumentationInstance = instrumentationField.get(activity) as Instrumentation
        instrumentationField.set(activity, DQInstrumentation(instrumentationInstance))

    }.onFailure {
        it.printStackTrace()
    }
}

class DQInstrumentation(private val originInstrumentation: Instrumentation) : Instrumentation() {

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?, intent: Intent?,
        requestCode: Int?, options: Bundle?
    ): ActivityResult? {
        return kotlin.runCatching {
            Log.d("MainActivity", "hook instrumentation perform before")
            val execStartActivityMethod = Instrumentation::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                Activity::class.java,
                Activity::class.java,
                Intent::class.java,
                Int::class.java,
                Bundle::class.java
            )
            execStartActivityMethod.isAccessible = true
            val pluginActivityResult = execStartActivityMethod.invoke(
                originInstrumentation, who, contextThread, token, target, intent,
                requestCode, options
            ) as? ActivityResult
            Log.d("MainActivity", "hook instrumentation perform after")
            return pluginActivityResult
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

    }

}

fun hookLaunchActivity() {
    runCatching {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread")
        activityThreadField.isAccessible = true
        val activityThreadInstance = activityThreadField.get(null)

        val mHField = activityThreadClass.getDeclaredField("mH")
        mHField.isAccessible = true
        val mHInstance = mHField.get(activityThreadInstance)

        val mCallbackField = Handler::class.java.getDeclaredField("mCallback")
        mCallbackField.isAccessible = true
        mCallbackField.set(mHInstance, object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                if (msg.obj == 100) {
                    val obj = msg.obj
                    kotlin.runCatching {
                        val intentField = obj.javaClass.getDeclaredField("intent")
                        intentField.isAccessible = true
                        val intent = intentField.get(obj) as Intent
                        val targetIntent = intent.getParcelableExtra<Intent>(TARGET_INTENT)
                        if (targetIntent != null) {
                            intent.component = targetIntent.component
                        }
                    }
                }
                return false
            }
        })

    }.onFailure {
        it.printStackTrace()
    }
}