package highonstudy.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.AppPreference;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.SavedJObsModel;
import highonstudy.com.data.sqlite.SavedJobsDbController;
import highonstudy.com.utility.AdUtils;
import highonstudy.com.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyViewHolder> implements Filterable {

    private List<Post> postList = new ArrayList<>();
    private List<Post> postListFiltered = new ArrayList<>();
    private LayoutInflater inflater;
    private final Context mContext;
    // Favourites view
    private final List<SavedJObsModel> savedJObsModels = new ArrayList<>();
    private SavedJobsDbController savedJobsDbController;
    private final boolean isSaved = false;


    public JobsAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.postList = postList;
        this.postListFiltered = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobsAdapter.MyViewHolder holder, int position) {
        Post post = postListFiltered.get(position);

        String title = null;
        if (post.getTitle().getRendered() != null)
        {
            title = postListFiltered.get(position).getTitle().getRendered();
            holder.JobsTitle.setText(Html.fromHtml(title));

        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT, 4);
        String lastdate = null;
        if (post.getLastdate()!=null)
        {
            if (post.getLastdate().getValiddate() != null) {
                lastdate = post.getLastdate().getValiddate();
                if (AppUtils.isValidFormat(lastdate))
                {
                    String month = AppUtils.getMonth(postListFiltered.get(position).getLastdate().getValiddate());
                    if (month != null)
                    {
                        holder.JobsMonth.setText(month);
                    }
                    String date = AppUtils.getDate(postListFiltered.get(position).getLastdate().getValiddate());
                    if (date!= null)
                    {
                        holder.JobsDate.setText(date);
                    }
                    String valid = AppUtils.checkdatevalidity(postListFiltered.get(position).getLastdate().getValiddate());
                    if (valid=="green")
                    {
                        holder.JobsDate.setTextColor(Color.GREEN);
                    }
                    else if (valid == "yellow")
                    {
                        holder.JobsDate.setTextColor(mContext.getResources().getColor(R.color.yellow));
                    }
                    else if (valid == "red")
                    {
                        holder.JobsDate.setTextColor(Color.RED);
                    }
                    else
                    {
                        holder.JobsDate.setTextColor(mContext.getResources().getColor(R.color.darkblue));
                    }
                }
                else
                {
                    holder.LastDateLayout.setVisibility(View.GONE);
                    holder.JobsTitle.setLayoutParams(param);
                }
            }
            else
            {
                holder.LastDateLayout.setVisibility(View.GONE);
                holder.JobsTitle.setLayoutParams(param);
            }
        }
        else
        {
            holder.LastDateLayout.setVisibility(View.GONE);
            holder.JobsTitle.setLayoutParams(param);
        }


        holder.Whatsapp.setOnClickListener(new View.OnClickListener() {
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

        holder.Telegram.setOnClickListener(new View.OnClickListener() {
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

        if (AppUtils.CheckSaveJobs(mContext,post.getLink()))
        {
            Glide.with(mContext)
                    .load(R.drawable.ic_star)
                    .into(holder.SaveJob);
        }
        else
        {
            Glide.with(mContext)
                    .load(R.drawable.ic_un_star)
                    .into(holder.SaveJob);
        }

        holder.SaveJob.setOnClickListener(new View.OnClickListener() {
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
                notifyItemChanged(position);
            }
        });
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webSingle = new Intent(mContext, WebViewActivity.class);
                webSingle.putExtra(AppConstant.JOBTITLE, postList.get(position).getTitle().getRendered());
                webSingle.putExtra(AppConstant.JOBURL, postList.get(position).getLink());
                webSingle.putExtra(AppConstant.JOBCONTENT, postList.get(position).getContent().getRendered());
                if (post.getLastdate()!=null)
                {
                    if (post.getLastdate().getValiddate() != null) {
                        webSingle.putExtra(AppConstant.JOBLASTDATE, postList.get(position).getLastdate().getValiddate());
                    }
                }


                if (AppPreference.getInstance(mContext).getClicks() >= 5)
                {
                    if (AdUtils.getInstance(mContext).showFullScreenAd()) {
                        AdUtils.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                AppPreference.getInstance(mContext).setClicks(0);
                                mContext.startActivity(webSingle);
                            }
                        });
                    } else {
                        mContext.startActivity(webSingle);
                        AppPreference.getInstance(mContext).setClicks(0);
                    }
                }
                else
                {
                    int currentval = AppPreference.getInstance(mContext).getClicks();
                    int updateval = currentval+1;
                    AppPreference.getInstance(mContext).setClicks(updateval);
                    mContext.startActivity(webSingle);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                List<Post> filterposts = new ArrayList<>();
                String query = charSequence.toString().toLowerCase();

                if (query.length() == 0)
                {
                    postListFiltered.addAll(postList);
                }
                for (Post post : postListFiltered)
                {
                    if (post.getTitle().getRendered().toLowerCase().trim().contains(query))
                    {
                        filterposts.add(post);
                    }
                }
                results.count = filterposts.size();
                results.values = filterposts;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                postListFiltered = (List<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView JobsTitle;
        private final TextView JobsDate;
        private final TextView JobsMonth;
        private final TextView JobsLastDateText;
        private final ImageView Whatsapp;
        private final ImageView Telegram;
        private final ImageView SaveJob;
        private final CardView mCardView;
        private final LinearLayout LastDateLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            JobsTitle = itemView.findViewById(R.id.title_text);
            JobsDate = itemView.findViewById(R.id.post_last_date);
            JobsMonth = itemView.findViewById(R.id.post_last_month);
            JobsLastDateText = itemView.findViewById(R.id.post_last_date_text);
            Whatsapp = itemView.findViewById(R.id.job_post_whatsapp);
            Telegram = itemView.findViewById(R.id.job_post_telegram);
            SaveJob = itemView.findViewById(R.id.job_post_save);
            mCardView = itemView.findViewById(R.id.card_view_top);
            LastDateLayout = itemView.findViewById(R.id.last_date_layout);
        }
    }

}