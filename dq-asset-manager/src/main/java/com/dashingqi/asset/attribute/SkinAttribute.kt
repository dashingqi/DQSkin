package com.dashingqi.asset.attribute

import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.dashingqi.asset.SkinChangeListener
import com.dashingqi.asset.config.LOG_TAG
import com.dashingqi.asset.config.isDebug
import com.dashingqi.asset.constant.*
import com.dashingqi.asset.utils.getResId

/**
 * @desc : 皮肤属性类
 * @author : zhangqi
 * @time : 2022/6/21 07:32
 */

class SkinAttribute {

    /** 收集View与属性的对应关系 */
    private val mSkinViews = arrayListOf<SkinViewAttribute>()

    /**
     * 应用皮肤，进行换肤
     */
    fun applySkin() {
        // 尽量避免使用 forEach{}
        for (index in mSkinViews.indices) {
            mSkinViews[index].applySkin()
        }
    }

    /**
     * 收集属性值
     */
    fun collectAttribute(view: View, attributeSet: AttributeSet) {
        val attributeViewPair = arrayListOf<SkinViewAttributePair>()
        for (index in 0..attributeSet.attributeCount) {
            // 属性名
            val attributeName = attributeSet.getAttributeName(index)
            val isContainsAttrName = ATTRIBUTES_LIST.contains(attributeName)
            if (isDebug) {
                Log.d(LOG_TAG, "isContainsAttrName $isContainsAttrName")
            }
            if (!isContainsAttrName) continue
            // 属性值
            val attributeValue = attributeSet.getAttributeValue(index)
            // 如果颜色是硬编码，就不用收集替换了
            if (attributeValue.startsWith(PREFIX_COLOR_HARD_CODE)) continue

            // 获取资源ID
            val resId = when (attributeValue) {
                PREFIX_DEFAULT_ATTR_VALUE -> {
                    // @
                    attributeValue.substring(1).toInt()
                }
                PREFIX_QUESTION_MARK -> {
                    // ？
                    val attrId = attributeValue.substring(1).toInt()
                    val resArray = getResId(view.context, IntArray(attrId))
                    if (resArray.isNotEmpty()) {
                        resArray[0]
                    } else {
                        INVALID_RES_ID
                    }
                }
                else -> {
                    INVALID_RES_ID
                }
            }

            val skinViewAttributePair = SkinViewAttributePair(attributeName, resId)
            attributeViewPair.add(skinViewAttributePair)
        }
        // 是否达到条件
        val isRightCondition = attributeViewPair.isNotEmpty() || view is SkinChangeListener
        if (isRightCondition) {
            val skinViewAttribute = SkinViewAttribute(view, attributeViewPair)
            skinViewAttribute.applySkin()
            mSkinViews.add(skinViewAttribute)
        }
    }
}