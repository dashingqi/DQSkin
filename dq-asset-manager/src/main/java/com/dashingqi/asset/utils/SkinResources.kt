package com.dashingqi.asset.utils

import android.app.Application
import android.content.res.Resources
import android.text.TextUtils
import androidx.annotation.NonNull

/**
 * @desc : 皮肤资源
 * @author : zhangqi
 * @time : 2022/6/21 07:09
 */
class SkinResources private constructor() {

    /** App的Resources*/
    private var mAppResources: Resources? = null

    /** 皮肤的Resources */
    private var mSkinResources: Resources? = null

    /** 皮肤包的包名*/
    private var mSkinPackageName = ""

    /** 记录是否使用默认的皮肤*/
    private var isDefaultSkin = false

    /**
     * 初始化操作
     * @param application Application
     */
    fun init(@NonNull application: Application) {
        mAppResources = application.resources
    }

    /**
     * 应用皮肤
     * @param resources Resources
     * @param packageName String
     */
    fun applySkin(resources: Resources, packageName: String) {
        mSkinPackageName = packageName
        mAppResources = resources
        isDefaultSkin = TextUtils.isEmpty(packageName) || resources == null
    }

    /**
     * 重制皮肤数据,加载皮肤使用默认
     */
    fun resetResources() {
        mSkinResources = null
        mSkinPackageName = ""
        isDefaultSkin = true
    }


    /**
     * 获取皮肤包中的资源ID
     * @param resId Int
     * @return Int
     */
    fun getIdentifier(resId: Int): Int {

        if (isDefaultSkin) {
            return resId
        }
        val resName = mAppResources?.getResourceEntryName(resId)
        val resourceTypeName = mAppResources?.getResourceTypeName(resId)
        return mSkinResources?.getIdentifier(resName, resourceTypeName, mSkinPackageName) ?: 0

    }

    /**
     *
     * @param resId Int
     * @return Int
     */
    fun getColor(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            return mAppResources?.getColor(resId) ?: 0

        }
        return mSkinResources?.getColor(resId) ?: 0
    }

    companion object {
        val INSTANCE
            get() = Holder.holder
    }

    object Holder {
        val holder = SkinResources()
    }

}