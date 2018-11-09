package me.harshithgoka.youtubedl.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import me.harshithgoka.youtubedl.Activities.MainActivity;
import me.harshithgoka.youtubedl.R;
import me.harshithgoka.youtubedl.YoutubeDL.Utils.FormatUtils;
import me.harshithgoka.youtubedl.YoutubeDL.Format;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

public class FormatAdapter extends RecyclerView.Adapter<FormatAdapter.MyViewHolder> {

    List<Format> formats;
    Context context;
    MainActivity mainActivity;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView quality, itag, ext;
        public View audio, video;

        public MyViewHolder(View itemView) {
            super(itemView);
            quality = itemView.findViewById(R.id.format_quality);
            itag = itemView.findViewById(R.id.format_itag);
            ext = itemView.findViewById(R.id.format_ext);
            audio = itemView.findViewById(R.id.audio);
            video = itemView.findViewById(R.id.video);

            // This is nice!
            // https://stackoverflow.com/questions/31627073/why-does-onclicklistener-on-a-viewholder-dont-work
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String finalurl = ((TextView) view.findViewById(R.id.format_ext)).getText().toString();

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;
            ClipData clip = ClipData.newRawUri("DownloadURL", Uri.parse(finalurl));
            clipboard.setPrimaryClip(clip);
//            Toast.makeText(context, String.format("Your video \"%s\" is now downloading. Check the notification area.", formats.get(getLayoutPosition()).title), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, String.format("(%s) Quality link copied to Clipboard", ((TextView) view.findViewById(R.id.format_quality)).getText().toString()), Toast.LENGTH_SHORT).show();

            mainActivity.download(formats.get(getLayoutPosition()));
//            formats.get(getLayoutPosition()).download(context);
        }
    }

    public FormatAdapter( Context context, List<Format> formats, MainActivity activity ) {
        this.formats = formats;
        this.context = context;
        this.mainActivity = activity;
    }

    @Override
    public FormatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FormatAdapter.MyViewHolder holder, int position) {
        Format format = formats.get(position);
        holder.quality.setText(FormatUtils.getTitle(format));
        holder.ext.setText(format.extension);
        holder.itag.setText(String.format(Locale.UK, "%d", format.itag));

        holder.audio.setBackgroundResource(format.audio ? R.drawable.ic_volume_up_black_24dp : R.drawable.ic_volume_off_black_24dp);
        holder.video.setBackgroundResource(format.video ? R.drawable.ic_videocam_black_24dp: R.drawable.ic_videocam_off_black_24dp);
    }

    @Override
    public int getItemCount() {
        if (formats != null)
            return formats.size();
        else
            return 0;
    }
}
