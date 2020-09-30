package com.livetv.normal.model;

class WrapperVideoStream {

    /* renamed from: e */
    private VideoStream f93e;

    public WrapperVideoStream(VideoStream e) {
        this.f93e = e;
    }

    public VideoStream unwrap() {
        return this.f93e;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.f93e.getContentId() != ((VideoStream) o).getContentId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.f93e.getContentId();
    }
}
