package com.example.startup.store;

import com.example.startup.Startup;

import java.util.List;
import java.util.Map;

/**
 * com.example.startup.store.StartupSortStore
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class StartupSortStore {

    private List<Startup<?>> result;
    // 所有任务
    private Map<Class<? extends Startup>, Startup<?>> startupMap;
    //当前任务的子任务
    private Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap;

    public StartupSortStore() {
    }

    public StartupSortStore(List<Startup<?>> result,
                            Map<Class<? extends Startup>, Startup<?>> startupMap,
                            Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap) {
        this.result = result;
        this.startupMap = startupMap;
        this.startupChildrenMap = startupChildrenMap;
    }

    public Map<Class<? extends Startup>, Startup<?>> getStartupMap() {
        return startupMap;
    }

    public void setStartupMap(Map<Class<? extends Startup>, Startup<?>> startupMap) {
        this.startupMap = startupMap;
    }

    public Map<Class<? extends Startup>, List<Class<? extends Startup>>> getStartupChildrenMap() {
        return startupChildrenMap;
    }

    public void setStartupChildrenMap(Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap) {
        this.startupChildrenMap = startupChildrenMap;
    }

    public List<Startup<?>> getResult() {
        return result;
    }

    public void setResult(List<Startup<?>> result) {
        this.result = result;
    }
}
