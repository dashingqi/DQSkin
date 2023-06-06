package com.dashingqi.asset.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.dashingqi.asset.config.isDebug
import com.dashingqi.asset.constant.COLOR_TAG
import com.dashingqi.asset.constant.EMPTY_STRING
import com.dashingqi.asset.constant.INVALID_RES_ID

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
    private var mSkinPackageName = EMPTY_STRING

    /** 记录是否使用默认的皮肤*/
    private var isDefaultSkin = false

    /**
     * 初始化操作
     * @param application Context
     */
    fun init(application: Context) {
        mAppResources = application.resources
    }

    /**
     * 应用皮肤
     * @param resources Resources
     * @param packageName String
     */
    fun applySkin(resources: Resources?, packageName: String) {
        mSkinPackageName = packageName
        mAppResources = resources
        isDefaultSkin = packageName.isEmpty() || resources == null
    }

    /**
     * 重制皮肤数据,加载皮肤使用默认
     */
    fun resetResources() {
        mSkinResources = null
        mSkinPackageName = EMPTY_STRING
        isDefaultSkin = true
    }


    /**
     * 获取皮肤包中的资源ID
     * @param resId Int
     * @return Int
     */
    private fun getIdentifier(resId: Int): Int {

        if (isDefaultSkin) {
            return resId
        }
        val resName = mAppResources?.getResourceEntryName(resId)
        val resourceTypeName = mAppResources?.getResourceTypeName(resId)
        return mSkinResources?.getIdentifier(resName, resourceTypeName, mSkinPackageName) ?: INVALID_RES_ID

    }

    /**
     *
     * @param resId Int
     * @return ColorStateList?
     */
    fun getColorStateList(resId: Int): ColorStateList? {
        return runCatching {
            if (isDefaultSkin) {
                return ResourcesCompat.getColorStateList(mAppResources!!, resId, null)
            }
            val skinId = getIdentifier(resId)
            if (skinId == INVALID_RES_ID) {
                return ResourcesCompat.getColorStateList(mAppResources!!, resId, null)
            }
            return ResourcesCompat.getColorStateList(mSkinResources!!, resId, null)
        }.getOrElse {
            if (isDebug) {
                it.printStackTrace()
            }
            null
        }
    }

    /**
     * 获取颜色
     * @param resId Int 资源ID
     * @return Int
     */
    fun getColor(resId: Int): Int? {
        return runCatching {
            if (isDefaultSkin) {
                return ResourcesCompat.getColor(mAppResources!!, resId, null)
            }
            val skinId = getIdentifier(resId)
            if (skinId == INVALID_RES_ID) {
                return ResourcesCompat.getColor(mAppResources!!, resId, null)

            }
            return ResourcesCompat.getColor(mSkinResources!!, resId, null)
        }.getOrElse {
            if (isDebug) {
                it.printStackTrace()
            }
            null
        }
    }


    /**
     * 获取Drawable
     * @param resId Int 资源ID
     * @return Drawable? 可为空的Drawable
     */
    fun getDrawable(resId: Int): Drawable? {
        return runCatching {
            if (isDefaultSkin) {
                return ResourcesCompat.getDrawable(mAppResources!!, resId, null)
            }

            // 获取到皮肤包中对应资源ID
            val skinId = getIdentifier(resId)

            if (skinId == INVALID_RES_ID) {
                return ResourcesCompat.getDrawable(mAppResources!!, resId, null)
            }
            return ResourcesCompat.getDrawable(mSkinResources!!, skinId, null)
        }.getOrElse {
            if (isDebug) {
                it.printStackTrace()
            }
            null
        }
    }


    /**
     * 获取的可能是Color也可能是Drawable
     * @param resId Int 资源ID
     */
    fun getBackground(resId: Int): Any? {
        return runCatching {
            val resourceTypeName = mAppResources?.getResourceTypeName(resId)
            if (resourceTypeName.isNullOrEmpty()) return null
            when (resourceTypeName) {
                COLOR_TAG -> {
                    getColor(resId)
                }
                else -> {
                    getDrawable(resId)
                }
            }
        }.getOrElse {
            if (isDebug) {
                it.printStackTrace()
            }
            null
        }
    }

    companion object {
        val INSTANCE
            get() = Holder.holder
    }

    object Holder {
        val holder = SkinResources()
    }

}