package com.dashingqi.asset

import android.app.Application
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.dashingqi.asset.lifecycle.SkinAppActivityLifecycle
import com.dashingqi.asset.utils.*

/**
 * 皮肤管理者
 * @author zhangqi
 * @since 2022/6/23
 */
class DQSkinManager private constructor() {

    /** 上下文 */
    private var mContext: Application? = null

    /** Activity的生命周期监听 */
    private var mSkinAppActivityLifecycle: SkinAppActivityLifecycle? = null

    /**
     * DQSkinManager的初始化操作
     * @param application Application
     */
    fun init(@NonNull application: Application) {
        mContext = application

        // Sp 初始化
        SkinPreference.INSTANCE.init(application)

        // 初始化皮肤资源 用于从app/皮肤包中加载资源
        SkinResources.INSTANCE.init(application)

        // 注册Activity生命周期监听
        mSkinAppActivityLifecycle = SkinAppActivityLifecycle()
        application.registerActivityLifecycleCallbacks(mSkinAppActivityLifecycle)

        // 加载上次应用的皮肤包
        val preSkinPath = SkinPreference.INSTANCE.getSkin()
        loadSkin(preSkinPath)
    }

    /**
     * 加载皮肤包
     * @param skinPath String 皮肤的路径
     */
    private fun loadSkin(skinPath: String?) {
        // 如果皮肤路径为空，就使用默认的皮肤
        if (skinPath.isNullOrEmpty()) {
            SkinPreference.INSTANCE.resetSkin()
            SkinResources.INSTANCE.resetResources()
        } else {
            mContext?.let { application ->
                val appResources = application.resources
                kotlin.runCatching {
                    val assetManager = getOrNullAssetManager(skinPath)
                    assetManager ?: return
                    val skinResources = buildPluginResources(assetManager, appResources, application)
                    val packageName = getPluginPackageName(application, skinPath)
                    // 设置当前皮肤包的Resources和当前皮肤包路径
                    SkinResources.INSTANCE.applySkin(skinResources, packageName)
                    // 记录下当前应用的皮肤
                    SkinPreference.INSTANCE.setSkin(skinPath)
                }
            }
        }
    }

    companion object {
        val INSTANCE
            get() = Holder.holder
    }

    object Holder {
        val holder = DQSkinManager()
    }
}