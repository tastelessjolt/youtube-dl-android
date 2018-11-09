package me.harshithgoka.youtubedl.YoutubeDL;

import java.sql.Timestamp;
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
    public Timestamp last_updated;

    public String js_code;
    public String func_name;


    VideoInfo(String id, String title, String length, String view_count, String author, String thumbnail_url, Timestamp last_updated, List<Format> formats, String js_code, String func_name) {
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

        this.last_updated = last_updated;
    }

    public String getUrl() {
        return String.format(Locale.UK, "https://www.youtube.com/watch?v=%s", video_id);
    }

    public boolean isEquivalent(VideoInfo v1) {
        return video_id.equals(v1.video_id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VideoInfo) {
            return this.isEquivalent((VideoInfo) obj);
        }
        return false;
    }
}
