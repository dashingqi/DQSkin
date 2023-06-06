package com.dashingqi.classloader;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhangqi61
 * @since 2023/6/6
 */
public class DQHookInvocationHandler implements InvocationHandler {
    private Object mObj;

    public DQHookInvocationHandler(Object obj) {
        mObj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("MainActivity", "hookActivity perform method is " + method.getName());
        return method.invoke(mObj, args);
    }
}
