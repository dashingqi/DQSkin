package com.dashingqi.asset.attribute

/**
 * @desc : 皮肤属性类
 * @author : zhangqi
 * @time : 2022/6/21 07:32
 */
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
}