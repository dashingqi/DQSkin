package com.dashingqi.hook.singletons

import android.content.Intent
import android.os.Handler
import android.os.Message
import java.lang.reflect.Proxy

/**
 * Singleton 的Hook
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
            classLoader, arrayOf<Class<*>>(iActivityTaskManagerClass),
            DQHookInvocationHandler(mInstance)
        )
        mInstanceField.set(singleton, proxyInstance)
    }.onFailure {
        it.printStackTrace()
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