package com.example.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * com.example.llc.skin_lib.SkinApplicationActivityLifeCycle
 * @author liulongchao
 * @since 2020-09-24
 * */
public class SkinActivityLifeCycle extends AbsActivityLifeCycle {

    private final Observable mObservable;
    private final ArrayMap<Activity, SkinLayoutInflaterFactory2> mLayoutInflaterFactory = new ArrayMap<>();

    SkinActivityLifeCycle(Observable observable) {
        this.mObservable = observable;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /*
         *  onCreate方法执行之后 回调到这里，此时Activity的onCreate已经执行完毕，也就是setContentView方法已经执行结束，
         *  mFactorySet已经被设置为true，所以要反射修改为false，否则会抛出 IllegalStateException("A factory has already been set on this LayoutInflater")
         * */
        /*
         * 更新状态栏
         * */
        SkinThemeUtils.updateStatusBarColor(activity);
        /*
         * 更新布局视图
         * */
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        try {
            // Android布局加载器 使用 mFactorySet 标记是否设置过 Factory
            // 如设置过 抛出一次异常
            // 设置 mFactorySet 为 false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SkinLayoutInflaterFactory2 skinLayoutInflaterFactory = new SkinLayoutInflaterFactory2(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        mLayoutInflaterFactory.put(activity, skinLayoutInflaterFactory);
        // 观察者模式，当
        mObservable.addObserver(skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutInflaterFactory2 skinLayoutInflaterFactory = mLayoutInflaterFactory.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutInflaterFactory);
    }
}
