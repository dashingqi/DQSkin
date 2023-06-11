package com.dashingqi.hook.classloader;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 修改启动模式
 *
 * @author zhangqi61
 * @since 2023/6/6
 */
public class DynamicProxyUtils {
    public static void hookAms() {
        try {
            Field singletonField;
            Class<?> iActivityManager;            // 1，获取Instrumentation中调用startActivity(,intent,)方法的对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {                // 10.0以上是ActivityTaskManager中的IActivityTaskManagerSingleton
                Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
                singletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
                iActivityManager = Class.forName("android.app.IActivityTaskManager");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                // 8.0,9.0在ActivityManager类中IActivityManagerSingleton
                Class activityManagerClass = ActivityManager.class;
                singletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
                iActivityManager = Class.forName("android.app.IActivityManager");
            } else {                // 8.0以下在ActivityManagerNative类中 gDefault
                Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
                singletonField = activityManagerNative.getDeclaredField("gDefault");
                iActivityManager = Class.forName("android.app.IActivityManager");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);            // 2，获取Singleton中的mInstance，也就是要代理的对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Method getMethod = singletonClass.getDeclaredMethod("get");
            Object mInstance = getMethod.invoke(singleton);
            if (mInstance == null) {
                return;
            }            //开始动态代理
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManager}, new AmsHookBinderInvocationHandler(mInstance));
            mInstanceField.set(singleton, proxy);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }    //动态代理执行类

    public static class AmsHookBinderInvocationHandler implements InvocationHandler {
        private Object obj;

        public AmsHookBinderInvocationHandler(Object rawIActivityManager) {
            obj = rawIActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.d("MainActivity", "hookActivity perform method is " + method.getName());
            return method.invoke(obj, args);
        }
    }
}