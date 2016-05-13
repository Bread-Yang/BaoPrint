package com.MDGround.HaiLanPrint.models;

import java.io.Serializable;

/**
 * Created by dee on 2015/8/5.
 */
public class LocalMedia implements Serializable {

    private String imageLocalPath;
    private long duration;
    private long lastUpdateAt;

    public LocalMedia() {

    }

    public LocalMedia(String imageLocalPath, long lastUpdateAt, long duration) {
        this.imageLocalPath = imageLocalPath;
        this.duration = duration;
        this.lastUpdateAt = lastUpdateAt;
    }

    public LocalMedia(String imageLocalPath) {
        this.imageLocalPath = imageLocalPath;
    }

    public String getImageLocalPath() {
        return imageLocalPath;
    }

    public void setImageLocalPath(String imageLocalPath) {
        this.imageLocalPath = imageLocalPath;
    }

    public long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
