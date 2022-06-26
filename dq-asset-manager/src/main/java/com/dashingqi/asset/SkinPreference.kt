package com.dashingqi.asset

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * @desc : Skin的SP
 * @author : zhangqi
 * @time : 2022/6/25 16:54
 */
internal class SkinPreference private constructor() {

    /** SharedPreferences */
    private var mSharedPreferences: SharedPreferences? = null


    /**
     * 初始化操作
     * @param context Context 上下文
     */
    fun init(@NonNull context: Context) {
        if (mSharedPreferences != null) return
        mSharedPreferences = context.applicationContext.getSharedPreferences(SKIN_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 设置当前应用的皮肤
     * @param skinPath String 皮肤路径
     */
    fun setSkin(@NonNull skinPath: String) {
        mSharedPreferences?.edit()?.putString(KEY_SKIN_PATH, skinPath)?.apply()
    }

    /**
     * 获取正在应用的皮肤
     * @return String? 皮肤路径
     */
    @Nullable
    fun getSkin(): String? {
        return mSharedPreferences?.getString(KEY_SKIN_PATH, "")
    }

    /**
     * 清空保存的皮肤数据
     */
    fun resetSkin() {
        mSharedPreferences?.edit()?.remove(KEY_SKIN_PATH)?.apply()
    }

    companion object {
        val INSTANCE
            get() = Holder.holder

        /** key SkinPath*/
        private const val KEY_SKIN_PATH = "key-skin-path"

        /** skin sp file name */
        private const val SKIN_FILE_NAME = "skins-file-name"
    }

    object Holder {
        val holder = SkinPreference()
    }
}