package com.sv.common.context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {
    private static final String TAG = ContextManager.class.getSimpleName();
    private static ObjectMapper mapper = new ObjectMapper();
    private volatile static ContextManager instance;
    public static ContextManager getInstance() {
        if (instance == null) {
            synchronized (ContextManager.class) {
                if (instance == null) {
                    instance = new ContextManager();
                }
            }
        }
        return instance;
    }
    protected ContextManager() {}

    private final Map context = new HashMap();

    /**
     * @param moduleName 模块名
     * @param clazz 模块名对应的context定义类
     * @return 存储在全局 ContextManager 对应的 moduleName 单例，用作模块内全局变量存储区
     */
    public <T> T getModuleContext(String moduleName, Class<T> clazz) {
        Object moduleData = context.get(moduleName);
        if(moduleData == null){
            try {
                moduleData = clazz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            context.put(moduleName, moduleData);
            return (T) moduleData;
        }
        return mapper.convertValue(moduleData, clazz);
    }
}
