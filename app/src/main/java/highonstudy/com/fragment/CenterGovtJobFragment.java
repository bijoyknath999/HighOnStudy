package highonstudy.com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import highonstudy.com.R;
import highonstudy.com.adapters.JobsAdapter;
import highonstudy.com.api.http.ApiUtils;
import highonstudy.com.api.models.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CenterGovtJobFragment extends Fragment implements SearchView.OnQueryTextListener {

    private LinearLayout loadingView, noDataView;
    private RecyclerView rvPosts;
    private JobsAdapter jobsAdapter = null;
    private List<Post> postList;
    int pageCount = 1;
    private SpinKitView bottomLayout;
    private GridLayoutManager mLayoutManager;
    private RelativeLayout MoreLoadButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        initView(rootView);
        initLoader(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoader();
        loadSearchedPosts(false, "");
    }

    public void initView(View rootView) {

        bottomLayout = rootView.findViewById(R.id.rv_lj_itemload);
        MoreLoadButton = rootView.findViewById(R.id.rv_more_load_button);
        loadingView = rootView.findViewById(R.id.loadingView);
        noDataView = rootView.findViewById(R.id.noDataView);

        rvPosts = rootView.findViewById(R.id.rvLatestJob);
        mLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        rvPosts.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        jobsAdapter = new JobsAdapter(getActivity(), postList);
        rvPosts.setAdapter(jobsAdapter);

        MoreLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecyclerView();
            }
        });



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

    public void loadSearchedPosts(boolean Scrolled,String searchText) {
        ApiUtils.getApiInterface().getSearchedPosts(958,pageCount, searchText).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {

                    rvPosts.setVisibility(View.VISIBLE);
                    if (!Scrolled) {
                        if (!postList.isEmpty()) {
                            postList.clear();
                        }
                    }

                    postList.addAll(response.body());
                    jobsAdapter = new JobsAdapter(getContext(), postList);
                    rvPosts.setAdapter(jobsAdapter);
                    jobsAdapter.notifyDataSetChanged();
                    MoreLoadButton.setVisibility(View.VISIBLE);

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

    // Method for repopulating recycler view
    private void updateRecyclerView() {

        // Show Progress Layout
        bottomLayout.setVisibility(View.VISIBLE);
        MoreLoadButton.setVisibility(View.GONE);

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {


                pageCount++;

                loadSearchedPosts(true,"");


                // Toast for task completion
                //Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();

                // After adding new data hide the view.
                bottomLayout.setVisibility(View.GONE);
                MoreLoadButton.setVisibility(View.VISIBLE);
            }
        }, 5000);

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
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        else if (id == R.id.action_refresh)
        {
            restartloader();
        }
        return super.onOptionsItemSelected(item);

    }

    private void restartloader() {
        rvPosts.setVisibility(View.GONE);
        MoreLoadButton.setVisibility(View.GONE);
        showLoader();
        loadSearchedPosts(false,"");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (postList.isEmpty())
        {
            postList.clear();
            jobsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        restartloader();
        super.onStart();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showLoader();
        loadSearchedPosts(false, newText);
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

}
