package com.sv.lib_theme;
import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ThemeManager {
    private static final String TAG = ThemeManager.class.getSimpleName();
    private Application application;
    public void init(Application application) {
        this.application = application;
        initFonts();
    }
    private void initFonts() {
        //初始化字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private volatile static ThemeManager instance;
    public static ThemeManager getInstance() {
        if (instance == null) {
            synchronized (ThemeManager.class) {
                if (instance == null) {
                    instance = new ThemeManager();
                }
            }
        }
        return instance;
    }
    protected ThemeManager() {}
}