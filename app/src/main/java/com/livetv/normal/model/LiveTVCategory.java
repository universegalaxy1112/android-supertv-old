package com.livetv.normal.model;

import java.util.ArrayList;
import java.util.List;

public class LiveTVCategory extends BaseCategory {

    //    private int id;           //cve
//    private String catName;   //nombre
    private int position;
    private int totalChannels;
    private List<LiveProgram> livePrograms;

    public LiveTVCategory() {
        livePrograms = new ArrayList<>();
    }

    public String getTotalChannels() {
        return "Channels(" + totalChannels + ")";
    }

    public void setTotalChannels(int totalChannels) {
        this.totalChannels = totalChannels;
    }

    public List<LiveProgram> getLivePrograms() {
        return livePrograms;
    }

    public void setLivePrograms(List<LiveProgram> livePrograms) {
        this.livePrograms = livePrograms;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
