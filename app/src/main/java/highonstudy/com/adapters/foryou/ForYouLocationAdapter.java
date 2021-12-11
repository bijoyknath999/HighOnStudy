package highonstudy.com.adapters.foryou;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import highonstudy.com.R;
import highonstudy.com.activity.UniversalActivity;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.models.ForYouLocationItems;
import highonstudy.com.models.JobSlider_Item;
import highonstudy.com.data.constant.AppConstant;

public class ForYouLocationAdapter extends RecyclerView.Adapter<ForYouLocationAdapter.MyViewHolder> {

    private final List<ForYouLocationItems> forYouLocationItems;
    private final LayoutInflater inflater;
    private final Context mContext;

    public ForYouLocationAdapter(Context context, List<ForYouLocationItems> forYouLocationItems) {
        this.mContext = context;
        this.forYouLocationItems = forYouLocationItems;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ForYouLocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_you_location, parent, false);
        return new ForYouLocationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouLocationAdapter.MyViewHolder holder, int position) {
        final ForYouLocationItems item = forYouLocationItems.get(position);


        if (item.getName()!=null)
        {
            String titleText = item.getName();
            holder.PostTitleTextView.setText(Html.fromHtml(titleText));
        }
        else
        {
            holder.PostTitleTextView.setText("Null");
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webSingle = new Intent(mContext
                        , WebViewActivity.class);
                webSingle.putExtra(AppConstant.JOBTITLE, forYouLocationItems.get(position).getName());
                webSingle.putExtra(AppConstant.JOBURL, forYouLocationItems.get(position).getLink());
                mContext.startActivity(webSingle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forYouLocationItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView PostTitleTextView;
        private final CardView mCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PostTitleTextView = itemView.findViewById(R.id.for_you_location_title);
            mCardView = itemView.findViewById(R.id.card_view_for_you_location);
        }
    }
}