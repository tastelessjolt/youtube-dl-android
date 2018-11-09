package me.harshithgoka.youtubedl.YoutubeDL

import java.sql.Timestamp
import java.util.ArrayList
import java.util.Locale

class VideoInfo (var video_id: String, var title: String, length: String, view_count: String, var author: String, var thumbnail_url: String, var last_updated: Timestamp, formats: List<Format>) {
    var length: Int = -1
    var view_count: Int = -1
    var formats: MutableList<Format>

    val url: String
        get() = String.format(Locale.UK, "https://www.youtube.com/watch?v=%s", video_id)

    init {
        catchAll {
            this.view_count = Integer.parseInt(view_count)
        }

        catchAll {
            this.length = Integer.parseInt(length)
        }

        this.formats = ArrayList<Format>()
        this.formats.addAll(formats)
    }

    fun isEquivalent(v1: VideoInfo): Boolean {
        return video_id == v1.video_id
    }

    override fun equals(other: Any?): Boolean {
        return if (other is VideoInfo) {
            this.isEquivalent(other)
        } else false
    }

    private inline fun catchAll (action: () -> Unit) {
        try {
            action()
        }
        catch (e: Exception) {
            // do nothing
        }
    }

    override fun hashCode(): Int {
        return video_id.hashCode()
    }
}
