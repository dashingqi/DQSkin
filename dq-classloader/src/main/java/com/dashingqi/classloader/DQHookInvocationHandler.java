package com.dashingqi.classloader;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhangqi61
 * @since 2023/6/6
 */
public class DQHookInvocationHandler implements InvocationHandler {
    private final Object mObj;

    private static final String TARGET_INTENT = "targetIntent";

    public DQHookInvocationHandler(Object obj) {
        mObj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("MainActivity", "hookActivity perform method is " + method.getName());
        if ("startActivity".equals(method.getName())) {
            int index = -1;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                Intent pluginIntent = (Intent) args[index];
                Intent proxyIntent = new Intent();
                proxyIntent.setClassName("com.dashingqi.dqskin",
                        "com.dashingqi.dqskin.DQProxyActivity");
                proxyIntent.putExtra(TARGET_INTENT, pluginIntent);
                args[index] = proxyIntent;
            }
        }
        return method.invoke(mObj, args);
    }
}
