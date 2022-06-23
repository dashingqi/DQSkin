package com.dashingqi.dqskin

import android.app.Application
import com.dashingqi.asset.DQSkinManager

/**
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DQSkinManager.INSTANCE.init(this)
    }
}