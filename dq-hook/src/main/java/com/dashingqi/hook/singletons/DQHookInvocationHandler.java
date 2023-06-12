package com.dashingqi.hook.singletons;

import static com.dashingqi.hook.constants.DQHookConstKt.METHOD_NAME_START_ACTIVITY;
import static com.dashingqi.hook.singletons.DQSingletonHookKt.TARGET_INTENT;

import android.content.Intent;
import android.util.Log;

import com.dashingqi.hook.debug.DQDebugKt;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import kotlin.jvm.functions.Function0;

/**
 * 动态代理接口实现
 * @author zhangqi61
 * @since 2023/6/6
 */
public class DQHookInvocationHandler implements InvocationHandler {
    private final Object mObj;

    public DQHookInvocationHandler(Object obj) {
        mObj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DQDebugKt.printLog(() -> "hookActivity perform method is " + method.getName());
        if (METHOD_NAME_START_ACTIVITY.equals(method.getName())) {
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
                        "com.dashingqi.hook.stub.DQStubActivity");
                proxyIntent.putExtra(TARGET_INTENT, pluginIntent);
                args[index] = proxyIntent;
            }
        }
        return method.invoke(mObj, args);
    }
}
