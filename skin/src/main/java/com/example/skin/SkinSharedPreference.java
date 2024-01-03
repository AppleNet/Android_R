package com.example.skin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * com.example.llc.skin_lib.SkinPreference
 * @author liulongchao
 * @since 2020-09-23
 *
 *  用来记录使用的那份皮肤
 * */
public class SkinSharedPreference {

    private static final String SKIN_SHARED = "skins";
    private static final String KEY_SKIN_PATH = "skin-path";
    private SharedPreferences mPref;

    private static volatile SkinSharedPreference instance;

    public void init(Context context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    private SkinSharedPreference() {}

    public static final class Holder {
        private static final SkinSharedPreference instance = new SkinSharedPreference();
    }

    public static SkinSharedPreference getInstance() {
        return Holder.instance;
    }

    /**
     * 设置并记录当前生效的皮肤包信息
     * @param skinPath 皮肤包路径
     */
    public void setSkin(String skinPath) {
        mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    /**
     * 重置
     */
    public void reset() {
        mPref.edit().remove(KEY_SKIN_PATH).apply();
    }

    /**
     * 获取当前缓存的皮肤包信息
     * @return str
     */
    public String getSkin() {
        return mPref.getString(KEY_SKIN_PATH, null);
    }
}
