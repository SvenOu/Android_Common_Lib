package com.tts.android.mybatic;

import android.app.Application;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sven-ou on 2017/12/13.
 */

public class MybatisManager {
    private static final String TAG = MybatisManager.class.getSimpleName();
    private volatile static MybatisManager instance;

    private Map xmlDocuments;
    private Map proxies;
    private String tempNamespace;
    private String tempMethodKey;
    private Application application;
    private TypedArray mybatisResIdArray;


    public static MybatisManager getInstance() {
        if (instance == null) {
            Log.i(TAG, "MybatisManager instance is null, create it");
            synchronized (MybatisManager.class) {
                if (instance == null) {
                    instance = new MybatisManager();
                }
            }
        } else {
            Log.i(TAG, "DatabaseManager instance is NOT null");
        }
        return instance;
    }

    public void initConfig(Application application, int mybatisResId) {
        this.application = application;
        this.mybatisResIdArray = application.getResources().obtainTypedArray(mybatisResId);;
        xmlDocuments = new HashMap();
        proxies = new HashMap();

        xmlDocuments = new HashMap();
        proxies = new HashMap();

        generateProxies();
    }

    private void generateProxies() {
        for (int i = 0; i<mybatisResIdArray.length(); i++) {
            try {
                readXmlText(mybatisResIdArray.getResourceId(i, -1));
                createProxies();
            } catch (XmlPullParserException | ClassNotFoundException | IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void createProxies() throws ClassNotFoundException {
        for (Object key : xmlDocuments.keySet()) {
            Map mapper = (Map) xmlDocuments.get(key);
            InvocationHandler ds = new DynamicInterfaceProxy(mapper);
            Class<?> cls = Class.forName(key.toString());
            Object proxy = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, ds);
            proxies.put(key, proxy);
        }
    }

    private void readXmlText(int resID) throws XmlPullParserException, IOException {
        XmlResourceParser xmlParser = application.getResources().getXml(resID);
        int event = xmlParser.getEventType();   //先获取当前解析器光标在哪
        while (event != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    Log.i(TAG, "xml parsing begins");
                    break;
                case XmlPullParser.START_TAG:
                    Log.i(TAG, "current tag：" + xmlParser.getName());
                    if (xmlParser.getName().equals(XmlMappingConfig.MAPPER)) {
                        tempNamespace = xmlParser.getAttributeValue(null, XmlMappingConfig.NAMESPACE);
                        Log.i(TAG, "namespace：" + tempNamespace);
                    }
                    if (xmlParser.getName().equals(XmlMappingConfig.TAG_INSERT) ||
                            xmlParser.getName().equals(XmlMappingConfig.TAG_UPDATE) ||
                            xmlParser.getName().equals(XmlMappingConfig.TAG_SELECT) ||
                            xmlParser.getName().equals(XmlMappingConfig.TAG_DELETE)) {
                        String id = xmlParser.getAttributeValue(null, XmlMappingConfig.ATTR_ID);
                        String parameterType = xmlParser.getAttributeValue(null, XmlMappingConfig.ATTR_PARAMETER_TYPE);
                        String resultType = xmlParser.getAttributeValue(null, XmlMappingConfig.ATTR_RESULT_TYPE);
                        String tableForChangeAll = xmlParser.getAttributeValue(null, XmlMappingConfig.TABLE_FOR_CHANGE_ALL);
                        if (TextUtils.isEmpty(id)) {
                            throw new RuntimeException("id is empty with <" + xmlParser.getName() + "> in mapper: " + tempNamespace + ".");
                        }
                        if (TextUtils.isEmpty(parameterType)) {
                            parameterType = "";
                        }
                        if (TextUtils.isEmpty(resultType)) {
                            resultType = "";
                        }
                        if (TextUtils.isEmpty(tableForChangeAll)) {
                            tableForChangeAll = "";
                        }
                        Log.i(TAG, "id：" + id);
                        Log.i(TAG, "parameter_type：" + parameterType);
                        Log.i(TAG, "result_type：" + resultType);
                        Log.i(TAG, "table_for_change_all：" + tableForChangeAll);

                        tempMethodKey = id + parameterType.replaceAll(",\\s+", "");
                        Log.i(TAG, "tempMethodKey：" + tempMethodKey);

                        if (!xmlDocuments.containsKey(tempNamespace)) {
                            xmlDocuments.put(tempNamespace, new HashMap<>());
                        }
                        Map methods = (Map) xmlDocuments.get(tempNamespace);
                        MethodEntity doc = new MethodEntity();
                        doc.setTag(xmlParser.getName());
                        doc.setId(id);
                        doc.setParameterType(parameterType);
                        doc.setResultType(resultType);
                        doc.setTableForChangeAll(tableForChangeAll);
                        methods.put(tempMethodKey, doc);
                    }

                    break;
                case XmlPullParser.TEXT:
                    if (xmlDocuments.containsKey(tempNamespace)) {
                        Map methods = (Map) xmlDocuments.get(tempNamespace);
                        if (methods.containsKey(tempMethodKey)) {
                            MethodEntity doc = (MethodEntity) methods.get(tempMethodKey);
                            doc.setText(xmlParser.getText());
                        }
                    }
                    Log.i(TAG, "Text:" + xmlParser.getText());
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
            event = xmlParser.next();   //将当前解析器光标往下一步移
        }

    }

    public <T> T getMapper(Class<T> cls) {
        return cls.cast(proxies.get(cls.getName()));
    }
}
