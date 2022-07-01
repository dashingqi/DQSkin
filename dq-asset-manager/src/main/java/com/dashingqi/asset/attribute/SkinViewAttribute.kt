package com.dashingqi.asset.attribute

import android.view.View
import androidx.annotation.NonNull
import com.dashingqi.asset.SkinChangeListener

/**
 * View与属性对应关系
 * @author zhangqi
 * @since 2022/6/23
 */
data class SkinViewAttribute(
    /** View */
    @NonNull
    var view: View,
    /** 属性集合 */
    @NonNull
    var skinPairs: List<SkinViewAttributePair> = arrayListOf()
) {
    /**
     * 应用皮肤，进行换肤
     */
    fun applySkin() {
        doSkinChange()
        for (index in skinPairs.indices) {
            val attributePair = skinPairs[index]
            handleApplySkin(attributePair)
        }
    }

    /**
     * 处理应用皮肤
     */
    private fun handleApplySkin(attributePair: SkinViewAttributePair) {


    }


    /**
     * 实现SkinChangeListener接口的View 进行换肤
     */
    private fun doSkinChange() {
        if (view is SkinChangeListener) {
            (view as SkinChangeListener).onSkinChange()
        }
    }
}