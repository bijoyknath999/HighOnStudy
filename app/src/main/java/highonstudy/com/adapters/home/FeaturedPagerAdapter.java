package highonstudy.com.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;
import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.constant.AppConstant;

import java.util.List;


public class FeaturedPagerAdapter extends CardSliderAdapter<FeaturedPagerAdapter.FViewHolder>{

    private final List<Post> postList;
    private final LayoutInflater inflater;
    private final Context mContext;

    public FeaturedPagerAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.postList = postList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindVH(FeaturedPagerAdapter.FViewHolder viewHolder, int i) {

        Post post = postList.get(i);

        String[] key1 = new String[8];
        key1[0] = "Recruitment";
        key1[1] = "Recruitment ";
        key1[2] = "Recruitment";
        key1[3] = "Recruitment";
        key1[4] = "Recruitment";
        key1[5] = "Recruitment";
        key1[6] = "Recruitment";
        key1[7] = "Recruitment";

        String[] key2 = new String[8];
        key2[0] = "Apprentice Vacancies";
        key2[1] = "Posts";
        key2[2] = "Posts";
        key2[3] = "Posts";
        key2[4] = "Posts";
        key2[5] = "Posts";
        key2[6] = "Posts";
        key2[7] = "Posts";

        if (post.getShort()!=null && post.getJobs()!=null && post.getLastdate()!=null)
        {
            if (post.getShort().getShort()!=null && post.getLastdate().getValiddate()!=null && post.getJobs().getNumber()!=null)
            {
                String shorts = post.getShort().getShort().toString();
                String text1 = key1[i];
                String text2 = key2[i];
                String jobsno = post.getJobs().getNumber();

                String Lastdate = post.getLastdate().getValiddate().toString();
                String year = Lastdate.substring(Math.max(Lastdate.length() - 4, 0));

                String title = shorts+" "+text1+" "+year+" "+jobsno+" "+text2;

                viewHolder.textTitle.setText(Html.fromHtml(title));
            }
        }

        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webSingle = new Intent(mContext
                        , WebViewActivity.class);
                webSingle.putExtra(AppConstant.JOBTITLE, postList.get(i).getTitle().getRendered());
                webSingle.putExtra(AppConstant.JOBURL, postList.get(i).getLink());
                mContext.startActivity(webSingle);
            }
        });


        int[] images = new int[8];
        images[0] = R.drawable.slider1;
        images[1] = R.drawable.slider2;
        images[2] = R.drawable.slider3;
        images[3] = R.drawable.slider4;
        images[4] = R.drawable.slider5;
        images[5] = R.drawable.slider6;
        images[6] = R.drawable.slider7;
        images[7] = R.drawable.slider8;

        viewHolder.Layout1.setBackgroundDrawable( mContext.getResources().getDrawable(images[i]) );
    }

    @NonNull
    @Override
    public FeaturedPagerAdapter.FViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_screen_view_pager, parent, false);
        return new FViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class FViewHolder extends RecyclerView.ViewHolder{

        private final CardView mCardView;

        private final TextView textTitle;
        private final RelativeLayout Layout1;

        public FViewHolder(View view){
            super(view);
            textTitle = view.findViewById(R.id.recent_post_title);
            Layout1 = view.findViewById(R.id.home_screen_view_pager_layout);
            mCardView = itemView.findViewById(R.id.card_view_top);
        }
    }
}