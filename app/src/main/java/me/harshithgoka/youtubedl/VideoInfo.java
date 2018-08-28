package me.harshithgoka.youtubedl;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoInfo {
    public String video_id;
    public String thumbnail_url;
    public String title;
    public String author;
    public int length;
    public int view_count;
    public List<Format> formats;


    VideoInfo(String id, String title, String length, String view_count, String author, String thumbnail_url, List<Format> formats) {
        this.video_id = id;
        this.title = title;
        this.author = author;
        try {
            this.view_count = Integer.parseInt(view_count);
        }
        catch (Exception e) {
            // do nothing
            this.view_count = -1;
        }

        try {
            this.length = Integer.parseInt(length);
        }
        catch (Exception e) {
            // do nothing
            this.length = -1;
        }

        this.thumbnail_url = thumbnail_url;
        this.formats = new ArrayList<>();
        this.formats.addAll(formats);
    }

    public String getUrl() {
        return String.format(Locale.UK, "https://www.youtube.com/watch?v=%s", video_id);
    }
}
