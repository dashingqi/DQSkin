package com.dashingqi.classloader

import android.content.Context
import dalvik.system.DexClassLoader
import java.lang.reflect.Array.newInstance

/**
 * 类加载器
 * @author zhangqi
 * @since 2023/6/6
 */


/**
 * 宿主dexElements = 宿主dexElements + 插件dexElements
 *
 * 1.获取宿主dexElements
 * 2.获取插件dexElements
 * 3.合并两个dexElements
 * 4.将新的dexElements 赋值到 宿主dexElements
 *
 * 目标：dexElements  -- DexPathList类的对象 -- BaseDexClassLoader的对象，类加载器
 *
 * 获取的是宿主的类加载器  --- 反射 dexElements  宿主
 *
 * 获取的是插件的类加载器  --- 反射 dexElements  插件
 */
fun classLoader(context: Context, apkPath: String) {
    runCatching {
        // 反射获取BaseDexClassLoader Class对象
        val baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader")
        // 获取元素 pathList 元素
        val pathListField = baseDexClassLoader.getDeclaredField("pathList")
        pathListField.isAccessible = true

        // 反射获取DexPathList Class对象
        val dexPathList = Class.forName("dalvik.system.DexPathList")
        val dexElementsField = dexPathList.getDeclaredField("dexElements")
        dexElementsField.isAccessible = true

        // 获取到宿主类加载器的对象
        val hostClassLoader = context.classLoader
        // 获取到宿主PathList类的对象
        val hostPathList = pathListField.get(hostClassLoader)
        // 获取到宿主DexElements 数组对象
        val hostDexElements = dexElementsField[hostPathList] as Array<Any>


        // 获取到插件类加载器的对象
        val pluginDexClassLoader = DexClassLoader(
            apkPath,
            context.cacheDir.absolutePath,
            null, hostClassLoader
        )
        // 获取到插件pathList类的对象
        val pluginPathList = pathListField.get(pluginDexClassLoader)
        // 获取到插件DexElements数组
        val pluginDexElements = dexElementsField[pluginPathList] as Array<Any>

        // 构造一个新数组
        val newDexElementsArray = newInstance(
            hostDexElements::class.java.componentType,
            hostDexElements.size + pluginDexElements.size
        ) as Array<Any>

        System.arraycopy(
            hostDexElements, 0, newDexElementsArray, 0,
            hostDexElements.size
        )
        System.arraycopy(
            pluginDexElements, 0, newDexElementsArray,
            hostDexElements.size,
            pluginDexElements.size
        )
        // 宿主DexElements重新赋值
        dexElementsField.set(hostPathList, newDexElementsArray)
    }.onFailure {
        it.printStackTrace()
    }
}
