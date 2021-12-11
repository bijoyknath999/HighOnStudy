package highonstudy.com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


public class UniversalListFragment extends Fragment {

    private String selectedCategoryId,selectedCategoryName;
    private String searchText = "";
    private ImageView imgFeatured;
    private TextView tvRecentPostTitle;
    private LinearLayout loadingView, noDataView;
    private SpinKitView rlTopPost;
    private RecyclerView rvPosts;
    private JobsAdapter jobsAdapter = null;

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

        jobsAdapter = new JobsAdapter(getActivity(), postList);

        initVar();
        initView(rootView);
        initLoader(rootView);
        showLoader();
        initFunctionality(false);
        implementScrollListener();

    return rootView;
    }


    public void initVar() {

        postList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategoryId = getArguments().getString(AppConstant.BUNDLE_KEY_CATEGORY_ID);
            searchText = getArguments().getString(AppConstant.BUNDLE_KEY_SEARCH_TEXT);
            selectedCategoryName = getArguments().getString(AppConstant.BUNDLE_KEY_CATEGORY_NAME);
        }
    }

    public void initFunctionality(boolean Scrolled) {
        if (selectedCategoryName.equals("Tags"))
        {
            loadTags(Scrolled);
        }
        else if (selectedCategoryName.equals("ExploreSlider"))
        {
            if (selectedCategoryId.equals("psu"))
            {
                loadPSU(Scrolled);
            }
            else if (selectedCategoryId != "psu" || selectedCategoryId != "bank")
            {
                loadother(Scrolled);
            }
        }
        else if (selectedCategoryName.equals("other"))
        {
            loadother(Scrolled);
        }
        else if (selectedCategoryName.equals("exam"))
        {
            loadExam(Scrolled);
        }
    }

    private void loadother(boolean Scrolled)
    {
        ApiUtils.getApiInterface().getSearchedPosts(Integer.parseInt(selectedCategoryId),pageCount,searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    if (!Scrolled)
                    {
                        if (!postList.isEmpty())
                        {
                            postList.clear();
                        }
                    }
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


    private void loadExam(boolean Scrolled)
    {
        ApiUtils.getApiInterface().getSearchEXAM(pageCount,searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    if (!Scrolled)
                    {
                        if (!postList.isEmpty())
                        {
                            postList.clear();
                        }
                    }
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

    private void loadTags(boolean Scrolled) {

        ApiUtils.getApiInterface().getSearchTags(Integer.parseInt(selectedCategoryId),pageCount,searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    if (!Scrolled)
                    {
                        if (!postList.isEmpty())
                        {
                            postList.clear();
                        }
                    }
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

    private void loadPSU(boolean Scrolled)
    {
        ApiUtils.getApiInterface().getSearchPSU(pageCount, searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    if (!Scrolled)
                    {
                        if (!postList.isEmpty())
                        {
                            postList.clear();
                        }
                    }

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
        ApiUtils.getApiInterface().getSearchedPosts(Integer.parseInt(selectedCategoryId),pageCount, searchText).enqueue(new Callback<List<Post>>() {
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
                    updateRecyclerView();
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

                initFunctionality(true);


                // Toast for task completion
                //Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();

                // After adding new data hide the view.
                bottomLayout.setVisibility(View.GONE);
                userScrolled = true;
            }
        }, 5000);

    }
}
