package highonstudy.com.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import highonstudy.com.R;
import highonstudy.com.data.sqlite.SavedJObsModel;
import highonstudy.com.data.sqlite.notifymodel;
import highonstudy.com.listeners.ListItemClickListener;

public class SavedJobsAdapter extends RecyclerView.Adapter<SavedJobsAdapter.ViewHolder> {

    private final Context mContext;

    private final ArrayList<SavedJObsModel> dataList;

    // handle interface for item listener
    private ListItemClickListener itemClickListener;

    public SavedJobsAdapter(Context context, ArrayList<SavedJObsModel> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvTitle;
        private final TextView tvSubTitle;
        private final TextView tvLastDate;
        private final ImageView btnDelete;
        private final LinearLayout LastDateLayout;
        private final RelativeLayout lytFavourite;
        // handle interface for item listener
        private final ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, final ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;

            tvTitle = itemView.findViewById(R.id.title_text);
            tvSubTitle = itemView.findViewById(R.id.content_text);
            tvLastDate = itemView.findViewById(R.id.post_last_date);
            LastDateLayout = itemView.findViewById(R.id.last_date_layout);
            lytFavourite = itemView.findViewById(R.id.lyt_favourite);

            btnDelete = itemView.findViewById(R.id.btn_delete);


            lytFavourite.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_jobs_list, parent, false);
        return new ViewHolder(view, viewType, itemClickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = dataList.get(position).getTitle();
        String message = dataList.get(position).getContent();
        String lastsate = dataList.get(position).getLastdate();

        if (title != null) {
            holder.tvTitle.setText(Html.fromHtml(title));
        }
        if (message!= null)
        {
            holder.tvSubTitle.setText(Html.fromHtml(message));
        }
        if (lastsate!= null)
        {
            holder.tvLastDate.setText(lastsate);
        }
        else
        {
            holder.LastDateLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
