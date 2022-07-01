package com.dashingqi.asset.constant

/**
 * 皮肤常量
 * @author zhangqi61
 * @since 2022/7/1
 */

/** 用于标识[.]*/
internal const val CONSTANTS_POINT = "."

/** 无效的角标*/
internal const val INVALID_INDEX = -1

/** 无效的资源ID */
internal const val INVALID_RES_ID = 0

/** 空字符串*/
internal const val EMPTY_STRING = ""

/** Tag */
internal const val SKIN_LOG_TAG = "SkinLogTag"


/************************要收集的属性*****************************/

/** background */
internal const val ATTR_BACKGROUND = "background"

/** src */
internal const val ATTR_SRC = "src"

/** textColor */
internal const val ATTR_TEXT_COLOR = "textColor"

/** drawableLeft*/
internal const val ATTR_DRAWABLE_LEFT = "drawableLeft"

/** drawableRight*/
internal const val ATTR_DRAWABLE_RIGHT = "drawableRight"

/** drawableTop*/
internal const val ATTR_DRAWABLE_TOP = "drawableTop"

/** drawableBottom*/
internal const val ATTR_DRAWABLE_BOTTOM = "drawableBottom"

/*************************************************************/

/** 要收集的属性集合*/
internal val ATTRIBUTES_LIST by lazy {
    arrayListOf(
        ATTR_BACKGROUND,
        ATTR_SRC,
        ATTR_TEXT_COLOR,
        ATTR_DRAWABLE_LEFT,
        ATTR_DRAWABLE_RIGHT,
        ATTR_DRAWABLE_TOP,
        ATTR_DRAWABLE_BOTTOM
    )
}

/**************************属性的开头常量**********************************/

/** 颜色硬编码的前缀  #FFFFFF */
internal const val PREFIX_COLOR_HARD_CODE = "#"
/** ? 开头的属性值*/
internal const val PREFIX_QUESTION_MARK = "?"
/** @ 开头的属性值 正常都是@ */
internal const val PREFIX_DEFAULT_ATTR_VALUE = "@"

/***********************************************************************/