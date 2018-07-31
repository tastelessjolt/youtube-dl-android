package me.harshithgoka.youtubedl.Utils;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCountAdapter(new FileDownloadHelper.ConnectionCountAdapter() {
                    @Override
                    public int determineConnectionCount(int downloadId, String url, String path, long totalLength) {
                        return 4;
                    }
                })
                .commit();
    }
}
