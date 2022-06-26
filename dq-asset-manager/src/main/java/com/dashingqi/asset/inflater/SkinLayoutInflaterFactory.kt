package com.dashingqi.asset.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

/**
 * 皮肤自定义的LayoutInflaterFactory
 * @author zhangqi61
 * @since 2022/6/23
 */
class SkinLayoutInflaterFactory : LayoutInflater.Factory2 {
    /**
     * 生产View
     * @param parent View?
     * @param name String
     * @param context Context
     * @param attrs AttributeSet
     * @return View?
     */
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {

        return null
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }
}