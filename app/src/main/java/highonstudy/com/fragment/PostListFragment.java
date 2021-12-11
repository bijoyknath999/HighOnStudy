package highonstudy.com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import highonstudy.com.R;
import highonstudy.com.adapters.JobsAdapter;
import highonstudy.com.api.http.ApiUtils;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.constant.AppConstant;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostListFragment extends Fragment {

    private int selectedCategoryId;
    private String searchText = "";
    private ImageView imgFeatured;
    private TextView tvRecentPostTitle;
    private LinearLayout loadingView, noDataView;
    private SpinKitView rlTopPost;
    private RecyclerView rvPosts;
    private JobsAdapter jobsAdapter;

    private List<Post> postList;
    private final Post firstPost = null;
    int pageCount = 1;
    int view_hold = 10;
    boolean isloading = true;

    private SpinKitView bottomLayout;
    private GridLayoutManager mLayoutManager;
    private boolean userScrolled = true;
    int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;


    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jobs_post_list, container, false);

    return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jobsAdapter = new JobsAdapter(getActivity(), postList);

        setHasOptionsMenu(true);
        initVar();
        initView(view);
        initLoader(view);
        showLoader();
        loadSearchedPosts(searchText);
        jobsAdapter.notifyDataSetChanged();


        implementScrollListener();
    }

    public void initVar() {

        postList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategoryId = getArguments().getInt(AppConstant.BUNDLE_KEY_CATEGORY_ID);
        }
    }

    public void initView(View rootView) {
        tvRecentPostTitle = rootView.findViewById(R.id.recent_post_title);

        bottomLayout = rootView.findViewById(R.id.rv_itemload);

        loadingView = rootView.findViewById(R.id.loadingView);
        noDataView = rootView.findViewById(R.id.noDataView);

        rvPosts = rootView.findViewById(R.id.rvPosts);
        mLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rvPosts.setLayoutManager(mLayoutManager);


    }

    public void initLoader(View rootView) {
        loadingView = rootView.findViewById(R.id.loadingView);
        noDataView = rootView.findViewById(R.id.noDataView);
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

    public void loadSearchedPosts(String searchText) {
        ApiUtils.getApiInterface().getSearchedPosts(selectedCategoryId,pageCount, searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    postList.addAll(response.body());
                    jobsAdapter = new JobsAdapter(getContext(), postList);
                    rvPosts.setAdapter(jobsAdapter);
                    jobsAdapter.notifyDataSetChanged();
                }

                hideLoader();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                showEmptyView();
            }
        });
    }


    // Implement scroll listener
    private void implementScrollListener() {
        rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {


                super.onScrollStateChanged(recyclerView, newState);

                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,
                                   int dy) {

                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled && (visibleItemCount + pastVisibleItems) == totalItemCount) {
                    userScrolled = false;
                    //updateRecyclerView();
                }

            }

        });

    }

    // Method for repopulating recycler view
    private void updateRecyclerView() {

        // Show Progress Layout
        bottomLayout.setVisibility(View.VISIBLE);

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {


                pageCount++;

                loadSearchedPosts(searchText);




                // Toast for task completion
                //Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();

                // After adding new data hide the view.
                bottomLayout.setVisibility(View.GONE);
                userScrolled = true;
            }
        }, 5000);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setImageResource(R.drawable.ic_search_24);

        searchView.setQueryHint(getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showLoader();
                searchText = newText;
                loadSearchedPosts(searchText);
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onResume() {
        loadSearchedPosts(searchText);
        super.onResume();
    }

    @Override
    public void onStart() {
        loadSearchedPosts(searchText);
        super.onStart();
    }
}
