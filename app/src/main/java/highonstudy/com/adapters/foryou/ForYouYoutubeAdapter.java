package highonstudy.com.adapters.foryou;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import highonstudy.com.R;
import highonstudy.com.activity.UniversalActivity;
import highonstudy.com.api.models.youtube.YoutubeDetails;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.models.ForYouEducationItems;

public class ForYouYoutubeAdapter extends RecyclerView.Adapter<ForYouYoutubeAdapter.MyViewHolder> {

    private final List<YoutubeDetails> youtubeDetails;
    private final LayoutInflater inflater;
    private final Context mContext;
    private final Lifecycle lifecycle;

    public ForYouYoutubeAdapter(Context context, List<YoutubeDetails> youtubeDetails, Lifecycle lifecycle) {
        this.mContext = context;
        this.youtubeDetails = youtubeDetails;
        this.lifecycle = lifecycle;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ForYouYoutubeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_you_youtube, parent, false);
        return new ForYouYoutubeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouYoutubeAdapter.MyViewHolder holder, int position) {
        final YoutubeDetails item = youtubeDetails.get(position);

        lifecycle.addObserver(holder.youTubePlayerView);
        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = item.getVideo_id();
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        holder.ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareJob(String.valueOf(Html.fromHtml(item.getTitle())), item.getVideo_id());
            }
        });


    }

    private void shareJob(String Title, String ID) {
        String string = mContext.getString(R.string.app_name);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        String sharetext = Title + ": \nhttps://youtu.be/" + ID + "\n";

        intent.putExtra("android.intent.extra.SUBJECT", string);
        intent.putExtra("android.intent.extra.TEXT", sharetext);
        mContext.startActivity(Intent.createChooser(intent, "Sharing is Caring"));
    }

    @Override
    public int getItemCount() {
        return youtubeDetails.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final YouTubePlayerView youTubePlayerView;
        private final Button ShareButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.for_you_youtube_player_view);
            ShareButton = itemView.findViewById(R.id.for_you_youtube_share_button);

        }
    }
}