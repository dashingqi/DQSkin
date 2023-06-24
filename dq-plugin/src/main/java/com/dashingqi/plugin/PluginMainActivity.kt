package com.dashingqi.plugin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

class PluginMainActivity : PluginBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_plugin_main, null)
        setContentView(view)
        Log.d("DQHookTag", "onCreate perform")
    }
}