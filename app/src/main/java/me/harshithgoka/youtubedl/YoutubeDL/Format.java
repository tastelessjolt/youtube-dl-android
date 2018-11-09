package me.harshithgoka.youtubedl.YoutubeDL;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import me.harshithgoka.youtubedl.YoutubeDL.Utils.FormatUtils;

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

public class Format implements Parcelable {
    public enum DownloadState {
        NOT_DOWNLOADED,
        DOWNLOADING,
        DOWNLOADED,
    }

    public String title;
    public int itag;
    public String url;
    public String quality;
    public String type;
    public String extension;
    public String description;
    public String content;

    public boolean audio, video;

    public DownloadState dowmloadState;
    public String location;

    public Format (String title) {
        this.title = title;
    }

    protected Format(Parcel in) {
        itag = in.readInt();
        url = in.readString();
        quality = in.readString();
        type = in.readString();

        extension = FormatUtils.getExtension(this);
        content = FormatUtils.getTitle(this);
        description = FormatUtils.getDescription(this);

        dowmloadState = DownloadState.NOT_DOWNLOADED;
    }

    public void setFormat(Format fmt) {
        title = fmt.title;
        itag = fmt.itag;
        url = fmt.url;
        quality = fmt.quality;
        type = fmt.type;
        extension = fmt.extension;
        description = fmt.description;
        content = fmt.content;

        audio = fmt.audio;
        video = fmt.video;

        dowmloadState = fmt.dowmloadState;
    }

    public static final Creator<Format> CREATOR = new Creator<Format>() {
        @Override
        public Format createFromParcel(Parcel in) {
            return new Format(in);
        }

        @Override
        public Format[] newArray(int size) {
            return new Format[size];
        }
    };

    public void setItag(int itag) {
        this.itag = itag;

        extension = FormatUtils.getExtension(this);
        content = FormatUtils.getTitle(this);
        description = FormatUtils.getDescription(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(itag);
        parcel.writeString(url);
        parcel.writeString(quality);
        parcel.writeString(type);
    }

    public String sanitizeFilename() {
        return title.replaceAll("/", "|");
    }
}