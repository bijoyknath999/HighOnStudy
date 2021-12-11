package highonstudy.com.adapters.foryou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import highonstudy.com.R;
import highonstudy.com.activity.UniversalActivity;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.models.ForYouEducationItems;
import highonstudy.com.models.ForYouLocationItems;

public class ForYouEducationAdapter extends RecyclerView.Adapter<ForYouEducationAdapter.MyViewHolder> {

    private final List<ForYouEducationItems> forYouEducationItems;
    private final LayoutInflater inflater;
    private final Context mContext;

    public ForYouEducationAdapter(Context context, List<ForYouEducationItems> forYouEducationItems) {
        this.mContext = context;
        this.forYouEducationItems = forYouEducationItems;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ForYouEducationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_you_education, parent, false);
        return new ForYouEducationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouEducationAdapter.MyViewHolder holder, int position) {
        final ForYouEducationItems item = forYouEducationItems.get(position);


        if (item.getName()!=null)
        {
            String titleText = item.getName();
            holder.PostTitleTextView.setText(Html.fromHtml(titleText));
        }
        else
        {
            holder.PostTitleTextView.setText("Null");
        }

        int[] colors = new int[6];
        colors[0] = R.color.colorview1;
        colors[1] = R.color.colorview1;
        colors[2] = R.color.colorview2;
        colors[3] = R.color.colorview2;
        colors[4] = R.color.colorview3;
        colors[5] = R.color.colorview3;

        holder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(colors[position]));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UniversalActivity.class);
                intent.putExtra(AppConstant.JOBCATID, item.getID());
                intent.putExtra(AppConstant.JOBCATNAME, "Tags");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forYouEducationItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView PostTitleTextView;
        private final CardView mCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PostTitleTextView = itemView.findViewById(R.id.for_you_education_title);
            mCardView = itemView.findViewById(R.id.card_view_for_you_education);
        }
    }
}