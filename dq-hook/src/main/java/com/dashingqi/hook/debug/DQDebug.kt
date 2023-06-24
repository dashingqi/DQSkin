package com.dashingqi.hook.debug

import android.util.Log
import com.dashingqi.classloader.BuildConfig

/**
 * @desc : DQ debug log
 * @author : zhangqi
 * @time : 2023/6/11 16:26
 */

/** default tag */
private const val DEFAULT_HOOK_TAG = "DQHookTag"

/** id debug model */
val DEBUG = BuildConfig.DEBUG

fun printLog(messageAction: () -> String) {
    printLog(DEFAULT_HOOK_TAG, messageAction)
}

fun printLog(customTag: String, messageAction: () -> String) {
    if (DEBUG) {
        Log.d(customTag, messageAction.invoke())
    }
}