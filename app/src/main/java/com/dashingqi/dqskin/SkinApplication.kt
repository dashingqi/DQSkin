package com.dashingqi.dqskin

import android.app.Application
import com.dashingqi.hook.classloader.loadApk
import com.dashingqi.hook.debug.printLog
import com.dashingqi.hook.instru.hookInstrumentation
import java.io.File

/**
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val apkPath = cacheDir.absolutePath + File.separator + "dq-plugin-debug.apk"
        printLog { "apkPath = $apkPath" }
        loadApk(this, apkPath)
        // hookAMS()
        // hookLaunchActivity()
        hookInstrumentation(this)
    }
}