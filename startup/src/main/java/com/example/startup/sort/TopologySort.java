package com.example.startup.sort;

import com.example.startup.Startup;
import com.example.startup.store.StartupSortStore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.example.startup.sort.TopologySort
 *
 * @author liulongchao
 * @since 2022/9/2
 */
public class TopologySort {


    /**
     * 拓扑排序
     * @param startupList startupList
     * @return StartupSortStore
     */
    public static StartupSortStore sort(List<? extends Startup<?>> startupList) {

        // 将所有的任务存入到一个map中
        Map<Class<? extends Startup>, Startup<?>> startupMap = new HashMap<>();

        // 入读表，记录每个任务的依赖数
        Map<Class<? extends Startup>, Integer> inDegreeMap = new HashMap<>();
        // 记录每个任务的依赖任务
        Map<Class<? extends Startup>, List<Class<? extends Startup>>> startupChildrenMap = new HashMap<>();
        // 记录入度为 0 的任务
        Deque<Class<? extends Startup>> zeroDegree = new ArrayDeque<>();
        // 遍历所有的任务
        for (Startup<?> startup : startupList) {
            startupMap.put(startup.getClass(), startup);
            // 获取每个任务的依赖数
            int dependenciesCount = startup.getDependenciesCount();
            // 存入入度表中
            inDegreeMap.put(startup.getClass(), dependenciesCount);

            if (dependenciesCount == 0) {
                // 如果依赖的任务数为0，则存入0入度表
                zeroDegree.offer(startup.getClass());
            } else {
                // 获取每个任务依赖的任务，一个任务可以依赖多个任务，所以是一个集合
                // 例如获取 ImTaskStartup 的 dependencies，包含 DeviceIdTaskStartup CuidTaskStartup
                List<Class<? extends Startup<?>>> dependencies = startup.dependencies();
                // 遍历依赖的任务
                for (Class<? extends Startup<?>> parent : dependencies) {
                    // parent = DeviceIdTaskStartup ｜ parent = CuidTaskStartup
                    List<Class<? extends Startup>> children = startupChildrenMap.get(parent);
                    if (children == null) {
                        children = new ArrayList<>();
                        startupChildrenMap.put(parent, children);
                    }
                    // 也就是说 DeviceIdTaskStartup 的 child 是 ImTaskStartup
                    children.add(startup.getClass());
                }
            }
        }
        // 依次
        List<Startup<?>> result = new ArrayList<>();
        List<Startup<?>> main = new ArrayList<>();
        List<Startup<?>> threads = new ArrayList<>();
        // 如果 0 入度表不为空
        while (!zeroDegree.isEmpty()) {
            Class<? extends Startup> cls = zeroDegree.poll();
            Startup<?> startup = startupMap.get(cls);
            if (startup.callCreateOnMainThread()) {
                main.add(startup);
            } else {
                threads.add(startup);
            }
            //
            if (startupChildrenMap.containsKey(cls)) {
                List<Class<? extends Startup>> childStartup = startupChildrenMap.get(cls);
                for (Class<? extends Startup<?>> childCls : childStartup) {
                    Integer integer = inDegreeMap.get(childCls);
                    inDegreeMap.put(cls, integer - 1);
                    if (integer - 1 == 0) {
                        zeroDegree.offer(childCls);
                    }
                }
            }
        }
        result.addAll(threads);
        result.addAll(main);

        StartupSortStore startupSortStore = new StartupSortStore();
        startupSortStore.setResult(result);
        startupSortStore.setStartupMap(startupMap);
        startupSortStore.setStartupChildrenMap(startupChildrenMap);
        return startupSortStore;
    }


}
