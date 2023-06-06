package com.dashingqi.asset.attribute

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.dashingqi.asset.SkinChangeListener
import com.dashingqi.asset.config.isDebug
import com.dashingqi.asset.constant.*
import com.dashingqi.asset.constant.ATTR_BACKGROUND
import com.dashingqi.asset.constant.ATTR_DRAWABLE_LEFT
import com.dashingqi.asset.constant.ATTR_SRC
import com.dashingqi.asset.constant.ATTR_TEXT_COLOR
import com.dashingqi.asset.constant.SKIN_LOG_TAG
import com.dashingqi.asset.utils.SkinResources

/**
 * View与属性对应关系
 * @author zhangqi
 * @since 2022/6/23
 */
data class SkinViewAttribute(
    /** View */
    var view: View,
    /** 属性集合 */
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
     * 实现SkinChangeListener接口的View 进行换肤
     */
    private fun doSkinChange() {
        if (view is SkinChangeListener) {
            (view as SkinChangeListener).onSkinChange()
        }
    }

    /**
     * 处理应用皮肤
     */
    private fun handleApplySkin(attributePair: SkinViewAttributePair) {
        // 属性名
        val attributeName = attributePair.attributeName
        val resId = attributePair.attributeResId
        if (isDebug) {
            Log.d(
                SKIN_LOG_TAG, """
                attributeName is $attributeName
                skinResId is $resId
            """.trimIndent()
            )
        }
        when (attributeName) {
            ATTR_BACKGROUND -> {
                when (val background = SkinResources.INSTANCE.getBackground(resId)) {
                    is Int -> {
                        // 设置的是Color
                        view.setBackgroundColor(background)
                    }
                    is Drawable -> {
                        // 设置的是Drawable文件
                        ViewCompat.setBackground(view, background)
                    }
                }
            }

            ATTR_SRC -> {
                // ImageView特有的
                val isImageView = view is ImageView
                if (!isImageView) return
                when (val background = SkinResources.INSTANCE.getBackground(resId)) {
                    is Int -> {
                        (view as ImageView).setImageDrawable(ColorDrawable(background))
                    }
                    is Drawable -> {
                        (view as ImageView).setImageDrawable(background)
                    }
                }
            }

            ATTR_TEXT_COLOR -> {
                // TextView
                val isTextView = view is TextView
                if (!isTextView) return
                (view as TextView).setTextColor(SkinResources.INSTANCE.getColorStateList(resId))
            }

            ATTR_DRAWABLE_LEFT,
            ATTR_DRAWABLE_RIGHT,
            ATTR_DRAWABLE_TOP,
            ATTR_DRAWABLE_BOTTOM -> {
                // TextView
                val drawable = SkinResources.INSTANCE.getDrawable(resId)
                drawable ?: return
                val isTextView = view is TextView
                if (!isTextView) return
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(drawable, drawable, drawable, drawable)
            }
        }
    }
}