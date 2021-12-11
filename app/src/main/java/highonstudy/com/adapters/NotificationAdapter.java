package highonstudy.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.notifymodel;
import highonstudy.com.listeners.ListItemClickListener;
import highonstudy.com.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context mContext;

    private final ArrayList<notifymodel> dataList;

    // handle interface for item listener
    private ListItemClickListener itemClickListener;

    public NotificationAdapter(Context context, ArrayList<notifymodel> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvTitle;
        private final TextView tvSubTitle;
        private final ImageView ivRemoveNotification;

        // handle interface for item listener
        private final ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, final ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            ivRemoveNotification = itemView.findViewById(R.id.ivRemoveNotification);

            // Listener
            ivRemoveNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getLayoutPosition(), view);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getLayoutPosition(), view);
                    }
                }
            });
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view, viewType, itemClickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = dataList.get(position).getTitle();
        String message = dataList.get(position).getContent();

        if (title != null) {
            if (dataList.get(position).isReadStatus()) {
                holder.tvTitle.setTypeface(null, Typeface.BOLD);
            } else {
                holder.tvTitle.setTypeface(null, Typeface.NORMAL);
            }
            holder.tvTitle.setText(title);
            holder.tvSubTitle.setText(message);
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
