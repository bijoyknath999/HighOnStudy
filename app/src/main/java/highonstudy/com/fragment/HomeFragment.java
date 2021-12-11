package highonstudy.com.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import highonstudy.com.R;
import highonstudy.com.adapters.home.FeaturedPagerAdapter;
import highonstudy.com.adapters.home.GridAdapter;
import highonstudy.com.adapters.home.GridJobByLAdapter;
import highonstudy.com.adapters.home.GridJobPublicSCAdapter;
import highonstudy.com.adapters.home.JobSliderAdapter;
import highonstudy.com.api.http.ApiUtils;
import highonstudy.com.api.models.Post;
import highonstudy.com.api.params.HttpParams;
import highonstudy.com.models.grid_items;
import highonstudy.com.models.gridjobByL_items;
import highonstudy.com.models.JobSlider_Item;
import highonstudy.com.data.constant.AllArray;
import highonstudy.com.data.constant.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener{

    private RecyclerView Explore_recycler_view;
    private ProgressBar progressBar;
    private View view;
    private RecyclerView rvMenus;
    private ScrollView lytContent;
    private List<Post> featuredPostList;
    private List<Post> trendingPostList;
    private RelativeLayout mFeaturedLayout;
    private FeaturedPagerAdapter featuredPostAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<grid_items> grid_itemsList;
    private RecyclerView JobBQualificationRecyclerView;
    private GridAdapter gridAdapter = null;
    private GridLayoutManager mLayoutManager,gridLayoutManager;
    private LinearLayoutManager layoutManager;
    private ProgressBar pbSectionLoader, pbSelectableCatLoader;
    private AdView mAdView;
    private List<gridjobByL_items> gridjobByL_items, gridPSC_items;
    private GridJobByLAdapter jobByLAdapter;
    private RecyclerView jobByLRecyclerview;
    private CardSliderViewPager sliderpager;
    private LinearLayout loadingView, noDataView;
    private List<JobSlider_Item> jobslider_items;
    private JobSliderAdapter jobSliderAdapter;
    private Timer timer2;
    private Timer timer;
    private WebView SpecialtextWeb;
    private Context mContext;
    private GridJobPublicSCAdapter gridJobPublicSCAdapter;
    private RecyclerView PCSRecyclerView;
    private List<String> PSCnames,PSClinks,ExploreTitle,ExploreID,JobByQ_Title,JobByQ_ID,JobByL_Title,JobByL_Link;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        featuredPostList = new ArrayList<>();
        trendingPostList = new ArrayList<>();
        grid_itemsList = new ArrayList<>();
        jobslider_items = new ArrayList<>();
        gridjobByL_items = new ArrayList<>();
        gridPSC_items = new ArrayList<>();


        mContext = getContext();
        PSCnames = AllArray.getpublic_service_comission_name(mContext);
        PSClinks = AllArray.getpublic_service_comission_link(mContext);

        ExploreTitle = AllArray.getexploreslidertitle(mContext);
        ExploreID = AllArray.getexploresliderid(mContext);

        JobByQ_Title = AllArray.getjobbyQ_title(mContext);
        JobByQ_ID = AllArray.getjobbyQ_ID(mContext);

        JobByL_Title = AllArray.getjobbyL_title(mContext);
        JobByL_Link = AllArray.getjobbyL_LINK(mContext);


        setHasOptionsMenu(true);
        initlay(view);
        initLoader(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoader();
        loadFeaturedPosts();
        loadTrendingArticle();
        loadJobSlider();
        loadgridjobbyQualification();
        loadgridjobbylocation();
        loadgridPublicSC();
        initListener();

    }

    private void initlay(View view) {

        SpecialtextWeb = view.findViewById(R.id.special_data_web);
        sliderpager = view.findViewById(R.id.home_slider);

        lytContent = view.findViewById(R.id.content_layout);
        rvMenus = view.findViewById(R.id.rvMenus);
        rvMenus.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mFeaturedLayout = view.findViewById(R.id.lytPagerContainer);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        timer2 = new Timer();
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new SliderTimer(), 1000, 2000);
    }

    public void loadFeaturedPosts() {

        ApiUtils.getApiInterface().getFeaturedPosts(AppConstant.DEFAULT_PAGE,214).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                if (response.isSuccessful()) {

                    lytContent.setVisibility(View.VISIBLE);

                    if (!featuredPostList.isEmpty()) {
                        featuredPostList.clear();
                    }
                    featuredPostList.addAll(response.body());

                    if (featuredPostList.size() > 0) {
                        mFeaturedLayout.setVisibility(View.VISIBLE);
                    }

                    if (featuredPostList.get(0).getTopnews()!=null)
                    {
                        if (featuredPostList.get(0).getTopnews().getSpecial()!=null)
                        {
                            SpecialtextWeb.setVisibility(View.VISIBLE);
                            SpecialtextWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                            SpecialtextWeb.setScrollbarFadingEnabled(false);
                            SpecialtextWeb.setPadding(0, 0, 0, 0);
                            SpecialtextWeb.setWebChromeClient(new WebChromeClient());
                            SpecialtextWeb.setVerticalScrollBarEnabled(false);
                            SpecialtextWeb.setHorizontalScrollBarEnabled(false);
                            SpecialtextWeb.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
                                }
                            });
                            SpecialtextWeb.loadDataWithBaseURL("", featuredPostList.get(0).getTopnews().getSpecial(), "text/html", "UTF-8", "");
                        }
                    }


                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
                hideLoader();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showEmptyView();
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public void loadTrendingArticle(){

        ApiUtils.getApiInterface().getFeaturedPosts(AppConstant.DEFAULT_PAGE,972).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {


                if (response.isSuccessful()) {

                    lytContent.setVisibility(View.VISIBLE);

                    if (!trendingPostList.isEmpty()) {
                        trendingPostList.clear();
                    }
                    trendingPostList.addAll(response.body());

                    if (trendingPostList.size() > 0) {
                        mFeaturedLayout.setVisibility(View.VISIBLE);
                    }


                    // featured post pager adapter
                    featuredPostAdapter = new FeaturedPagerAdapter(getContext(), trendingPostList);
                    sliderpager.setAdapter(featuredPostAdapter);
                    sliderpager.setCurrentItem(0);

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
                hideLoader();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                showEmptyView();
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
        // featured post pager adapter
        featuredPostAdapter = new FeaturedPagerAdapter(getContext(), featuredPostList);
        sliderpager.setAdapter(featuredPostAdapter);
        sliderpager.setCurrentItem(0);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoader();
                lytContent.setVisibility(View.GONE);
                loadFeaturedPosts();
                loadTrendingArticle();
                loadJobSlider();
                loadgridjobbylocation();
                loadgridjobbyQualification();
                loadgridPublicSC();
                lytContent.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    private void loadJobSlider() {

        Explore_recycler_view = view.findViewById(R.id.explore_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        Explore_recycler_view.setLayoutManager(layoutManager);
        jobSliderAdapter = new JobSliderAdapter(getActivity(),jobslider_items);
        Explore_recycler_view.setAdapter(jobSliderAdapter);
        jobSliderAdapter.notifyDataSetChanged();

        if (jobslider_items!=null)
        {
            jobslider_items.clear();
        }
        for (int i = 0; i < ExploreTitle.size(); i++) {
            JobSlider_Item jobslider_item = new JobSlider_Item(ExploreTitle.get(i), ExploreID.get(i));
            jobslider_items.add(jobslider_item);
        }

    }

    private void loadgridjobbyQualification() {


        JobBQualificationRecyclerView = view.findViewById(R.id.rvPosts);
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        JobBQualificationRecyclerView.setLayoutManager(mLayoutManager);

        if (grid_itemsList!=null)
        {
            grid_itemsList.clear();
        }
        for (int i=0; i< JobByQ_Title.size(); i++)
        {
            grid_items gridItems = new grid_items(JobByQ_Title.get(i),JobByQ_ID.get(i));
            grid_itemsList.add(gridItems);
        }

        gridAdapter = new GridAdapter(getActivity(),grid_itemsList);
        JobBQualificationRecyclerView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        JobBQualificationRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                enableDisableSwipeRefresh(newState == RecyclerView.SCROLL_STATE_IDLE);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void loadgridjobbylocation() {

        jobByLRecyclerview = view.findViewById(R.id.rvPosts2);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        jobByLRecyclerview.setLayoutManager(gridLayoutManager);

        if (gridjobByL_items!=null)
        {
            gridjobByL_items.clear();
        }

        for (int i=0; i< JobByL_Title.size(); i++)
        {
            gridjobByL_items gridjobByLItems = new gridjobByL_items(JobByL_Title.get(i),JobByL_Link.get(i));
            gridjobByL_items.add(gridjobByLItems);
        }

        // recent post adapter
        jobByLAdapter = new GridJobByLAdapter(getActivity(),gridjobByL_items);
        jobByLRecyclerview.setAdapter(jobByLAdapter);
        jobByLAdapter.notifyDataSetChanged();
        jobByLRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                enableDisableSwipeRefresh(newState == RecyclerView.SCROLL_STATE_IDLE);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadgridPublicSC() {

        PCSRecyclerView = view.findViewById(R.id.rvPosts3);
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        PCSRecyclerView.setLayoutManager(mLayoutManager);


        if (gridPSC_items != null) {
            gridPSC_items.clear();
        }

        for (int i = 0; i < PSCnames.size(); i++) {
            gridjobByL_items gridjob = new gridjobByL_items(PSCnames.get(i),PSClinks.get(i));
            gridPSC_items.add(gridjob);
        }

        // recent post adapter
        gridJobPublicSCAdapter = new GridJobPublicSCAdapter(getActivity(), gridPSC_items);
        PCSRecyclerView.setAdapter(gridJobPublicSCAdapter);
        gridJobPublicSCAdapter.notifyDataSetChanged();
        PCSRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                enableDisableSwipeRefresh(newState == RecyclerView.SCROLL_STATE_IDLE);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);

    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }

    public void initLoader(View view) {
        loadingView = view.findViewById(R.id.loadingView);
        noDataView = view.findViewById(R.id.noDataView);
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
            lytContent.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        ImageView clseicon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        clseicon.setColorFilter(getResources().getColor(R.color.defaultcolor));

        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_go_btn);
        icon.setColorFilter(getResources().getColor(R.color.defaultcolor));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.defaultcolor));
        searchView.setQueryHint(getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;
            case R.id.action_refresh:
                restartLoader();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class SliderTimer extends TimerTask {
        private SliderTimer() {
        }

        public void run() {
            if (HomeFragment.this.getActivity() != null) {
                HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                    /* class in.gurutricks.sarkarijankari.fragment.FragmentHome.SliderTimer.AnonymousClass1 */

                    public void run() {
                        if (HomeFragment.this.sliderpager.getCurrentItem() < HomeFragment.this.featuredPostList.size() - 1) {
                            HomeFragment.this.sliderpager.setCurrentItem(HomeFragment.this.sliderpager.getCurrentItem() + 1);
                        } else {
                            HomeFragment.this.sliderpager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    private void restartLoader() {
        showLoader();
        lytContent.setVisibility(View.GONE);
        Timer timer2 = timer;
        if (timer2 != null) {
            timer2.cancel();
        }
        Timer timer3 = new Timer();
        timer = timer3;
        timer3.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        sliderpager.setVisibility(View.VISIBLE);
        loadgridjobbylocation();
        loadFeaturedPosts();
        loadTrendingArticle();
        loadJobSlider();
        loadgridjobbyQualification();
        loadgridPublicSC();
        lytContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        timer.cancel();
        if (!featuredPostList.isEmpty())
        {
            featuredPostList.clear();
            featuredPostAdapter.notifyDataSetChanged();
        }

        else if (!gridjobByL_items.isEmpty())
        {
            gridjobByL_items.clear();
            jobByLAdapter.notifyDataSetChanged();
        }
        else if (!grid_itemsList.isEmpty())
        {
            grid_itemsList.clear();
            gridAdapter.notifyDataSetChanged();

        }
        else if (!jobslider_items.isEmpty())
        {
            jobslider_items.clear();
            jobSliderAdapter.notifyDataSetChanged();
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        restartLoader();
    }
}