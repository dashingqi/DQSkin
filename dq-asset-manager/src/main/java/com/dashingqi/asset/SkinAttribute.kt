package com.dashingqi.asset

import android.view.View

/**
 * @desc :
 * @author : zhangqi
 * @time : 2022/6/21 07:32
 */
class SkinAttribute {

    init {

    }

    private val mSkinViews = arrayListOf<SkinView>()

    fun applySkin() {
        mSkinViews.forEach {
            it.applySkin()
        }
    }

    class SkinView {
        var view: View? = null
        var skinPairs: List<SkinPair> = arrayListOf()

        fun applySkin() {

        }

    }

    class SkinPair {
        var attributeName: String = ""
        var attributeResId: Int = 0
    }
}