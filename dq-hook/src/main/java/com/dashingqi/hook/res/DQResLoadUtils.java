package com.dashingqi.hook.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import androidx.annotation.Nullable;

import com.dashingqi.hook.debug.DQDebugKt;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : zhangqi
 * @desc : 资源加载工具类
 * @time : 2023/6/24 15:02
 */
public class DQResLoadUtils {

    /**
     * 存储 Resource
     */
    private static volatile ConcurrentHashMap<String, Resources> mResourceMap;


    /**
     * 获取资源
     *
     * @param context 上下文
     * @param apkPath apk路径
     * @return resource
     */
    @Nullable
    public static Resources getResource(Context context, String apkPath) {
        if (mResourceMap == null) {
            mResourceMap = new ConcurrentHashMap<>();
        }

        if (mResourceMap.containsKey(apkPath)) {
            return mResourceMap.get(apkPath);
        }

        Resources resources = loadResource(context, apkPath);
        if (resources != null) {
            mResourceMap.put(apkPath, resources);
        }
        return resources;
    }


    /**
     * 加载资源
     *
     * @param context 上下文
     * @return Resource
     */
    @Nullable
    private static Resources loadResource(Context context, String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, apkPath);
            Resources resources = context.getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception exception) {
            if (DQDebugKt.getDEBUG()) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
