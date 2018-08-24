package me.harshithgoka.youtubedl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshithgoka on 12/16/2017 AD.
 */

public class FormatAdapter extends RecyclerView.Adapter<FormatAdapter.MyViewHolder> {

    List<Format> formats;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView quality, type, itag, url;
        public View parentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            quality = itemView.findViewById(R.id.format_quality);
            type = itemView.findViewById(R.id.format_type);
            itag = itemView.findViewById(R.id.format_itag);
            url = itemView.findViewById(R.id.format_url);
            parentView = itemView;
        }
    }

    public FormatAdapter( Context context, List<Format> formats ) {
        this.formats = new ArrayList<>();
        this.formats.addAll(formats);

        this.context = context;
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
        if (format.quality != null)
            holder.quality.setText(format.quality);
        else
            holder.quality.setText("Quality not defined");
        if (format.type != null)
            holder.type.setText(format.type);
        else
            holder.type.setText("Type not defined");

        if (format.url != null)
            holder.url.setText(format.url);
        else
            holder.url.setText("URL not found");

        holder.itag.setText(format.itag + "");

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalurl = ((TextView) view.findViewById(R.id.format_url)).getText().toString();

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                assert clipboard != null;
                ClipData clip = ClipData.newRawUri("DownloadURL", Uri.parse(finalurl));
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, String.format("(%s) Quality link copied to Clipboard", ((TextView) view.findViewById(R.id.format_quality)).getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (formats != null)
            return formats.size();
        else
            return 0;
    }
}
