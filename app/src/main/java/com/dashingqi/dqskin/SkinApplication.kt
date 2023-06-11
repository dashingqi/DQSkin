package com.dashingqi.dqskin

import android.app.Application
import android.util.Log
import com.dashingqi.classloader.hookAMS
import com.dashingqi.classloader.hookLaunchActivity
import java.io.File

/**
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val apkPath = cacheDir.absolutePath + File.separator + "dq-plugin-debug.apk"
        Log.d("MainActivity", "apkPath = $apkPath")
        //classLoader(this,apkPath)
        //hookAMS()
        // hookLaunchActivity()
    }
}