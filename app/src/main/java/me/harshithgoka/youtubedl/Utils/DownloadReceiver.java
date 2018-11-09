package me.harshithgoka.youtubedl.Utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.core.util.Pair;
import me.harshithgoka.youtubedl.YoutubeDL.Format;
import me.harshithgoka.youtubedl.YoutubeDL.VideoInfo;

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long receivedID = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        DownloadManager mgr = (DownloadManager)
                context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(receivedID);
        Cursor cur = mgr.query(query);
        int status_index = cur.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int id_index = cur.getColumnIndex(DownloadManager.COLUMN_ID);
        int uri_index = cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        int dest_index = cur.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = context.getSharedPreferences("download_history", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("in_progress", "");

        Type type = new TypeToken<HashMap<Long, Format>>() {}.getType();
        HashMap<Long, Format> inProgressDownloads = gson.fromJson(json, type);
        if (inProgressDownloads == null) {
            inProgressDownloads = new HashMap<Long, Format>();
        }

        json = sharedPreferences.getString("mixing_downloads", "");
        type = new TypeToken<ArrayList<Pair<Format, Format>>>() {}.getType();
        ArrayList<Pair<Format, Format>> mixingDownloads = gson.fromJson(json, type);
        if (mixingDownloads == null) {
            mixingDownloads = new ArrayList<>();
        }

        if(cur.moveToFirst()) {
            if(cur.getInt(status_index) == DownloadManager.STATUS_SUCCESSFUL){
                long id = cur.getLong(id_index);
                Format format = inProgressDownloads.get(id);
                if(format != null) {
                    inProgressDownloads.remove(id);
                    URI uri = URI.create(cur.getString(uri_index));
                    File file = new File(uri);
                    Log.d("DownloadReceiver", file.getAbsolutePath());
                    format.setLocation(file.getAbsolutePath());
                    File destFile = new File(cur.getString(dest_index));
                    if(file.renameTo(destFile)){
                        Log.d("DownloadReceiver", "Move to final dest successful");
                        format.setLocation(destFile.getAbsolutePath());
                        Toast.makeText(context, String.format("YoutubeDL download complete to folder \"%s\"", destFile.getParentFile().getAbsolutePath()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        cur.close();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("in_progress", gson.toJson(inProgressDownloads));
        editor.putString("mixing_downloads", gson.toJson(mixingDownloads));
        editor.apply();
    }
}

