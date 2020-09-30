package com.livetv.normal.model;

import java.util.List;

public class Settings {
    private static List<Setting> settingList;

    public static List<Setting> getMovieList() {
        return settingList;
    }

    public static void setSettingList(List<Setting> list) {
        settingList = list;
    }
}
