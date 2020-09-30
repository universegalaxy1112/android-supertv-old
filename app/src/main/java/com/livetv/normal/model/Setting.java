package com.livetv.normal.model;

public class Setting extends VideoStream {
    private String HDPosterUrl;
    private String SDPosterUrl;

    public String getSDPosterUrl() {
        return this.SDPosterUrl;
    }

    public void setSDPosterUrl(String SDPosterUrl2) {
        this.SDPosterUrl = SDPosterUrl2;
    }

    public String getHDPosterUrl() {
        return this.HDPosterUrl;
    }

    public void setHDPosterUrl(String HDPosterUrl2) {
        this.HDPosterUrl = HDPosterUrl2;
    }
}
