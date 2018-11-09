package me.harshithgoka.youtubedl.YoutubeDL

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast

import com.google.gson.Gson

import java.io.File
import java.util.HashSet

import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import me.harshithgoka.youtubedl.YoutubeDL.Utils.FormatUtils

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

class Format(var title: String) {

    var itag: Int = 0
        set(value) {
            field = value

            extension = FormatUtils.getExtension(this)
            content = FormatUtils.getTitle(this)
            description = FormatUtils.getDescription(this)
        }

    var url: String? = null
    var quality: String? = null
    var type: String? = null

    var extension: String? = null
    var content: String? = null
    var description: String? = null

    var audio: Boolean = false
    var video: Boolean = false

    var dowmloadState: DownloadState = DownloadState.NOT_DOWNLOADED
    var location: String? = null

    enum class DownloadState {
        NOT_DOWNLOADED,
        DOWNLOADING,
        DOWNLOADED
    }

    fun sanitizeFilename(): String {
        return title.replace("/".toRegex(), "|")
    }
}