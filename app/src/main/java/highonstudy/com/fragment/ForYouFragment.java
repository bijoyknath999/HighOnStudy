package highonstudy.com.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.github.islamkhsh.CardSliderViewPager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.adapters.foryou.ForYouEducationAdapter;
import highonstudy.com.adapters.foryou.ForYouLocationAdapter;
import highonstudy.com.adapters.foryou.ForYouTopStoriesAdapter;
import highonstudy.com.adapters.foryou.ForYouTopStoriesImageLayoutAdapter;
import highonstudy.com.adapters.foryou.ForYouYoutubeAdapter;
import highonstudy.com.api.http.ApiUtils;
import highonstudy.com.api.models.Post;
import highonstudy.com.api.models.youtube.YoutubeDetails;
import highonstudy.com.data.AppPreference;
import highonstudy.com.data.constant.AllArray;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.listeners.ListItemClickListener;
import highonstudy.com.models.ForYouEducationItems;
import highonstudy.com.models.ForYouLocationItems;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForYouFragment extends Fragment implements SearchView.OnQueryTextListener{
    private View view;
    private Context mContext;
    private RecyclerView LocationRecyclerView;
    private RecyclerView EducationRecyclerView;
    private ForYouLocationAdapter forYouLocationAdapter;
    private ForYouEducationAdapter forYouEducationAdapter;
    private List<ForYouLocationItems> forYouLocationItems;
    private List<ForYouEducationItems> forYouEducationItems;
    private List<String> LocationTitle, LocationLink, EducationTitle, EducationCatID;
    private ScrollView lytContent;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private LinearLayout loadingView, noDataView;
    private GridLayoutManager gridLayoutManager;
    private boolean timerstate;
    private int dotsCount = 0;
    private ViewPager TopStoriesPager;
    private List<Post> postList;
    private ForYouTopStoriesAdapter forYouTopStoriesAdapter;
    private RelativeLayout TopStoriesLayout;

    //Top Stories Image Layout
    private CardSliderViewPager TopStoriesImageLayoutPager;
    private List<Post> postListImage;
    private ForYouTopStoriesImageLayoutAdapter forYouTopStoriesImageLayoutAdapter;
    private RelativeLayout TopStoriesImageLayoutLayout;
    private final boolean scrollplz = false;
    private Timer timer;

    private static final String GOOGLE_YOUTUBE_API_KEY = "AIzaSyDfxAP57xF_xGNDwDH3ZEC-CDaX9L04WvI";//here you should use your api key for testing purpose you can use this api also
    private static final String CHANNEL_ID = "UCLCZBjFU3VsWzj27UtWIlnw"; //here you should use your channel id for testing purpose you can use this ID also
    private static final String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId=" + CHANNEL_ID + "&maxResults=10&key=" + GOOGLE_YOUTUBE_API_KEY + "";


    private RecyclerView YoutubeRecyclerView;
    private ForYouYoutubeAdapter forYouYoutubeAdapter;
    private ArrayList<YoutubeDetails> mListData = new ArrayList<>();
    private LinearLayout YoutubeLayout;
    private GridLayoutManager gridLayoutManageryoutube;
    private LinearLayout YoutubeClick;
    private final int CurrentPosition = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_for_you, container, false);

        forYouLocationItems = new ArrayList<>();
        forYouEducationItems = new ArrayList<>();
        postList = new ArrayList<>();
        postListImage = new ArrayList<>();

        timer = new Timer();

        mContext = getContext();

        LocationTitle = AllArray.getForYouLocationtitle(mContext);
        LocationLink = AllArray.getForYouLocationLINK(mContext);

        EducationTitle = AllArray.getForYouEducationtitle(mContext);
        EducationCatID = AllArray.getForYouEducationCatID(mContext);

        setHasOptionsMenu(true);
        initlay(view);
        initLoader(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoader();
        loadlocation();
        loadEducation();
        loadTopStories(false);
        loadTopStoriesImageLayout();
        initList(mListData);
        new RequestYoutubeAPI().execute();
        initListener();
    }

    private void initlay(View view) {
        lytContent = view.findViewById(R.id.content_layout_for_you);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_for_you);
        TopStoriesPager = view.findViewById(R.id.for_you_top_stories_pager);
        TopStoriesLayout = view.findViewById(R.id.for_you_top_stories_layout);

        TopStoriesImageLayoutPager= view.findViewById(R.id.for_you_top_stories_image_layout_pager);
        TopStoriesImageLayoutLayout = view.findViewById(R.id.for_you_top_stories_image_layout_layout);

        YoutubeRecyclerView = view.findViewById(R.id.for_you_youtube_recyclerview);
        YoutubeLayout = view.findViewById(R.id.for_you_youtube_layout);
        YoutubeClick = view.findViewById(R.id.for_you_youtube_link_click_layout);
        YoutubeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYoutube("UCLCZBjFU3VsWzj27UtWIlnw");
            }
        });
    }

    private void openYoutube(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube.com/channel/" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void loadlocation() {


        LocationRecyclerView = view.findViewById(R.id.for_you_location_recyclerview);
        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        LocationRecyclerView.setLayoutManager(layoutManager);
        forYouLocationAdapter = new ForYouLocationAdapter(getActivity(),forYouLocationItems);
        LocationRecyclerView.setAdapter(forYouLocationAdapter);
        forYouLocationAdapter.notifyDataSetChanged();

        if (forYouLocationItems!=null)
        {
            forYouLocationItems.clear();
        }
        for (int i = 0; i < LocationTitle.size(); i++) {
            ForYouLocationItems forYouLocationItem = new ForYouLocationItems(LocationTitle.get(i), LocationLink.get(i));
            forYouLocationItems.add(forYouLocationItem);
        }

        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadEducation() {


        EducationRecyclerView = view.findViewById(R.id.for_you_education_recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        EducationRecyclerView.setLayoutManager(gridLayoutManager);
        forYouEducationAdapter = new ForYouEducationAdapter(getActivity(),forYouEducationItems);
        EducationRecyclerView.setAdapter(forYouEducationAdapter);
        forYouEducationAdapter.notifyDataSetChanged();

        if (forYouEducationItems!=null)
        {
            forYouEducationItems.clear();
        }


        for (int i = 0; i < EducationTitle.size(); i++) {
            ForYouEducationItems forYouEducationItem = new ForYouEducationItems(EducationTitle.get(i), EducationCatID.get(i));
            forYouEducationItems.add(forYouEducationItem);
        }

        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void loadTopStories(boolean b) {

        ApiUtils.getApiInterface().getTopStories(214,1).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {

                    lytContent.setVisibility(View.VISIBLE);

                    if (!postList.isEmpty()) {
                        postList.clear();
                    }

                    List<Post> posts = new ArrayList<>();
                    posts.addAll(response.body());

                    for (Post post : posts)
                    {
                        if (post.getShort() != null)
                        {
                            postList.add(post);
                        }
                    }

                    if (postList.size() > 0) {
                        TopStoriesLayout.setVisibility(View.VISIBLE);

                    }

                    // featured post pager adapter
                    forYouTopStoriesAdapter = new ForYouTopStoriesAdapter(getContext(), postList);
                    TopStoriesPager.setAdapter(forYouTopStoriesAdapter);
                    TopStoriesPager.setCurrentItem(0);
                    if (!b)
                        AutoScroll();


                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }


                    forYouTopStoriesAdapter.setItemClickListener(new ListItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            TopStoriesImageLayoutPager.setCurrentItem(position);
                        }
                    });
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

    public void loadTopStoriesImageLayout() {

        ApiUtils.getApiInterface().getTopStories(214,1).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {

                    lytContent.setVisibility(View.VISIBLE);

                    if (!postListImage.isEmpty()) {
                        postListImage.clear();
                    }

                    List<Post> posts = new ArrayList<>();
                    posts.addAll(response.body());

                    for (Post post : posts)
                    {
                        if (post.getShort() != null)
                        {
                            postListImage.add(post);
                        }
                    }

                    if (postListImage.size() > 0) {
                        TopStoriesImageLayoutLayout.setVisibility(View.VISIBLE);

                    }

                    // featured post pager adapter
                    forYouTopStoriesImageLayoutAdapter = new ForYouTopStoriesImageLayoutAdapter(getContext(), postListImage);
                    TopStoriesImageLayoutPager.setAdapter(forYouTopStoriesImageLayoutAdapter);
                    TopStoriesImageLayoutPager.setCurrentItem(0);


                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    forYouTopStoriesImageLayoutAdapter.setItemClickListener(new ListItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            Intent webSingle = new Intent(mContext
                                    , WebViewActivity.class);
                            webSingle.putExtra(AppConstant.JOBTITLE, postList.get(position).getTitle().getRendered());
                            webSingle.putExtra(AppConstant.JOBURL, postList.get(position).getLink());
                            webSingle.putExtra(AppConstant.JOBCONTENT, postList.get(position).getContent().getRendered());
                            if (postList.get(position).getLastdate()!= null)
                            {
                                if (postList.get(position).getLastdate().getValiddate() != null)
                                {
                                    webSingle.putExtra(AppConstant.JOBLASTDATE, postList.get(position).getLastdate().getValiddate());
                                }
                            }
                            mContext.startActivity(webSingle);
                        }
                    });
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

    //Start Youtube

    private void initList(ArrayList<YoutubeDetails> mListData) {
        gridLayoutManageryoutube = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        YoutubeRecyclerView.setLayoutManager(gridLayoutManageryoutube);
        forYouYoutubeAdapter = new ForYouYoutubeAdapter(mContext, mListData,this.getLifecycle());
        YoutubeRecyclerView.setAdapter(forYouYoutubeAdapter);

    }

    //create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
            Log.e("URL", CHANNLE_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<YoutubeDetails> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDetails> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {
                                YoutubeDetails youtubeObject = new YoutubeDetails();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);
                                YoutubeLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }

    //End Youtube

    private void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoader();
                lytContent.setVisibility(View.GONE);
                loadlocation();
                loadEducation();
                loadTopStories(true);
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    private void AutoScroll() {

        dotsCount = forYouTopStoriesAdapter.getCount();
        if (dotsCount>1) {
            final int[] ci = {0};

            if (timerstate == true) {
                timer.cancel();
                timer.purge();
                timer = new Timer();
            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TopStoriesPager.post(new Runnable() {
                            @Override
                            public void run() {
                                TopStoriesPager.setCurrentItem(ci[0] % 10);
                                TopStoriesImageLayoutPager.setCurrentItem(ci[0] % 10);
                                AppPreference.getInstance(mContext).setItem(TopStoriesPager.getCurrentItem());
                                forYouTopStoriesAdapter.notifyDataSetChanged();
                                ci[0]++;
                                timerstate = true;
                            }
                        });
                    }
                }, 1000, 5000);
            }
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
            lytContent.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
        }
        if (noDataView != null) {
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
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

    private void restartLoader() {
        showLoader();
        lytContent.setVisibility(View.GONE);
        loadlocation();
        loadEducation();
        loadTopStories(true);
        loadTopStoriesImageLayout();
        initList(mListData);
        new RequestYoutubeAPI().execute();
        lytContent.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStop() {
        if (!forYouLocationItems.isEmpty())
        {
            forYouLocationItems.clear();
            forYouLocationAdapter.notifyDataSetChanged();
        }
        else if (!forYouEducationItems.isEmpty())
        {
            forYouEducationItems.clear();
            forYouEducationAdapter.notifyDataSetChanged();
        }
        else if(!postList.isEmpty())
        {
            postList.clear();
            forYouTopStoriesAdapter.notifyDataSetChanged();
        }
        else if (!postListImage.isEmpty())
        {
            postListImage.clear();
            forYouTopStoriesImageLayoutAdapter.notifyDataSetChanged();
        }
        else if (!mListData.isEmpty())
        {
            mListData.clear();
            forYouYoutubeAdapter.notifyDataSetChanged();
        }

        //timer.cancel();
        //AppPreference.getInstance(mContext).setItem(0);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        restartLoader();
    }

}