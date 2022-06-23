package com.dashingqi.asset

import android.app.Application
import com.dashingqi.asset.lifecycle.SkinAppActivityLifecycle
import com.dashingqi.asset.utils.SkinResources

/**
 * 皮肤管理者
 * @author zhangqi61
 * @since 2022/6/23
 */
class DQSkinManager private constructor() {

    /** 上下文 */
    private lateinit var mContext: Application

    private var mSkinAppActivityLifecycle: SkinAppActivityLifecycle? = null

    /**
     * DQSkinManager的初始化操作
     * @param application Application
     */
    fun init(application: Application) {
        mContext = application
        /** 初始化皮肤资源 用于从app/皮肤包中加载资源*/
        SkinResources.INSTANCE.init(application)
        /** 注册Activity生命周期监听*/
        mSkinAppActivityLifecycle = SkinAppActivityLifecycle()
        application.registerActivityLifecycleCallbacks(mSkinAppActivityLifecycle)
    }

    companion object {
        val INSTANCE
            get() = Holder.holder
    }

    object Holder {
        val holder = DQSkinManager()
    }
}