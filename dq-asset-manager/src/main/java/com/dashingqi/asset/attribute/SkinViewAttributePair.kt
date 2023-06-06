package com.dashingqi.asset.attribute

import com.dashingqi.asset.constant.EMPTY_STRING
import com.dashingqi.asset.constant.INVALID_RES_ID

/**
 * View的属性与资源ID封装类
 * @author zhangqi
 * @since 2022/6/23
 */
data class SkinViewAttributePair(
    /** View对应的属性名字*/
    val attributeName: String = EMPTY_STRING,
    /** View对应的资源ID*/
    val attributeResId: Int = INVALID_RES_ID
)