package me.harshithgoka.youtubedl.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import me.harshithgoka.youtubedl.Activities.MainActivity
import me.harshithgoka.youtubedl.R
import me.harshithgoka.youtubedl.YoutubeDL.Utils.FormatUtils
import me.harshithgoka.youtubedl.YoutubeDL.Format

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

class FormatAdapter(internal var context: Context, internal var formats: List<Format>?, internal var mainActivity: MainActivity) : RecyclerView.Adapter<FormatAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var quality: TextView
        var itag: TextView
        var ext: TextView
        var audio: View
        var video: View

        init {
            quality = itemView.findViewById(R.id.format_quality)
            itag = itemView.findViewById(R.id.format_itag)
            ext = itemView.findViewById(R.id.format_ext)
            audio = itemView.findViewById(R.id.audio)
            video = itemView.findViewById(R.id.video)

            // This is nice!
            // https://stackoverflow.com/questions/31627073/why-does-onclicklistener-on-a-viewholder-dont-work
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val finalurl = (view.findViewById<View>(R.id.format_ext) as TextView).text.toString()

            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newRawUri("DownloadURL", Uri.parse(finalurl))
            clipboard.primaryClip = clip
            mainActivity.download(formats!![layoutPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormatAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FormatAdapter.MyViewHolder, position: Int) {
        val format = formats!![position]
        holder.quality.text = FormatUtils.getTitle(format)
        holder.ext.text = format.extension
        holder.itag.text = String.format(Locale.UK, "%d", format.itag)

        holder.audio.setBackgroundResource(if (format.audio) R.drawable.ic_volume_up_black_24dp else R.drawable.ic_volume_off_black_24dp)
        holder.video.setBackgroundResource(if (format.video) R.drawable.ic_videocam_black_24dp else R.drawable.ic_videocam_off_black_24dp)
    }

    override fun getItemCount(): Int {
        return formats?.size ?: 0
    }
}
