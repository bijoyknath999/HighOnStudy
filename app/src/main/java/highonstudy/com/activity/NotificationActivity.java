package highonstudy.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import highonstudy.com.R;
import highonstudy.com.adapters.NotificationAdapter;
import highonstudy.com.data.sqlite.NotificationDbController;
import highonstudy.com.data.sqlite.notifymodel;
import highonstudy.com.listeners.ListItemClickListener;
import highonstudy.com.utility.AppUtils;
import highonstudy.com.utility.DialogUtils;

public class NotificationActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RecyclerView rvNotification;
    private NotificationAdapter mAdapter;
    private ArrayList<notifymodel> notificationList;

    private NotificationDbController notificationController;
    private static final String READ = "read", UNREAD = "unread";
    private LinearLayout loadingView, noDataView;
    private Toolbar mToolbar;
    private MenuItem menuItemClearAll;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = NotificationActivity.this;
        mContext = mActivity.getApplicationContext();

        initVars();
        initialView();
        initialListener();

    }

    private void initVars() {
        notificationList = new ArrayList<>();
        notificationController = new NotificationDbController(mContext);
    }

    private void initialView() {
        setContentView(R.layout.activity_notification);

        //productList
        rvNotification = findViewById(R.id.recycler_view);

        mAdapter = new NotificationAdapter(mActivity, notificationList);
        rvNotification.setLayoutManager(new LinearLayoutManager(mActivity));
        rvNotification.setAdapter(mAdapter);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.notifications));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);

        initLoader();

    }

    public void initLoader() {
        loadingView = findViewById(R.id.loadingView);
        noDataView = findViewById(R.id.noDataView);
    }

    public void showLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.GONE);
        }
    }

    public void showEmptyView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }


    private void initialListener() {

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(final int position, View view) {
                notificationController.updateStatus(notificationList.get(position).getId(), true);
                switch (view.getId()) {
                    case R.id.ivRemoveNotification:
                        // Remove notification from list
                        DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_notify_msg), getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {
                                notificationController.deleteNotification(String.valueOf(notificationList.get(position).getId()),mContext);
                                loadNotifications();
                            }
                        });
                        break;

                    default:
                        AppUtils.getInstance().invokeGoWeb(mActivity, WebViewActivity.class, notificationList.get(position).getTitle(),notificationList.get(position).getUrl(),false);
                        break;
                }
            }
        });
    }

    private void loadNotifications(){

        showLoader();
        if (!notificationList.isEmpty()){
            notificationList.clear();
        }
        notificationList.addAll(notificationController.getAllData());
        hideLoader();
        mAdapter.notifyDataSetChanged();

        if (notificationList.isEmpty()) {
            showEmptyView();
            if (menuItemClearAll!=null) {
                menuItemClearAll.setVisible(false);
            }
        }
        else
        {
            if (menuItemClearAll!=null)
            {
                menuItemClearAll.setVisible(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.menus_clear_all:
                DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_all_notify_msg), getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                    @Override
                    public void onPositiveClick() {
                        if (notificationList.size()> 0) {
                            notificationController.deleteAllNotification();
                        }
                        else {
                            Toast.makeText(mContext, R.string.empty_list, Toast.LENGTH_SHORT).show();
                        }
                        loadNotifications();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clear_all, menu);
        menuItemClearAll = menu.findItem(R.id.menus_clear_all);
        loadNotifications();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            loadNotifications();
        }
    }

}
