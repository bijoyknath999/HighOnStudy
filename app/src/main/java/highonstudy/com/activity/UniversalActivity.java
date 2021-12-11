package highonstudy.com.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import highonstudy.com.R;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.fragment.UniversalListFragment;
import highonstudy.com.utility.Tools;

public class UniversalActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;
    private Fragment postListFragment;
    private FragmentManager fragmentManager;
    private String selectedCategoryId;
    private String selectedCategoryName;
    private Toolbar toolbar;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
    }

    private void initVar() {
        mActivity = UniversalActivity.this;
        mContext = mActivity.getApplicationContext();

    }

    private void initView() {
        setContentView(R.layout.activity_universal);

        selectedCategoryId = getIntent().getStringExtra(AppConstant.JOBCATID);
        selectedCategoryName = getIntent().getStringExtra(AppConstant.JOBCATNAME);
        fragmentManager = getSupportFragmentManager();
        postListFragment = fragmentManager.findFragmentById(R.id.fragment_container);



        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle args = new Bundle();
        postListFragment = new UniversalListFragment();
        args.putString(AppConstant.BUNDLE_KEY_CATEGORY_ID, selectedCategoryId);
        args.putString(AppConstant.BUNDLE_KEY_SEARCH_TEXT, "");
        args.putString(AppConstant.BUNDLE_KEY_CATEGORY_NAME, selectedCategoryName);
        postListFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, postListFragment)
                .commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

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

        ImageView searchicon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchicon.setImageResource(R.drawable.ic_search);
        searchicon.setColorFilter(R.color.defaultcolor);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.defaultcolor));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //some texts here

                Bundle args = new Bundle();
                postListFragment = new UniversalListFragment();
                args.putString(AppConstant.BUNDLE_KEY_CATEGORY_ID, selectedCategoryId);
                args.putString(AppConstant.BUNDLE_KEY_SEARCH_TEXT, newText);
                args.putString(AppConstant.BUNDLE_KEY_CATEGORY_NAME, selectedCategoryName);
                postListFragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, postListFragment)
                        .commit();

                return false;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
