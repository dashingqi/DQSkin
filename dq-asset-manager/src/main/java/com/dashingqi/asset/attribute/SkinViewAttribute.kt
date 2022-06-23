package com.dashingqi.asset.attribute

import android.view.View

/**
 * View与属性对应关系
 * @author zhangqi61
 * @since 2022/6/23
 */
data class SkinViewAttribute(
    /** View */
    var view: View? = null,
    /** 属性集合 */
    var skinPairs: List<SkinViewAttributePair> = arrayListOf()
) {
    /**
     * 应用皮肤
     */
    fun applySkin() {

    }
}