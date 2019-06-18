package com.wzhy.viewpagerserial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

public class VpsApplication extends Application {
    public static VpsApplication mApp;
    private static Stack<Activity> mActivityStack;
    private int mForegroundActivityCount = 0;

    public static Context getAppContext() {
        return mApp.getApplicationContext();
    }

    public static VpsApplication getApp() {
        return mApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApp= this;
        mActivityStack = new Stack<>();
        initRegister();
    }

    private void initRegister() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mActivityStack.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mForegroundActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mForegroundActivityCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivityStack.remove(activity);
            }
        });
    }
    /**
     * 前台Activity的个数
     * @return > 0，处于前台；= 0，退到后台。
     */
    public int getForegroundActivityCount(){
        return mForegroundActivityCount;
    }

    /**
     * 是否在后台
     * @return
     */
    public boolean isInBackground() {
        return mForegroundActivityCount <= 0;
    }


    /**
     * 当前Activity
     */
    public static Activity getCurrActivity() {
        return mActivityStack.lastElement();
    }


    /**
     * 退出应用
     */
    public void exitApp() {
        finishAllActivities();
        killProcess();
    }

    /**
     * 关闭所有Activity
     */
    public void finishAllActivities() {
        // 先打印当前容器内的Activity列表
        for (Activity activity : mActivityStack) {
            Log.d("Application", activity.getLocalClassName());
        }

        // 逐个退出Activity
        for (Activity activity : mActivityStack) {
            activity.finish();
        }

    }

    public void killProcess() {
        // 方式1：android.os.Process.killProcess（）
        android.os.Process.killProcess(android.os.Process.myPid());

        // 方式2：System.exit()
        // System.exit() = Java中结束进程的方法：关闭当前JVM虚拟机
        System.exit(0);

        // System.exit(0)和System.exit(1)的区别
        // 1. System.exit(0)：正常退出；
        // 2. System.exit(1)：非正常退出，通常这种退出方式应该放在catch块中。

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mActivityStack = null;
        mApp = null;
    }
}