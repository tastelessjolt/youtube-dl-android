package me.harshithgoka.youtubedl.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import java.util.Locale
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import me.harshithgoka.youtubedl.Activities.MainActivity
import me.harshithgoka.youtubedl.R
import me.harshithgoka.youtubedl.YoutubeDL.VideoInfo

class VideoInfoAdapter(internal var context: Context, internal var videoInfos: List<VideoInfo>?) : RecyclerView.Adapter<VideoInfoAdapter.VideoInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoInfoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_info_item, parent, false)

        return VideoInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoInfoViewHolder, position: Int) {
        val videoInfo = videoInfos!![position]
        holder.title.text = videoInfo.title
        holder.videoId.text = videoInfo.video_id
        holder.author.text = videoInfo.author

        Glide.with(context)
                .load(videoInfo.thumbnail_url)
                .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return if (videoInfos != null)
            videoInfos!!.size
        else
            0
    }

    inner class VideoInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView
        var videoId: TextView
        var author: TextView
        var copyLink: View
        var thumbnail: AppCompatImageView

        init {
            title = itemView.findViewById(R.id.video_title)
            videoId = itemView.findViewById(R.id.videoId)
            copyLink = itemView.findViewById(R.id.copyLink)
            author = itemView.findViewById(R.id.author)
            thumbnail = itemView.findViewById(R.id.thumb)
            copyLink.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        fun copyUrl(videoInfo: VideoInfo) {
            val url = videoInfo.url

            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newRawUri("DownloadURL", Uri.parse(url))
            clipboard.primaryClip = clip

            Toast.makeText(context, String.format(Locale.UK, "Copied Link: %s", url), Toast.LENGTH_SHORT).show()
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            val videoInfo = videoInfos!![pos]

            when (v.id) {
                R.id.copyLink -> copyUrl(videoInfo)
                R.id.videoInfo -> (context as MainActivity).loadVideoInfo(videoInfo)
            }
        }
    }
}
