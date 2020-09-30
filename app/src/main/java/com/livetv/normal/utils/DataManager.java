package com.livetv.normal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.livetv.normal.LiveTvApplication;
import java.util.HashSet;
import java.util.Set;

public class DataManager {
    private static DataManager m_DataMInstance;
    private final SharedPreferences pref = LiveTvApplication.getAppContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    private final Editor editor = this.pref.edit();

    public static DataManager getInstance() {
        if (m_DataMInstance == null) {
            m_DataMInstance = new DataManager();
        }
        return m_DataMInstance;
    }

    private DataManager() {

    }

    public void saveDataLong(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.apply();
    }

    public void saveData(String key, Object value) {
        if (value instanceof String) {
            this.editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            this.editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            this.editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            this.editor.putLong(key, ((Long) value).longValue());
        } else if (value instanceof Set) {
            this.editor.remove(key);
            this.editor.apply();
            this.editor.putStringSet(key, (Set) value);
        }
        this.editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return this.pref.getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key) {
        return this.pref.getStringSet(key, new HashSet());
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.pref.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return this.pref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return this.pref.getLong(key, defaultValue);
    }
}
