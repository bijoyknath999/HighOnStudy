package highonstudy.com.adapters.foryou;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.islamkhsh.CardSliderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import highonstudy.com.R;
import highonstudy.com.api.models.Post;
import highonstudy.com.listeners.ListItemClickListener;
import highonstudy.com.utility.AppUtils;

public class ForYouTopStoriesImageLayoutAdapter extends CardSliderAdapter<ForYouTopStoriesImageLayoutAdapter.FViewHolder> {

    private final List<Post> postList;
    private final LayoutInflater inflater;
    private final Context mContext;

    // Listener
    private ListItemClickListener mListener;

    public ForYouTopStoriesImageLayoutAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.postList = postList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindVH(@NotNull ForYouTopStoriesImageLayoutAdapter.FViewHolder fViewHolder, int i) {
        final Post post = postList.get(i);

        String titleText = null;
        if (post.getTitle()!=null)
        {
            if (post.getTitle().getRendered() != null)
            {
                titleText = post.getTitle().getRendered();
            }
        }
        if (titleText!= null)
        {
            fViewHolder.PostTitleTextView.setText(Html.fromHtml(titleText));
        }
        else
        {
            fViewHolder.PostTitleTextView.setText("");
        }

        String imgUrl = post.getImage().getImage();

        if (imgUrl != null) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(fViewHolder.StoriesImage);
        }
        else {
            Glide.with(mContext)
                    .load(R.color.imgPlaceholder)
                    .into(fViewHolder.StoriesImage);
        }

        fViewHolder.ClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(i, view);
                }
            }
        });


        fViewHolder.TelegramClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TelegramIntent = new Intent(Intent.ACTION_SEND);

                TelegramIntent.setType("text/plain");
                TelegramIntent.setPackage("org.telegram.messenger");
                TelegramIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle().getRendered()+"\n" +
                        ""+post.getLink());
                try {

                    mContext.startActivity(TelegramIntent);

                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(mContext,"Telegram not installed",Toast.LENGTH_SHORT).show();

                }
            }
        });


        fViewHolder.WhatsappClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle().getRendered()+"\n" +
                        ""+post.getLink());
                try {

                    mContext.startActivity(whatsappIntent);

                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(mContext,"Whatsap not installed",Toast.LENGTH_SHORT).show();

                }
            }
        });


        fViewHolder.ShareClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareJob(post.getTitle().getRendered(), post.getLink());
            }
        });

        if (AppUtils.CheckSaveJobs(mContext,post.getLink()))
        {
            Glide.with(mContext)
                    .load(R.drawable.ic_bookmark)
                    .into(fViewHolder.Save_ICon);
        }
        else
        {
            Glide.with(mContext)
                    .load(R.drawable.ic_bookmark_border)
                    .into(fViewHolder.Save_ICon);
        }

        fViewHolder.SaveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastdate = "";
                if (post.getLastdate()!=null)
                {
                    if (post.getLastdate().getValiddate()!=null)
                    {
                        lastdate = post.getLastdate().getValiddate();
                    }
                }
                AppUtils.SaveJob(mContext, post.getTitle().getRendered(), post.getContent().getRendered(), post.getLink(), lastdate);
                notifyItemChanged(i);
            }
        });
    }

    private void shareJob(String jobTitle, String jobURL) {
        String string = mContext.getString(R.string.app_name);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        String sharetext = jobTitle + ": \n" + jobURL + "\n";

        intent.putExtra("android.intent.extra.SUBJECT", string);
        intent.putExtra("android.intent.extra.TEXT", sharetext);
        mContext.startActivity(Intent.createChooser(intent, "Sharing is Caring"));
    }

    @NonNull
    @Override
    public ForYouTopStoriesImageLayoutAdapter.FViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_you_top_stories_image_layout, parent, false);
        return new ForYouTopStoriesImageLayoutAdapter.FViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class FViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout ClickLayout;
        private final ImageView StoriesImage;
        private final ImageView Whatsapp_Icon;
        private final ImageView Like_ICON;
        private final ImageView Save_ICon;
        private final RelativeLayout TelegramClick;
        private final RelativeLayout WhatsappClick;
        private final RelativeLayout ShareClick;
        private final RelativeLayout SaveClick;
        private final TextView PostTitleTextView;

        public FViewHolder(@NonNull View rootView) {
            super(rootView);

            PostTitleTextView = rootView.findViewById(R.id.for_top_stories_image_layout_title);
            ClickLayout = rootView.findViewById(R.id.for_top_stories_image_layout_click_layout);
            StoriesImage = rootView.findViewById(R.id.for_top_stories_image_layout_image);

            TelegramClick = rootView.findViewById(R.id.for_for_top_stories_telegram_click);
            Like_ICON = rootView.findViewById(R.id.for_for_top_stories_telegram);

            WhatsappClick = rootView.findViewById(R.id.for_for_top_stories_whatsapp_click);
            Whatsapp_Icon = rootView.findViewById(R.id.for_for_top_stories_whatsapp);


            ShareClick = rootView.findViewById(R.id.for_for_top_stories_share_click);


            SaveClick = rootView.findViewById(R.id.for_for_top_stories_save_click);
            Save_ICon = rootView.findViewById(R.id.for_for_top_stories_save);


        }
    }

    public void setItemClickListener(ListItemClickListener mListener) {
        this.mListener = mListener;
    }
}