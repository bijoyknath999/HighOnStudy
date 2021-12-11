package highonstudy.com.adapters.foryou;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;
import highonstudy.com.R;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.AppPreference;
import highonstudy.com.listeners.ListItemClickListener;

public class ForYouTopStoriesAdapter extends PagerAdapter {
    private final List<Post> postList;
    private final LayoutInflater inflater;
    private final Context mContext;
    private TextView PostTitleTextView;
    private TextView PostTitleTextViewShort;
    private CircularProgressBar circularProgressBar;

        // Listener
        private ListItemClickListener mListener;

        public ForYouTopStoriesAdapter(Context context, List<Post> postList) {
            this.mContext = context;
            this.postList = postList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return postList.size();
        }

    @Override
    public float getPageWidth(int position) {
        return 0.23f;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
        public Object instantiateItem(final ViewGroup view, final int position) {

            View rootView = inflater.inflate(R.layout.item_for_you_top_stories, view, false);

            PostTitleTextView = rootView.findViewById(R.id.for_you_top_stories_title);
            PostTitleTextViewShort = rootView.findViewById(R.id.for_you_top_stories_title_short);
            RelativeLayout ArrowLayout = rootView.findViewById(R.id.for_you_top_stories_arrow_layout);
            RelativeLayout  ClickLayout = rootView.findViewById(R.id.for_you_top_stories_click_layout);




        circularProgressBar = rootView.findViewById(R.id.circularProgressBar);
        if (AppPreference.getInstance(mContext).getItem() == position)
        {
            circularProgressBar.setVisibility(View.VISIBLE);
            ArrowLayout.setVisibility(View.VISIBLE);
            circularProgressBar.setProgressMax(100f);
            circularProgressBar.setProgressWithAnimation(100f, Long.valueOf(5000));

        }
        final Post post = postList.get(position);

            String titleText = null;
            if (post.getShort()!=null)
            {
                if (post.getShort().getShort() != null)
                {
                    titleText = post.getShort().getShort();
                }
            }
            if (titleText!= null)
            {
                String finaltext = titleText.substring(0, 1).toUpperCase();
                PostTitleTextView.setText(titleText);
                PostTitleTextViewShort.setText(finaltext);
            }
            else
            {
                PostTitleTextViewShort.setText("");
                PostTitleTextView.setText("");
            }

        ClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position, view);
                }
            }
        });


            view.addView(rootView);


            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        public void setItemClickListener(ListItemClickListener mListener) {
            this.mListener = mListener;
        }
}
