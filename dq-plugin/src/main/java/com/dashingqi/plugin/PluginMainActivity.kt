package com.dashingqi.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PluginMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_main)
        Log.d("DQHookTag", "onCreate perform")
    }
}