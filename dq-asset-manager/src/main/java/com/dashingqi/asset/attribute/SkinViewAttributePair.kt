package com.dashingqi.asset.attribute

/**
 * View的属性与资源ID封装类
 * @author zhangqi61
 * @since 2022/6/23
 */
data class SkinViewAttributePair(
    /** View对应的属性名字*/
    var attributeName: String = "",
    /** View对应的资源ID*/
    var attributeResId: Int = 0
)