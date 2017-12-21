package me.harshithgoka.youtubedl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText urlEdit;
    TextView log;
    OkHttpClient client;

    List<Format> curr_formats;

    String _VALID_URL = "(?x)^\n" +
            " (\n" +
            "     (?:https?://|//)                                    # http(s):// or protocol-independent URL\n" +
            "     (?:(?:(?:(?:\\w+\\.)?[yY][oO][uU][tT][uU][bB][eE](?:-nocookie)?\\.com/|\n" +
            "        (?:www\\.)?deturl\\.com/www\\.youtube\\.com/|\n" +
            "        (?:www\\.)?pwnyoutube\\.com/|\n" +
            "        (?:www\\.)?hooktube\\.com/|\n" +
            "        (?:www\\.)?yourepeat\\.com/|\n" +
            "        tube\\.majestyc\\.net/|\n" +
            "        youtube\\.googleapis\\.com/)                        # the various hostnames, with wildcard subdomains\n" +
            "     (?:.*?\\#/)?                                          # handle anchor (#/) redirect urls\n" +
            "     (?:                                                  # the various things that can precede the ID:\n" +
            "         (?:(?:v|embed|e)/(?!videoseries))                # v/ or embed/ or e/\n" +
            "         |(?:                                             # or the v= param in all its forms\n" +
            "             (?:(?:watch|movie)(?:_popup)?(?:\\.php)?/?)?  # preceding watch(_popup|.php) or nothing (like /?v=xxxx)\n" +
            "             (?:\\?|\\#!?)                                  # the params delimiter ? or # or #!\n" +
            "             (?:.*?[&;])??                                # any other preceding param (like /?s=tuff&v=xxxx or ?s=tuff&amp;v=V36LpHqtcDY)\n" +
            "             v=\n" +
            "         )\n" +
            "     ))\n" +
            "     |(?:\n" +
            "        youtu\\.be|                                        # just youtu.be/xxxx\n" +
            "        vid\\.plus|                                        # or vid.plus/xxxx\n" +
            "        zwearz\\.com/watch|                                # or zwearz.com/watch/xxxx\n" +
            "     )/\n" +
            "     |(?:www\\.)?cleanvideosearch\\.com/media/action/yt/watch\\?videoId=\n" +
            "     )\n" +
            " )?                                                       # all until now is optional -> you can pass the naked ID\n" +
            " ([0-9A-Za-z_-]{11})                                      # here is it! the YouTube video ID\n" +
            " (?!.*?\\blist=\n" +
            "    (?:\n" +
            "        %(playlist_id)s|                                  # combined list/video URLs are handled by the playlist IE\n" +
            "        WL                                                # WL are handled by the watch later IE\n" +
            "    )\n" +
            " )\n" +
            "                                                 # if we found the ID, everything can follow\n" +
            " $";

    public String getID (String url) {
        Pattern pattern = Pattern.compile(_VALID_URL);
        Matcher m = pattern.matcher("https://www.youtube.com/watch?v=6D_BFaAewLU");
        m.find();
        return m.group(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();

        log = (TextView) findViewById(R.id.textView);
        log.setMovementMethod(new ScrollingMovementMethod());

        urlEdit = (EditText) findViewById(R.id.url);

        curr_formats = new ArrayList<>();
    }

    private void println (String s) {
        log.append(s + "\n");
    }


    public void startPoint(View button) {
        String url = urlEdit.getText().toString();

        println("Url: " + url);

        AsyncTask<String, Void, List<Format>> asyncTask = new GetInfoAsyncTask();
        asyncTask.execute(url);
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void pasteFromClipboard(View button) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipData clipData = clipboard.getPrimaryClip();
            Uri uri;
            String url;
            if ( clipData.getItemCount() > 0 ) {
                if ((uri = clipData.getItemAt(0).getUri()) != null) {
                    urlEdit.setText(uri.toString());
                }
                else if ((url = clipData.getItemAt(0).getText().toString()) != null ) {
                    urlEdit.setText(url);
                }
                startPoint(button);
            }
        }
    }

    public void showAllFormats(View view) {
        if (curr_formats.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), FormatsActivity.class);
            intent.putParcelableArrayListExtra(FormatsActivity.FORMATS, (ArrayList<? extends Parcelable>) curr_formats);
            startActivity(intent);
        }
    }

    class GetInfoAsyncTask extends AsyncTask<String, Void, List<Format>> {

        public void decryptSignature(String s, String video_id, String player_url) {
            // TODO: continue to get the function from player url
        }

        @Override
        protected List<Format> doInBackground(String... strings) {
            String you_url = strings[0];
            String response;
            JSONObject ret = new JSONObject();

            try {
                ret.put("status", false);
                response = run(you_url);
                Pattern ytconf = Pattern.compile("ytplayer.config[ =]*");
                Pattern ytcond_end = Pattern.compile(";[ ]*ytplayer\\.load");
                String json = ytcond_end.split(ytconf.split(response)[1])[0];
                JSONObject ytconfig = new JSONObject(json);
                ret.put ("data", ytconfig);
                ret.put("status", true);

                String fmts = ytconfig.getJSONObject("args").getString("url_encoded_fmt_stream_map") + "," + ytconfig.getJSONObject("args").getString("adaptive_fmts");

                String[] fmts_enc = fmts.split(",");
                List<Format> formats = new ArrayList<>();

                for (String fmt : fmts_enc) {
                    Format f = new Format();

                    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
                    String[] pairs = fmt.split("&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf("=");
                        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                    }

                    String url = query_pairs.get("url");

                    Set<String> params = query_pairs.keySet();
                    if (params.contains("sig"))
                        url += "&signature=" + query_pairs.get("sig");
                    else if (params.contains("s")) {
                        Pattern pattern = Pattern.compile("\"assets\":.+?\"js\":\\s*(\"[^\"]+\")");
                        Matcher m = pattern.matcher(response);
                        m.find();
                        String player_url = m.group(1);
                        player_url = new JSONObject("{ \"str\" :" + player_url + "}").getString("str");

                        Pattern playerType = Pattern.compile("(html5player-([^/]+?)(?:/html5player(?:-new)?)?\\.js)|((?:www|player)-([^/]+)(?:/[a-z]{2}_[A-Z]{2})?/base\\.js)");
                        m = playerType.matcher(player_url);
                        m.find();

                        String player_version = m.group();
                        String player_desc = "html5 player " + player_version;
                        String encrypted_signature = query_pairs.get("s");
                        String videoID = getID(you_url);

                        decryptSignature(encrypted_signature, videoID, you_url);

                        continue;
                    }

                    if (!url.contains("ratebypass")) {
                        url += "&ratebypass=yes";
                    }

                    f.url = url;

                    for (String param: params) {
                        if (param.equals("itag")) {
                            f.itag = Integer.parseInt(query_pairs.get(param));
                        }

                        if (param.equals("type")) {
                            f.type = query_pairs.get(param);
                        }

                        if (param.equals("quality")) {
                            f.quality = query_pairs.get(param);
                        }
                    }

                    formats.add(f);
                }

                return formats;


            } catch (IOException e) {
                try {
                    ret.put("message", e.toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Log.d("Err", e.toString());
            } catch (JSONException e) {
                try {
                    ret.put("message", e.toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
//                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Format> formats) {
            if (formats != null) {
                if (formats.size() > 0) {
                    curr_formats.clear();
                    curr_formats.addAll(formats);

                    String finalurl = formats.get(0).url;
                    println(finalurl);

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    assert clipboard != null;
                    ClipData clip = ClipData.newRawUri("DownloadURL", Uri.parse(finalurl));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), String.format("Best quality link (%s) copied to Clipboard", formats.get(0).quality), Toast.LENGTH_SHORT).show();
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
}
