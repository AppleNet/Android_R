package com.example.skin;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 *  com.example.llc.skin_lib.SkinLayoutInflaterFactory
 *
 * @author liulongchao
 * @since 2020-09-23
 *
 *  用来接管系统 view 的生成过程
 *
 * */
public class SkinLayoutInflaterFactory2 implements LayoutInflater.Factory2, Observer {


    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view.",
    };

    // 记录对应 view 的构造函数
    private static final Class<?>[] mConstructorSignature = new Class[] {
            Context.class, AttributeSet.class
    };

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    // 当选择新皮肤后需要替换 View 与之对应的属性
    // 页面属性管理器
    private final SkinAttributeAttrs mSkinAttribute;
    // 用于获取窗口的状态框的信息
    private final Activity mActivity;

    SkinLayoutInflaterFactory2(Activity activity) {
        this.mActivity = activity;
        this.mSkinAttribute = new SkinAttributeAttrs();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 换肤就是在需要的时候替换 View 的属性(src background 等)
        // 所以这里创建 view 从而修改 View 属性
        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (null != view) {
            // 加载属性
            mSkinAttribute.searchView(view, attrs);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (null != view) {
            mSkinAttribute.searchView(view, attrs);
        }
        return view;
    }

    /**
     * @param name View的类名
     * @param context 上下文
     * @param attrs 属性
     * @return View对象
     */
    private View createSDKView(String name, Context context, AttributeSet attrs) {
        // 如果包含 . 则不是 SDK 中的 View 可能是自定义 View 包括 support 库中的 View
        // 例如com.enjoy.skin.widget.MyTabLayout 或者 android.support.v7.widget.RecyclerView
        if (-1 != name.indexOf('.')) {
            return null;
        }

        for (String s : mClassPrefixList) {
            View view = createView(s + name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * 反射创建全类名View对象
     *
     * @param name 全类名View
     * @param context 上下文
     * @param attrs 属性
     * @return View对象
     */
    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据全类名找到其对应的构造方法对象
     *
     * @param context 上下文
     * @param name 全类名View
     * @return 构造方法对象
     */
    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    /**
     *  Activity(Observable)发出通知，这里就会执行
     *
     *  执行换肤操作
     * */
    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(mActivity);
        mSkinAttribute.applySkin();
    }
}
