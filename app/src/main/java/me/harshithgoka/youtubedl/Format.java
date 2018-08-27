package me.harshithgoka.youtubedl;

import android.app.DownloadManager;
import android.content.ContentProvider;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

import me.harshithgoka.youtubedl.Utils.Utils;

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

public class Format implements Parcelable {
    public String title;
    public int itag;
    public String url;
    public String quality;
    public String type;
    public String extension;
    public String description;
    public String content;

    public boolean audio, video;

    public Format (String title) {
        this.title = title;
    }

    protected Format(Parcel in) {
        itag = in.readInt();
        url = in.readString();
        quality = in.readString();
        type = in.readString();

        extension = Utils.getExtension(this);
        content = Utils.getTitle(this);
        description = Utils.getDescription(this);
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

        extension = Utils.getExtension(this);
        content = Utils.getTitle(this);
        description = Utils.getDescription(this);
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

    public void download (Context context) {
        String extension = Utils.getExtension(this);
        String name = title;
        Log.d("Filename", title + "." + extension);

        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        req.setTitle(name)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + name + "." + extension)
                .allowScanningByMediaScanner();
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager dm =null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            File[] files = context.getExternalMediaDirs();
            if (files.length > 0) {
                Log.d(files[0].getAbsolutePath(), files.length > 1 ? files[1].getAbsolutePath(): "");
            }
            dm = context.getSystemService(DownloadManager.class);
        }else{
            dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        dm.enqueue(req);
    }
}