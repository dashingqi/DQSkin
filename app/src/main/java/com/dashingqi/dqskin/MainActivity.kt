package com.dashingqi.dqskin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.dashingqi.classloader.hookInstrumentation
import dalvik.system.PathClassLoader
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnLoadDex).setOnClickListener {
            printClassLoader()
            loadOutApkClass()

        }

        findViewById<Button>(R.id.intentActivity).setOnClickListener {
            Intent(this, IntentActivity::class.java).apply { startActivity(this) }
        }

        hookInstrumentation(this)
    }

    /**
     * 打印ClassLoader
     */
    private fun printClassLoader() {
        Log.d(TAG, "Activity Class Loader = ${Activity::class.java.classLoader}")
        Log.d(TAG, "MainActivity Class Loader = ${MainActivity::class.java.classLoader}")
    }

    /**
     * 加载一个dex文件
     */
    private fun loadDex() {
        kotlin.runCatching {
            val dexPath = cacheDir.path + File.separator + "DexClass.dex"
            Log.d(TAG, "dexPath = $dexPath")
            val pathClassLoader = PathClassLoader(dexPath, null)
            val dexClazz = pathClassLoader.loadClass("com.dashingqi.plugin.DexClassKt")
            val method = dexClazz.getMethod("method")
            method.invoke(null)
        }
    }

    private fun loadOutApkClass() {
        kotlin.runCatching {
            val dexClazz = Class.forName("com.dashingqi.plugin.DexClassKt")
            val method = dexClazz.getMethod("method")
            method.invoke(null)
        }
    }
}