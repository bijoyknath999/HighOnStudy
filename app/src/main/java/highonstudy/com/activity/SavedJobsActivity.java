package highonstudy.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import highonstudy.com.adapters.SavedJobsAdapter;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.SavedJObsModel;
import highonstudy.com.data.sqlite.SavedJobsDbController;
import highonstudy.com.listeners.ListItemClickListener;
import highonstudy.com.utility.AppUtils;
import highonstudy.com.utility.DialogUtils;

public class SavedJobsActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RecyclerView rvNotification;
    private SavedJobsAdapter mAdapter;
    private ArrayList<SavedJObsModel> savedJObsModels;

    private SavedJobsDbController savedJobsDbController;
    private static final String READ = "read", UNREAD = "unread";
    private LinearLayout loadingView, noDataView;
    private Toolbar mToolbar;
    private MenuItem menuItemClearAll;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = SavedJobsActivity.this;
        mContext = mActivity.getApplicationContext();

        initVars();
        initialView();
        initialListener();

    }

    private void initVars() {
        savedJObsModels = new ArrayList<>();
        savedJobsDbController = new SavedJobsDbController(mContext);
    }

    private void initialView() {
        setContentView(R.layout.activity_saved_jobs);

        //productList
        rvNotification = findViewById(R.id.recycler_view);

        mAdapter = new SavedJobsAdapter(mActivity, savedJObsModels);
        rvNotification.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new SavedJobsAdapter(SavedJobsActivity.this, savedJObsModels);
        rvNotification.setAdapter(mAdapter);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.saved_jobs));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        initLoader();
        showLoader();

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
                switch (view.getId()) {
                    case R.id.lyt_favourite:
                        AppUtils.getInstance().invokeWebView(mActivity, WebViewActivity.class,
                                savedJObsModels.get(position).getTitle(),
                                savedJObsModels.get(position).getContent(),
                                savedJObsModels.get(position).getUrl(),
                                savedJObsModels.get(position).getLastdate(), false);

                        break;
                    case R.id.btn_delete:
                        DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_saved_item), getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                            @Override
                            public void onPositiveClick() {
                                savedJobsDbController.deleteSavedJobs(savedJObsModels.get(position).getUrl(),mContext);
                                loadNotifications();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void loadNotifications(){

        if (savedJobsDbController == null) {
            savedJobsDbController = new SavedJobsDbController(mContext);
        }
        savedJObsModels.clear();
        savedJObsModels.addAll(savedJobsDbController.getAllSavedJObsData());

        hideLoader();
        mAdapter.notifyDataSetChanged();

        if (savedJObsModels.size() == 0) {
            showEmptyView();
            if (menuItemClearAll != null) {
                menuItemClearAll.setVisible(false);
            }
        } else {
            mAdapter.notifyDataSetChanged();
            if (menuItemClearAll != null) {
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
            case R.id.menus_delete_all:
                DialogUtils.showDialogPrompt(mActivity, null, getString(R.string.delete_all_saved_list), getString(R.string.yes), getString(R.string.no), true, new DialogUtils.DialogActionListener() {
                    @Override
                    public void onPositiveClick() {
                        if (savedJObsModels.size()> 0) {
                            savedJobsDbController.deleteAllSavedJobs();
                            loadNotifications();
                        }
                        else {
                            Toast.makeText(mContext, R.string.empty_list, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_all, menu);
        menuItemClearAll = menu.findItem(R.id.menus_delete_all);
        loadNotifications();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            loadNotifications();
        }
    }
}
