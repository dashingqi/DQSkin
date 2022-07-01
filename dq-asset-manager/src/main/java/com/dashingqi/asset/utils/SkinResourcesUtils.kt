package com.dashingqi.asset.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.dashingqi.asset.constant.EMPTY_STRING
import com.dashingqi.asset.constant.INVALID_RES_ID

/**
 * @desc : 皮肤工具类
 * @author : zhangqi
 * @time : 2022/6/25 16:19
 */

/** addAssetPath 方法名字*/
private const val REFLEX_NAME_ADD_ASSET_PATH = "addAssetPath"

/**
 * 反射执行 AssetManager 的 addAssetPath方法，返回AssetManager
 * @param pluginPath String 插件路径
 * @return AssetManager?
 */
@Nullable
fun getOrNullAssetManager(@NonNull pluginPath: String): AssetManager? {
    return runCatching {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMethod = assetManager.javaClass.getMethod(REFLEX_NAME_ADD_ASSET_PATH)
        addAssetPathMethod.invoke(assetManager, pluginPath)
        assetManager
    }.getOrNull()
}

/**
 * 获取插件包的包名
 * @param application Application
 * @param pluginPath String
 * @return String 插件包的包名
 */
@NonNull
fun getPluginPackageName(@NonNull application: Application, @NonNull pluginPath: String): String {
    return runCatching {
        val pm = application.packageManager
        val packageInfo = pm.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES)
        packageInfo?.packageName ?: EMPTY_STRING
    }.getOrDefault(EMPTY_STRING)
}


/**
 * 构建插件的Resources
 * @param assetManager AssetManager
 * @param appResources Resources
 * @param context Context
 * @return Resources pluginResources
 */
@NonNull
fun buildPluginResources(
    @NonNull assetManager: AssetManager,
    @NonNull appResources: Resources,
    context: Context
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
@NonNull
fun getResId(
    @NonNull context: Context,
    @NonNull attrs: IntArray
): IntArray {
    val resId = IntArray(attrs.size)
    val typeArray = context.obtainStyledAttributes(attrs)
    for (index in attrs.indices) {
        resId[index] = typeArray.getResourceId(index, INVALID_RES_ID)
    }
    typeArray.recycle()
    return resId
}