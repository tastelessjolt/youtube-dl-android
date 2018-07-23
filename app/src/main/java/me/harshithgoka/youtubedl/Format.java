package me.harshithgoka.youtubedl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

public class Format implements Parcelable {
    public String title;
    public int itag;
    public String url;
    public String quality;
    public String type;

    public Format (String title) {
        this.title = title;
    }

    protected Format(Parcel in) {
        itag = in.readInt();
        url = in.readString();
        quality = in.readString();
        type = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itag);
        parcel.writeString(url);
        parcel.writeString(quality);
        parcel.writeString(type);
    }
}