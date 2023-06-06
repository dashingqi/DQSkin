package com.dashingqi.classloader

import java.lang.reflect.Proxy

/**
 * Activity的Hook
 * @author zhangqi61A
 * @since 2023/6/6
 */
fun hookActivity() {
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