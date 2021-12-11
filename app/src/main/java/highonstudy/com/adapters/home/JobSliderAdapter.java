package highonstudy.com.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import highonstudy.com.R;
import highonstudy.com.activity.UniversalActivity;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.api.models.topstories.Image;
import highonstudy.com.models.JobSlider_Item;
import highonstudy.com.data.constant.AppConstant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobSliderAdapter extends RecyclerView.Adapter<JobSliderAdapter.MyViewHolder> {

    private final List<JobSlider_Item> jobslider_items;
    private final LayoutInflater inflater;
    private final Context mContext;

    public JobSliderAdapter(Context context, List<JobSlider_Item> jobslider_itemList) {
        this.mContext = context;
        this.jobslider_items = jobslider_itemList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public JobSliderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        return new JobSliderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobSliderAdapter.MyViewHolder holder, int position) {
        final JobSlider_Item item = jobslider_items.get(position);


        if (item.getName()!=null)
        {
            String titleText = item.getName();
            holder.mFeaturedPostTitleTextView.setText(Html.fromHtml(titleText));
        }
        else
        {
            holder.mFeaturedPostTitleTextView.setText("Null");
        }


        String[] images = new String[9];
        images[0] = "#FF512F";
        images[1] = "#4776E6";
        images[2] = "#B24592";
        images[3] = "#673AB7";
        images[4] = "#EDE574";
        images[5] = "#AA076B";
        images[6] = "#FF512F";
        images[7] = "#1FA2FF";
        images[8] = "#f857a6";

        int[] icons = new int[9];
        icons[0] = R.drawable.railway;
        icons[1] = R.drawable.court_logo;
        icons[2] = R.drawable.bank_logo;
        icons[3] = R.drawable.defence_jobs;
        icons[4] = R.drawable.psu_logo;
        icons[5] = R.drawable.esic_logo;
        icons[6] = R.drawable.india_post_logo;
        icons[7] = R.drawable.nhm_logo;
        icons[8] = R.drawable.ssc_logo;


        Glide.with(mContext)
                .load(icons[position])
                .into(holder.mFeaturedJobsImage);

        holder.materialRippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobslider_items.get(position).getCatID().equals("bank"))
                {
                    Intent webSingle = new Intent(mContext, WebViewActivity.class);
                    webSingle.putExtra(AppConstant.JOBTITLE, "Bank");
                    webSingle.putExtra(AppConstant.JOBURL, "https://www.highonstudy.com/bank/");
                    mContext.startActivity(webSingle);
                }
                else
                {
                    Intent slidejonIntent = new Intent(mContext, UniversalActivity.class);
                    slidejonIntent.putExtra(AppConstant.JOBCATID, jobslider_items.get(position).getCatID());
                    slidejonIntent.putExtra(AppConstant.JOBCATNAME, "ExploreSlider");
                    mContext.startActivity(slidejonIntent);
                }
            }
        });

        //holder.mFeaturedJobsImage.setColorFilter(Color.parseColor(images[position]), PorterDuff.Mode.SRC_IN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        }

    }

    @Override
    public int getItemCount() {
        return jobslider_items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView mFeaturedPostTitleTextView;
        private final CircleImageView mFeaturedJobsImage;
        private CircleImageView LayoutColor;
        private final MaterialRippleLayout materialRippleLayout;
        private final ImageView ImageICon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mFeaturedPostTitleTextView = itemView.findViewById(R.id.explore_title);
            mFeaturedJobsImage = itemView.findViewById(R.id.explore_icon_bg);
            materialRippleLayout = itemView.findViewById(R.id.lyt_parent);
            ImageICon = itemView.findViewById(R.id.explore_icon);
        }
    }
}