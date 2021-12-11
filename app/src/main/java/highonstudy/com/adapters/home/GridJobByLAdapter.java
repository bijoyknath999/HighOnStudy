package highonstudy.com.adapters.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.models.gridjobByL_items;
import highonstudy.com.data.constant.AppConstant;

import java.util.List;


public class GridJobByLAdapter extends RecyclerView.Adapter<GridJobByLAdapter.ViewHolder> {

    private final List<gridjobByL_items> grid_items;
    private final Context context;

    public GridJobByLAdapter(Context context, List<gridjobByL_items> grid_itemsList) {
        this.context = context;
        grid_items = grid_itemsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_view, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPostTitle;
        private final CardView mCardView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            // Find all views ids
            tvPostTitle = itemView.findViewById(R.id.title_text);
            mCardView = itemView.findViewById(R.id.card_view_top);
        }
    }

    @Override
    public int getItemCount() {
        return (null != grid_items ? grid_items.size() : 0);

    }

    @Override
    public void onBindViewHolder(GridJobByLAdapter.ViewHolder mainHolder, int position) {
        final gridjobByL_items gridItems = grid_items.get(position);

        // setting data over views
        String title = gridItems.getName();
        mainHolder.tvPostTitle.setText(title);

        mainHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webSingle = new Intent(context
                        , WebViewActivity.class);
                webSingle.putExtra(AppConstant.JOBTITLE, gridItems.getName());
                webSingle.putExtra(AppConstant.JOBURL, gridItems.getLink());
                context.startActivity(webSingle);
            }
        });

    }
}
