package com.dashingqi.hook.singletons

import android.content.Intent
import android.os.Handler
import android.os.Message
import java.lang.reflect.Field
import java.lang.reflect.Proxy

/**
 * Singleton 的Hook
 * @author zhangqi61A
 * @since 2023/6/6
 */

internal const val TARGET_INTENT: String = "targetIntent"

/**
 * hook AMS
 * 主要任务：是将插件中的Activity替换成坑位Activity，绕过AMS的检查
 */
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

/**
 * hook Activity launch
 * 主要是将StubActivity 替换成目标Activity实现真正启动插件中的Activity
 */
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
                when (msg.what) {
                    100 -> {
                        val obj = msg.obj
                        kotlin.runCatching {
                            val intentField = obj::class.java.getDeclaredField("intent")
                            intentField.isAccessible = true
                            val intent = intentField.get(obj) as Intent
                            val targetIntent = intent.getParcelableExtra<Intent>(TARGET_INTENT)
                            if (targetIntent != null) {
                                intent.component = targetIntent.component
                            }
                        }
                    }

                    159 -> {
                        kotlin.runCatching {
                            // 获取 mActivityCallbacks 对象，并取出 LaunchActivityItem 的中 Intent ，进行替换
                            val mActivityCallbacksField: Field =
                                msg.obj::class.java.getDeclaredField("mActivityCallbacks")
                            mActivityCallbacksField.isAccessible = true
                            val mActivityCallbacks = mActivityCallbacksField.get(msg.obj) as ArrayList<*>
                            for (i in mActivityCallbacks.indices) {
                                if (mActivityCallbacks[i].javaClass.name ==
                                    "android.app.servertransaction.LaunchActivityItem"
                                ) {
                                    val launchActivityItem = mActivityCallbacks[i] // 获取启动代理的 Intent
                                    val mIntentField: Field = launchActivityItem.javaClass.getDeclaredField("mIntent")
                                    mIntentField.isAccessible = true
                                    val proxyIntent = mIntentField.get(launchActivityItem) as Intent
                                    val intent =
                                        proxyIntent.getParcelableExtra<Intent>(TARGET_INTENT)
                                    if (intent != null) {
                                        mIntentField.set(launchActivityItem, intent)
                                    }
                                }
                            }
                        }.onFailure {
                            it.printStackTrace()
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