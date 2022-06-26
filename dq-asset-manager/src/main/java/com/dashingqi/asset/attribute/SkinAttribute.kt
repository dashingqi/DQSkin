package com.dashingqi.asset.attribute

import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull

/**
 * @desc : 皮肤属性类
 * @author : zhangqi
 * @time : 2022/6/21 07:32
 */

/** 颜色硬编码的前缀*/
private const val PREFIX_COLOR_HARD_CODE_TAG = "#"

class SkinAttribute {

    private val mSkinViews = arrayListOf<SkinViewAttribute>()

    /**
     * 应用皮肤
     */
    fun applySkin() {
        mSkinViews.forEach {
            it.applySkin()
        }
    }

    /**
     * 收集属性值
     */
    fun collectAttribute(@NonNull view: View, @NonNull attributeSet: AttributeSet) {

        val attributeViewPair = arrayListOf<SkinViewAttributePair>()
        for (index in 0..attributeSet.attributeCount) {
            // 属性名
            val attributeName = attributeSet.getAttributeName(index)
            if (attributeName.startsWith(PREFIX_COLOR_HARD_CODE_TAG)) continue
        }

    }
}