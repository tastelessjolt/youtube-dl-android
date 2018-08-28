package me.harshithgoka.youtubedl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoInfoAdapter extends RecyclerView.Adapter<VideoInfoAdapter.VideoInfoViewHolder> {

    List<VideoInfo> videoInfos;
    Context context;

    VideoInfoAdapter(Context context, List<VideoInfo> videoInfos) {
        this.context = context;
        this.videoInfos = videoInfos;
    }

    @NonNull
    @Override
    public VideoInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_info_item, parent, false);

        return new VideoInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoInfoViewHolder holder, int position) {
        VideoInfo videoInfo = videoInfos.get(position);
        holder.title.setText(videoInfo.title);
        holder.videoId.setText(videoInfo.video_id);
        holder.author.setText(videoInfo.author);
    }

    @Override
    public int getItemCount() {
        if (videoInfos != null)
            return videoInfos.size();
        else
            return 0;
    }

    class VideoInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, videoId, author;
        View copyLink;
        public VideoInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            videoId = itemView.findViewById(R.id.videoId);
            copyLink = itemView.findViewById(R.id.copyLink);
            author = itemView.findViewById(R.id.author);
            copyLink.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        void copyUrl (VideoInfo videoInfo) {
            String url = videoInfo.getUrl();

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;
            ClipData clip = ClipData.newRawUri("DownloadURL", Uri.parse(url));
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, String.format(Locale.UK, "Copied Link: %s", url), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            VideoInfo videoInfo = videoInfos.get(pos);

            switch (v.getId()) {
                case R.id.copyLink:
                    copyUrl(videoInfo);
                    break;
                case R.id.videoInfo:
                    ((MainActivity) context).loadVideoInfo(videoInfo);
                    break;
            }
        }
    }
}
