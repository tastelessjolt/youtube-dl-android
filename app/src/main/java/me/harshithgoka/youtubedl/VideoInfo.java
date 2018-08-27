package me.harshithgoka.youtubedl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VideoInfo {
    public String video_id;
    public String thumbnail_url;
    public List<Format> formats;

    VideoInfo(String id, String thumbnail_url, List<Format> formats) {
        this.video_id = id;
        this.thumbnail_url = thumbnail_url;
        this.formats = new ArrayList<>();
        this.formats.addAll(formats);
    }
}
