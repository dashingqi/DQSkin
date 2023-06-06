package com.dashingqi.asset.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.view.LayoutInflater
import com.dashingqi.asset.config.isDebug
import com.dashingqi.asset.constant.EMPTY_STRING
import com.dashingqi.asset.constant.INVALID_RES_ID

/**
 * @desc : 皮肤工具类
 * @author : zhangqi
 * @time : 2022/6/25 16:19
 */

/** addAssetPath 方法名字*/
private const val REFLEX_NAME_ADD_ASSET_PATH = "addAssetPath"

/** mFactorySet 属性名字*/
private const val REFLEX_NAME_FACTORY_SET = "mFactorySet"

/**
 * 反射执行 AssetManager 的 addAssetPath方法，返回AssetManager
 * @param pluginPath String 插件路径
 * @return AssetManager?
 */
fun getOrNullAssetManager(pluginPath: String): AssetManager? {
    return runCatching {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMethod = assetManager.javaClass.getMethod(REFLEX_NAME_ADD_ASSET_PATH)
        addAssetPathMethod.invoke(assetManager, pluginPath)
        assetManager
    }.getOrNull()
}


/**
 * 反射设置mFactorySet 属性值为 false
 * @param layoutInflater LayoutInflater 布局加载器
 * @return Boolean 设置是否成功
 */
@SuppressLint("SoonBlockedPrivateApi")
fun resetFactorySetState(layoutInflater: LayoutInflater): Boolean {
    return runCatching {
        val factorySetFiled = LayoutInflater::class.java.getDeclaredField(REFLEX_NAME_FACTORY_SET)
        factorySetFiled.isAccessible = true
        factorySetFiled.setBoolean(layoutInflater, false)
        true
    }.getOrElse {
        if (isDebug) {
            it.printStackTrace()
        }
        false
    }
}

/**
 * 获取插件包的包名
 * @param context Context 上下文环境
 * @param pluginPath String 插件包的路径
 * @return String 插件包的包名
 */
fun getPluginPackageName(context: Context, pluginPath: String): String {
    return runCatching {
        val pm = context.packageManager
        val packageInfo = pm.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES)
        packageInfo?.packageName ?: EMPTY_STRING
    }.getOrDefault(EMPTY_STRING)
}


/**
 * 构建插件的Resources
 * @param assetManager AssetManager AssetManager
 * @param appResources Resources 宿主的Resource
 * @return Resources pluginResources 构建的插件Resources
 */
fun buildPluginResources(
    assetManager: AssetManager,
    appResources: Resources,
): Resources {

    return Resources(
        assetManager,
        appResources.displayMetrics,
        appResources.configuration
    )
}


/**
 * 获取到Theme中属性定义的资源ID
 * @param context Context 上下文
 * @param attrs IntArray 属性值数组
 * @return IntArray 资源ID
 */
fun getResId(
    context: Context,
    attrs: IntArray
): IntArray {
    val resId = IntArray(attrs.size)
    val typeArray = context.obtainStyledAttributes(attrs)
    for (index in attrs.indices) {
        resId[index] = typeArray.getResourceId(index, INVALID_RES_ID)
    }
    typeArray.recycle()
    return resId
}