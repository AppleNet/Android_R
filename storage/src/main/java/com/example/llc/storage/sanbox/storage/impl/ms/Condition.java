package com.example.llc.storage.sanbox.storage.impl.ms;

import android.content.ContentValues;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * com.example.llc.storage.sanbox.storage.impl.ms.Condition
 *
 * @author liulongchao
 * @since 2022/4/18
 */
public class Condition {

    /**
     *  查询条件
     * */
    public String whereCause;
    /**
     *  查询条件对应的值
     * */
    public String[] whereArgs;

    /**
     *  contentValues 解析，参数构造
     * */
    Condition(ContentValues contentValues) {
        if (contentValues == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = contentValues.valueSet().iterator();
        ArrayList<String> arrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            String value = (String) next.getValue();
            if (value != null && key != null) {
                if (key.equals(MediaStore.MediaColumns.DISPLAY_NAME)) {
                    stringBuilder.append(key).append(" = ?");
                    arrayList.add(value);
                }
            }
        }
        whereCause = stringBuilder.toString();
        int size = arrayList.size();
        whereArgs = arrayList.toArray(new String[size]);
    }
}
