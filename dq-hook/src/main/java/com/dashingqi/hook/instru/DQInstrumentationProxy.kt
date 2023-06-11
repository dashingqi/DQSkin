package com.dashingqi.hook.instru

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.dashingqi.hook.constants.KEY_TARGET_INTENT_NAME
import com.dashingqi.hook.constants.STUB_ACTIVITY_CLASS_NAME
import com.dashingqi.hook.debug.printLog

/**
 * @desc : DQ Instrumentation Proxy
 * @author : zhangqi
 * @time : 2023/6/11 14:54
 */
class DQInstrumentationProxy(
    private val originInstrumentation: Instrumentation, private val packageManager: PackageManager
) : Instrumentation() {

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        val targetActivityClassName = intent?.getStringExtra(KEY_TARGET_INTENT_NAME) ?: ""
        printLog { "targetActivityClassName = $targetActivityClassName" }
        if (targetActivityClassName.isNotEmpty()) {
            return super.newActivity(cl, targetActivityClassName, intent)
        }
        return super.newActivity(cl, className, intent)
    }

    fun execStartActivity(
        who: Context,
        contextThread: IBinder,
        token: IBinder,
        target: Activity,
        intent: Intent,
        requestCode: Int,
        options: Bundle?
    ): ActivityResult? {
        return kotlin.runCatching {
            val resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            if (resolveInfo == null || resolveInfo.size == 0) {
                // 保存目标插件
                intent.putExtra(KEY_TARGET_INTENT_NAME, intent.component?.className)
                intent.setClassName(who, STUB_ACTIVITY_CLASS_NAME)
            }
            printLog { "hook instrumentation perform before" }
            val execStartActivityMethod = Instrumentation::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                Activity::class.java,
                Intent::class.java,
                Int::class.java,
                Bundle::class.java
            )
            execStartActivityMethod.isAccessible = true
            val pluginActivityResult = execStartActivityMethod.invoke(
                originInstrumentation, who, contextThread, token, target, intent, requestCode, options
            ) as? ActivityResult
            printLog { "hook instrumentation perform after" }
            return pluginActivityResult
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

    }
}