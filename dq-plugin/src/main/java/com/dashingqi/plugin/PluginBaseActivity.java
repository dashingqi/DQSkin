package com.dashingqi.plugin;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dashingqi.hook.debug.DQDebugKt;
import com.dashingqi.hook.res.DQResLoadUtils;

import java.lang.reflect.Field;

/**
 * @author : zhangqi
 * @desc : 插件的 BaseActivity
 * @time : 2023/6/24 16:23
 */
public class PluginBaseActivity extends AppCompatActivity {

    protected Context mContext;

    private String apkPath = "/data/user/0/com.dashingqi.dqskin/cache/dq-plugin-debug.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resource = DQResLoadUtils.getResource(getApplication(), apkPath);
        mContext = new ContextThemeWrapper(getBaseContext(), 0);

        Class<? extends Context> aClass = mContext.getClass();
        try {
            Field mResourceField = aClass.getDeclaredField("mResources");
            mResourceField.setAccessible(true);
            mResourceField.set(mContext, resource);
        } catch (Exception exception) {
            if (DQDebugKt.getDEBUG()) {
                exception.printStackTrace();
            }
        }

    }
}
