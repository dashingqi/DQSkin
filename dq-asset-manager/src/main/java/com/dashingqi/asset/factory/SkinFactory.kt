package com.dashingqi.asset.factory

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.dashingqi.asset.attribute.SkinAttribute
import com.dashingqi.asset.constant.*
import com.dashingqi.asset.constant.CONSTANTS_POINT
import com.dashingqi.asset.constant.INVALID_INDEX
import com.dashingqi.asset.constant.PREFIX_ANDROIDX_WIDGET
import com.dashingqi.asset.constant.PREFIX_ANDROID_WEBKIT
import com.dashingqi.asset.constant.PREFIX_ANDROID_WIDGET
import java.lang.reflect.Constructor

/**
 * 皮肤自定义的Factory
 * @author zhangqi
 * @since 2022/6/23
 */
class SkinFactory(activity: Activity) : LayoutInflater.Factory2 {

    private val mClassPrefixList = arrayOf(
        PREFIX_ANDROID_WIDGET,
        PREFIX_ANDROIDX_WIDGET,
        PREFIX_ANDROID_WEBKIT,
        PREFIX_ANDROIDX_WEBKIT,
        PREFIX_ANDROID_APP,
        PREFIX_ANDROIDX_APP,
        PREFIX_ANDROID_VIEW,
        PREFIX_ANDROIDX_VIEW
    )

    private val mConstructorSignature = arrayOf(
        Context::class.java,
        AttributeSet::class.java
    )

    private val mConstructorMap =
        HashMap<String, Constructor<out View>>()

    /** SkinAttribute */
    private val mSkinAttribute: SkinAttribute by lazy {
        SkinAttribute()
    }

    /**
     * 生产View
     * @param parent View?
     * @param name String
     * @param context Context
     * @param attrs AttributeSet
     * @return View?
     */
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        if (attrs == null) return null
        var view = createSDKView(name, context, attrs)
        if (view == null) {
            view = createView(name, context, attrs)
        }
        if (view != null) {
            // 收集属性
            mSkinAttribute.collectAttribute(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    private fun createSDKView(name: String, context: Context, attrs: AttributeSet): View? {
        if (name.indexOf(CONSTANTS_POINT) != INVALID_INDEX) {
            return null
        }
        // 不要轻易使用forEach 存在装箱拆箱性能损耗
        for (index in mClassPrefixList.indices) {
            val view = createView(mClassPrefixList[index] + name, context, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    /**
     * 创建View
     * @param name String
     * @param context Context
     * @param attrs AttributeSet
     * @return View?
     */
    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor = findConstructor(context, name)
        runCatching {
            return constructor?.newInstance(context, attrs)
        }
        return null
    }

    /**
     *
     * @param context Context
     * @param name String
     * @return Constructor<out View>?
     */
    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor = mConstructorMap[name]
        if (constructor == null) {
            runCatching {
                val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                mConstructorMap[name] = constructor as Constructor<out View>
            }.getOrElse {
                it.printStackTrace()
            }
        }
        return constructor
    }
}