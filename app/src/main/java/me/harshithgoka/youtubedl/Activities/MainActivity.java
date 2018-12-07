package me.harshithgoka.youtubedl.Activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.core.util.Pair;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import me.harshithgoka.youtubedl.YoutubeDL.Extractor;
import me.harshithgoka.youtubedl.YoutubeDL.Format;
import me.harshithgoka.youtubedl.Adapters.FormatAdapter;
import me.harshithgoka.youtubedl.R;
import me.harshithgoka.youtubedl.CustomUI.RecyclerViewEmptySupport;
import me.harshithgoka.youtubedl.YoutubeDL.Utils.FormatUtils;
import me.harshithgoka.youtubedl.YoutubeDL.VideoInfo;
import me.harshithgoka.youtubedl.Adapters.VideoInfoAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9293;
    static final String HISTORY = "VideoInfos";

    EditText urlEdit;
    TextView log;
    MaterialButton btnCopy;

    List<Format> formats;

    Extractor extractor;

    Pattern youtubeUrlPattern;

    RecyclerViewEmptySupport formatsRecyclerView;
    FormatAdapter formatAdapter;
    LinearLayoutManager formatLinearLayoutManager;

    RecyclerViewEmptySupport viRecyclerView;
    VideoInfoAdapter viAdapter;
    LinearLayoutManager viLinearLayoutManager;

    BottomSheetBehavior<View> bottomSheetBehavior;
    List<ProgressBar> progressBars;

    SharedPreferences mPrefs;
    SharedPreferences sharedPreferences;
    ArrayList<VideoInfo> history;

    TextView videoTitle;

    WebView webview;

    Gson gson;
    HashMap<Long, Format> inProgressDownloads;
    ArrayList<Pair<Format, Format>> mixingDownloads;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }

        webview = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.loadUrl("http://google.com/");


        String appLogoText = "<font color=#c62828>Y</font>ou<font color=#e15827>T</font>ube<font color=#33745f>DL</font>";
        TextView appLogo = findViewById(R.id.app_logo);
        appLogo.setText(Html.fromHtml(appLogoText));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        this.btnCopy = findViewById(R.id.paste);

        this.btnCopy.setOnClickListener(this);

        log = (TextView) findViewById(R.id.textView);
        log.setMovementMethod(new ScrollingMovementMethod());

        urlEdit = (EditText) findViewById(R.id.url);

        urlEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        Log.d("URL", v.getText().toString());
                        startPoint(null);
                        return true;
                    default:
                        Log.d("Edit text GOT", actionId + " " + event.getKeyCode());
                        return false;
                }
            }
        });

        progressBars = new ArrayList<>();
        progressBars.add((ProgressBar) findViewById(R.id.progressBar));
        progressBars.add((ProgressBar) findViewById(R.id.progressBar2));

        formats = new ArrayList<>();
        extractor = new Extractor();

        youtubeUrlPattern = Pattern.compile(extractor._VALID_URL);

        // Formats Holder Bottom Sheet
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        videoTitle = findViewById(R.id.video_title);
        // Formats
        formatsRecyclerView = findViewById(R.id.recycler_view);
        formatAdapter = new FormatAdapter(getApplicationContext(), formats, this);
        formatsRecyclerView.setAdapter(formatAdapter);
        formatLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        formatsRecyclerView.setLayoutManager(formatLinearLayoutManager);

        // History Get Content
        mPrefs = getPreferences(MODE_PRIVATE);
        gson = new Gson();
        String json = mPrefs.getString(HISTORY, "");

        Type type = new TypeToken< List < VideoInfo >>() {}.getType();
        history = gson.fromJson(json, type);
        if (history == null) {
            history = new ArrayList<>();
        }

        sharedPreferences = getSharedPreferences("download_history", Context.MODE_PRIVATE);

        json = sharedPreferences.getString("in_progress", "");

        type = new TypeToken<HashMap<Long, Format>>() {}.getType();
        inProgressDownloads = gson.fromJson(json, type);
        if (inProgressDownloads == null) {
            inProgressDownloads = new HashMap<Long, Format>();
        }

        json = sharedPreferences.getString("mixing_downloads", "");
        type = new TypeToken<ArrayList<Pair<Format, Format>>>() {}.getType();
        mixingDownloads = gson.fromJson(json, type);
        if (mixingDownloads == null) {
            mixingDownloads = new ArrayList<>();
        }

        // History
        viRecyclerView = findViewById(R.id.historyRecyclerView);
        viAdapter = new VideoInfoAdapter(this, history);
        viRecyclerView.setEmptyView(findViewById(R.id.empty_history));
        viRecyclerView.setAdapter(viAdapter);
        viLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        viRecyclerView.setLayoutManager(viLinearLayoutManager);

        // Best Download

        MaterialButton btn = findViewById(R.id.best_download);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Format f1 = null, f2 = null;
                for (Format format: formats) {
                    if (format.getItag() == 137) {
                        f1 = format;
                    }
                    if (format.getItag() == 140) {
                        f2 = format;
                    }
                }
                Pair<Format, Format> p = new Pair<>(f1, f2);

                if (f1 == null || f2 == null) {
                    return;
                }

                download(f1);
                download(f2);

                mixingDownloads.add(p);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mixing_downloads", gson.toJson(mixingDownloads));
                editor.apply();
            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        ClipData clipData = appLinkIntent.getClipData();
        Uri appLinkData = appLinkIntent.getData();
        String url = null;
        if (appLinkData != null) {
            url = appLinkData.toString();
        }
        else if (clipData != null) {
            if (clipData.getItemCount() > 0)
                url = clipData.getItemAt(0).getText().toString();
        }

        if (url != null) {
            urlEdit.setText(url);
            startDownload(url);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPrefs.edit();

        String connectionsJSONString = gson.toJson(history);
        editor.putString(HISTORY, connectionsJSONString);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            super.onBackPressed();
        }
        else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("INFO", "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.menu_formats, menu);
        return true;
    }

    public void openSettingPage() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_LONG).show();
//                openSettingPage();
                return true;
            default:
                break;
        }

        return false;
    }

    private void println (String s) {
        log.append(s + "\n");
    }

    public String preprocess (String s) {
        int index = s.lastIndexOf("#");
        if (index > 0) {
            s = s.substring(0, index);
        }

        s = s.replaceFirst("m.youtube.com", "www.youtube.com");
        s = s.replaceFirst("&.*", "");

        return s;
    }

    public void startDownload(String url) {
        url = preprocess(url);

        java.util.regex.Matcher m = youtubeUrlPattern.matcher(url);
        println("Url: " + url);

        urlEdit.setText(url);

        if (!m.find()) {
            Toast.makeText(this, "Invalid Youtube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncTask<String, Void, VideoInfo> asyncTask = new YoutubeDLAsyncTask(getApplicationContext(), extractor);
        asyncTask.execute(url);
    }

    public void startPoint(View button) {
        String url = urlEdit.getText().toString();
        startDownload(url);
    }

    public void pasteFromClipboard(View button) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            Uri uri;
            CharSequence url;
            if ( clipData != null && clipData.getItemCount() > 0 ) {
                if ((uri = clipData.getItemAt(0).getUri()) != null) {
                    urlEdit.setText(uri.toString());
                }
                else if ((url = clipData.getItemAt(0).getText()) != null ) {
                    urlEdit.setText(url.toString());
                }
                startPoint(button);
            }
        }
    }

    public void showLoading () {
        for (ProgressBar bar : progressBars) {
            bar.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        for (ProgressBar bar: progressBars) {
            bar.setVisibility(View.GONE);
        }
    }

    public void loadVideoInfo(VideoInfo videoInfo) {
        Log.d("II", "Loading videoInfo");
        int numRemoved = formats.size();
        formats.clear();
        formatAdapter.notifyItemRangeRemoved(0, numRemoved);
        formats.addAll(videoInfo.getFormats());
        formatAdapter.notifyItemRangeInserted(0, videoInfo.getFormats().size());
        videoTitle.setText(videoInfo.getTitle());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paste:
                pasteFromClipboard(v);
                break;
        }
    }

    class YoutubeDLAsyncTask extends AsyncTask<String, Void, VideoInfo> {
        Context context;
        Extractor ytextractor;

        public YoutubeDLAsyncTask(Context context, Extractor extractor) {
            this.context = context;
            ytextractor = extractor;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected VideoInfo doInBackground(String... strings) {
            String you_url = strings[0];
            return ytextractor.getFormats(you_url);
        }

        @Override
        protected void onPostExecute(VideoInfo videoInfo) {
            hideLoading();
            if (videoInfo != null) {

                List<Format> formats = videoInfo.getFormats();

                if (formats.size() > 0) {
                    int index;
                    if ((index = history.indexOf(videoInfo)) != -1) {
                        history.remove(index);
                        history.add(0, videoInfo);
                        viAdapter.notifyItemMoved(index, 0);
                    }
                    else {
                        history.add(0, videoInfo);
                        viAdapter.notifyItemInserted(0);
                    }
                    loadVideoInfo(videoInfo);

                    String finalurl = formats.get(0).getUrl();
                    println(finalurl);

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    assert clipboard != null;
                    ClipData clip = ClipData.newRawUri("DownloadURL", Uri.parse(finalurl));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), String.format("Best quality link (%s) copied to Clipboard", formats.get(0).getQuality()), Toast.LENGTH_SHORT).show();
                }
                else {
                    println("No. of formats: 0");
                    Toast.makeText(getApplicationContext(), "Not yet implemented encrypted signature", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                println("Error connecting to the Internet");
            }
        }
    };


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

    public void download (Format format) {
        String extension = FormatUtils.INSTANCE.getExtension(format);
        String filename = format.sanitizeFilename() + "." + extension;
        Log.d("Filename", filename);
        String final_download_directory = getDownloadDirectory(getApplicationContext());

        DownloadManager dm =null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dm = getSystemService(DownloadManager.class);
        }else{
            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        }

        // Temporary Download folder
        File[] files = ContextCompat.getExternalFilesDirs(getApplicationContext(), Environment.DIRECTORY_MOVIES);
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

        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(format.getUrl()));
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

        inProgressDownloads.put(download_id, format);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("in_progress", gson.toJson(inProgressDownloads));
        editor.apply();

        Toast.makeText(getApplicationContext(), String.format("Your media file now downloading to \"%s\" folder. Check the notification area.", final_download_directory), Toast.LENGTH_SHORT).show();
        format.setDowmloadState(Format.DownloadState.DOWNLOADING);
    }
}
