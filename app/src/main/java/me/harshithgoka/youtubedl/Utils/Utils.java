package me.harshithgoka.youtubedl.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import me.harshithgoka.youtubedl.Format;

/**
 * Created by harshithg on 22/1/18.
 */

public class Utils {

    public static String _formats = "{\n" +
            "   \"5\":{\n" +
            "      \"ext\":\"flv\",\n" +
            "      \"width\":400,\n" +
            "      \"height\":240,\n" +
            "      \"acodec\":\"mp3\",\n" +
            "      \"abr\":64,\n" +
            "      \"vcodec\":\"h263\"\n" +
            "   },\n" +
            "   \"6\":{\n" +
            "      \"ext\":\"flv\",\n" +
            "      \"width\":450,\n" +
            "      \"height\":270,\n" +
            "      \"acodec\":\"mp3\",\n" +
            "      \"abr\":64,\n" +
            "      \"vcodec\":\"h263\"\n" +
            "   },\n" +
            "   \"13\":{\n" +
            "      \"ext\":\"3gp\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"vcodec\":\"mp4v\"\n" +
            "   },\n" +
            "   \"17\":{\n" +
            "      \"ext\":\"3gp\",\n" +
            "      \"width\":176,\n" +
            "      \"height\":144,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":24,\n" +
            "      \"vcodec\":\"mp4v\"\n" +
            "   },\n" +
            "   \"18\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":640,\n" +
            "      \"height\":360,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":96,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"22\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":1280,\n" +
            "      \"height\":720,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"34\":{\n" +
            "      \"ext\":\"flv\",\n" +
            "      \"width\":640,\n" +
            "      \"height\":360,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"35\":{\n" +
            "      \"ext\":\"flv\",\n" +
            "      \"width\":854,\n" +
            "      \"height\":480,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"36\":{\n" +
            "      \"ext\":\"3gp\",\n" +
            "      \"width\":320,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"vcodec\":\"mp4v\"\n" +
            "   },\n" +
            "   \"37\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":1920,\n" +
            "      \"height\":1080,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"38\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":4096,\n" +
            "      \"height\":3072,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"43\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"width\":640,\n" +
            "      \"height\":360,\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"44\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"width\":854,\n" +
            "      \"height\":480,\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"45\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"width\":1280,\n" +
            "      \"height\":720,\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"46\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"width\":1920,\n" +
            "      \"height\":1080,\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"59\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":854,\n" +
            "      \"height\":480,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"78\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"width\":854,\n" +
            "      \"height\":480,\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"82\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":360,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"83\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"84\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"85\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"100\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":360,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"vp8\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"101\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"vp8\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"102\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"3D\",\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"abr\":192,\n" +
            "      \"vcodec\":\"vp8\",\n" +
            "      \"preference\":-20\n" +
            "   },\n" +
            "   \"91\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":144,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":48,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"92\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":240,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":48,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"93\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":360,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"94\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"95\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":256,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"96\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":256,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"132\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":240,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":48,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"151\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":72,\n" +
            "      \"format_note\":\"HLS\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":24,\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"preference\":-10\n" +
            "   },\n" +
            "   \"133\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":240,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"134\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":360,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"135\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"136\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"137\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"138\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"160\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":144,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"212\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"264\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":1440,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"298\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"299\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"266\":{\n" +
            "      \"ext\":\"mp4\",\n" +
            "      \"height\":2160,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"h264\"\n" +
            "   },\n" +
            "   \"139\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":48,\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"140\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":128,\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"141\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"abr\":256,\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"256\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"258\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"aac\",\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"325\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"dtse\",\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"328\":{\n" +
            "      \"ext\":\"m4a\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"ec-3\",\n" +
            "      \"container\":\"m4a_dash\"\n" +
            "   },\n" +
            "   \"167\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":360,\n" +
            "      \"width\":640,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"168\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"width\":854,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"169\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":720,\n" +
            "      \"width\":1280,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"170\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":1080,\n" +
            "      \"width\":1920,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"218\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"width\":854,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"219\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"width\":854,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp8\"\n" +
            "   },\n" +
            "   \"278\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":144,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"container\":\"webm\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"242\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":240,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"243\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":360,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"244\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"245\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"246\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":480,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"247\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"248\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"271\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":1440,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"272\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":2160,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"302\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":720,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"303\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":1080,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"308\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":1440,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"313\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":2160,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\"\n" +
            "   },\n" +
            "   \"315\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"height\":2160,\n" +
            "      \"format_note\":\"DASH video\",\n" +
            "      \"vcodec\":\"vp9\",\n" +
            "      \"fps\":60\n" +
            "   },\n" +
            "   \"171\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"abr\":128\n" +
            "   },\n" +
            "   \"172\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"acodec\":\"vorbis\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"abr\":256\n" +
            "   },\n" +
            "   \"249\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"opus\",\n" +
            "      \"abr\":50\n" +
            "   },\n" +
            "   \"250\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"opus\",\n" +
            "      \"abr\":70\n" +
            "   },\n" +
            "   \"251\":{\n" +
            "      \"ext\":\"webm\",\n" +
            "      \"format_note\":\"DASH audio\",\n" +
            "      \"acodec\":\"opus\",\n" +
            "      \"abr\":160\n" +
            "   },\n" +
            "   \"_rtmp\":{\n" +
            "      \"protocol\":\"rtmp\"\n" +
            "   }\n" +
            "}";

    public static JSONObject formats;

    static {
        try {
            formats = new JSONObject(_formats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String removeQuotes(String arg) {
        char[] quotes = {'"', '\''};
        for (char quote : quotes) {
            if (arg.charAt(0) == quote && arg.charAt(arg.length() - 1) == quote) {
                return arg.substring(1, arg.length() - 1);
            }
        }
        return arg;
    }

    public static String getExtension (Format format) {
        if (formats != null) {
            try {
                JSONObject fmt = (JSONObject) formats.get(format.itag + "");
                return fmt.getString("ext");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getTitle (Format format) {
        String ret = "";
        if (formats != null) {
            try {
                JSONObject fmt = (JSONObject) formats.get(format.itag + "");
                boolean audio = fmt.has("acodec");
                boolean video = fmt.has("vcodec");

                format.audio = audio;
                format.video = video;

                if ( audio && video) {
                    ret = "Video + Audio";

                    if (fmt.has("height")) {
                        ret += String.format(Locale.UK, " %dp",fmt.getInt("height"));
                    }

                    if (fmt.has("abr")) {
                        ret += String.format(Locale.UK, " %dkbps audio",fmt.getInt("abr"));
                    }
                }
                else if (video) {
                    ret = "Video Only";
                    if (fmt.has("height")) {
                        ret += String.format(Locale.UK, " %dp",fmt.getInt("height"));
                    }
                }
                else if (audio) {
                    ret = "Audio Only";
                    if (fmt.has("abr")) {
                        ret += String.format(Locale.UK, " %dkbps",fmt.getInt("abr"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String getDescription(Format format) {
        if (formats != null) {
            try {
                JSONObject fmt = (JSONObject) formats.get(format.itag + "");
                return fmt.getString("ext");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
