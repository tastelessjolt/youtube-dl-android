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

        extension = FormatUtils.getExtension(this);
        content = FormatUtils.getTitle(this);
        description = FormatUtils.getDescription(this);
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

    public String getDownloadDirectory(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("download_folder", Environment.DIRECTORY_DOWNLOADS);
    }

    public String greatestCommonPrefix(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.substring(0, i);
            }
        }
        return a.substring(0, minLength);
    }

    public void download (Context context) {
        String extension = FormatUtils.getExtension(this);
        String filename = sanitizeFilename() + "." + extension;
        Log.d("Filename", filename);
        String final_download_directory = getDownloadDirectory(context);

        DownloadManager dm =null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dm = context.getSystemService(DownloadManager.class);
        }else{
            dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("download_history", Context.MODE_PRIVATE);
        Set<String> inProgressDownloads = sharedPreferences.getStringSet("in_progress", new HashSet<String>());

        // Temporary Download folder
        File[] files = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_MOVIES);
        int maximum_length = 0;
        int which_index = -1;
        String s;
        for (int i = 0; i < files.length; i++) {
            s = greatestCommonPrefix(final_download_directory, files[i].getAbsolutePath());
            if (s.length() > maximum_length) {
                maximum_length = s.length();
                which_index = i;
            }
        }
        Uri uri = null;
        if (which_index > -1) {
            uri = Uri.fromFile(files[which_index]);
        }

        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        req.setTitle(filename);
        req.setDescription(final_download_directory + File.separator + filename);
        if (uri != null) {
            req.setDestinationUri(uri);
        }
        else {
            req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator + filename);
        }
        req.allowScanningByMediaScanner();
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        long download_id = dm.enqueue(req);

        inProgressDownloads.add("" + download_id);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("in_progress", inProgressDownloads);
        editor.apply();

        Toast.makeText(context, String.format("Your media file now downloading to \"%s\" folder. Check the notification area.", final_download_directory), Toast.LENGTH_SHORT).show();
    }
}