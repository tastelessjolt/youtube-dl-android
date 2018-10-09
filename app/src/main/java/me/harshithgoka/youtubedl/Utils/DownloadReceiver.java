package me.harshithgoka.youtubedl.Utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

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

        SharedPreferences sharedPreferences = context.getSharedPreferences("download_history", Context.MODE_PRIVATE);
        Set<String> inProgressDownloads = sharedPreferences.getStringSet("in_progress", new HashSet<String>());



        if(cur.moveToFirst()) {
            if(cur.getInt(status_index) == DownloadManager.STATUS_SUCCESSFUL){
                long id = cur.getLong(id_index);
                if(inProgressDownloads.remove("" + id)) {
                    URI uri = URI.create(cur.getString(uri_index));
                    File file = new File(uri);
                    Log.d("DownloadReceiver", file.getAbsolutePath());
                    File destFile = new File(cur.getString(dest_index));
                    if(file.renameTo(destFile)){
                        Log.d("DownloadReceiver", "Move to final dest successful");
                        Toast.makeText(context, String.format("YoutubeDL download complete to folder \"%s\"", destFile.getParentFile().getAbsolutePath()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        cur.close();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("in_progress", inProgressDownloads);
        editor.apply();
    }
}

