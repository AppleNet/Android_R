package com.example.llc.android_r.rv;

/**
 * com.example.llc.android_r.rv.NBAStar
 *
 * @author liulongchao
 * @since 2024/1/14
 */
public class NBAStar {

    private String name;
    private String groupName;

    public NBAStar(String name, String groupName) {
        this.name = name;
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
