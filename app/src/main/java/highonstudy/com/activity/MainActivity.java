package highonstudy.com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;

import highonstudy.com.R;
import highonstudy.com.adapters.home.FeaturedPagerAdapter;
import highonstudy.com.adapters.home.GridAdapter;
import highonstudy.com.adapters.TabAdapter;
import highonstudy.com.api.models.Post;
import highonstudy.com.data.AppPreference;
import highonstudy.com.fragment.ForYouFragment;
import highonstudy.com.models.grid_items;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.fragment.AdmitCardFragment;
import highonstudy.com.fragment.CenterGovtJobFragment;
import highonstudy.com.fragment.CurrentAffairsFragment;
import highonstudy.com.fragment.FreeJobFragment;
import highonstudy.com.fragment.HomeFragment;
import highonstudy.com.fragment.LatestJobFragment;
import highonstudy.com.fragment.PostListFragment;
import highonstudy.com.fragment.QuestionPaperFragment;
import highonstudy.com.fragment.ResultFragment;
import highonstudy.com.fragment.SarkariResultFragment;
import highonstudy.com.utility.AdUtils;
import highonstudy.com.utility.AppUtils;
import highonstudy.com.utility.Tools;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.rd.PageIndicatorView;


import java.util.List;

public class MainActivity extends BaseActivity{


    private final String[] images = {
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/govt.png",
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/it.png",
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/bank.png",
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/railway.png",
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/ssc.png",
            "https://www.freshersnow.com/wp-content/themes/Newspaper/images/custom/college.png"
    };
    private final String[] names = {"GOVT JOBS","IT JOBS","BANK JOBS","RAILWAY JOBS","SSC JOBS","OFF CAMPUS"};

    // menu list

    private RecyclerView rvMenus;

    private PageIndicatorView pager_indicator;
    private final int dotsCount = 0;
    private ImageView[] dots;

    private final int currentIndex = 0;

    // Variables
    private Activity mActivity;
    private Context mContext;
    private final int perPageCount = 5;
    private final int currentCategoryIndex = 0;

    // Views
    private ImageButton imgBtnSearch;
    private ScrollView lytContent;
    private RelativeLayout rlTopRecentPost, rlNotificationView, rlSelectableCategory;
    private TextView tvFeaturedSeeMore, tvRecentSeeMore;



    // featured list
    private List<Post> featuredPostList;
    private RelativeLayout mFeaturedLayout;
    private ViewPager pagerFeaturedPost;
    private final FeaturedPagerAdapter featuredPostAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean timerstate;



    // recent list
    private final Post firstPost = null;
    private List<grid_items> grid_itemsList;
    private RecyclerView postsRecyclerView;
    private final GridAdapter gridAdapter = null;
    private GridLayoutManager mLayoutManager;
    private ProgressBar pbSectionLoader, pbSelectableCatLoader;


    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton sharebutton;

    DachshundTabLayout tabLayout;
    TabItem tabItem1,tabItem2,tabItem3,tabItem4,tabItem5;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    private AdView mAdView;
    private MeowBottomNavigation bottomNavigation;

    private MenuItem menu_notification;


    private HomeFragment homeFragment;


    Fragment selectedFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mActivity = MainActivity.this;
        mContext = getApplicationContext();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        bottomNavigation = findViewById(R.id.bottom_nav_bar);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_save));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_menu));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                int id = item.getId();
                switch (id)
                {
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                    case 2:
                        AppUtils.goWeb(mContext,"Shop.Highonstudy.com","https://shop.highonstudy.com");
                        break;
                    case 3:
                        Intent intent = new Intent(mContext, SavedJobsActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        opendrawer();
                        break;
                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                int id = item.getId();
                switch (id)
                {
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                    case 2:
                        AppUtils.goWeb(mContext,"Shop.Highonstudy.com","https://shop.highonstudy.com");
                        break;
                    case 3:
                        Intent intent = new Intent(mContext, SavedJobsActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        opendrawer();
                        break;
                }
            }
        });

        bottomNavigation.show(1, true);



        /*sharebutton = (FloatingActionButton) findViewById(R.id.fab_share);
        bottomNavigationView =  findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setSelectedItemId(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_menu)
                    opendrawer();
                else if(id == R.id.action_shop)
                    AppUtils.goWeb(mContext,"Shop.Highonstudy.com","https://shop.highonstudy.com");
                else if (id == R.id.action_home)
                    viewPager.setCurrentItem(0);
                else if (id == R.id.action_saved) {
                    Intent intent = new Intent(mContext, SavedJobsActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        tabLayout= findViewById(R.id.tablayout1);
        viewPager= findViewById(R.id.vpager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);
        viewPager.setAdapter(tabAdapter);

        tabAdapter.addFragment(new HomeFragment(), "");
        tabAdapter.addFragment(new ForYouFragment(), "FOR YOU");
        tabAdapter.addFragment(new LatestJobFragment(), "LATEST JOBS");
        tabAdapter.addFragment(new AdmitCardFragment(), "ADMIT CARD");
        tabAdapter.addFragment(new ResultFragment(), "RESULT");
        tabAdapter.addFragment(new FreeJobFragment(), "FREE JOB ALERT");
        tabAdapter.addFragment(new SarkariResultFragment(), "SARKARI RESULT");
        tabAdapter.addFragment(new CenterGovtJobFragment(), "CENTER GOVT JOB");
        tabAdapter.addFragment(new CurrentAffairsFragment(), "CURRENT AFFAIRS");
        tabAdapter.addFragment(new QuestionPaperFragment(), "QUESTION PAPER");
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);

        setAdviewVis();


        //loadFragment(new HomeFragment());

        initToolbar();
        initDrawer(true);

        if (AppPreference.getInstance(mContext).isNotificationOn())
        {
            FirebaseMessaging.getInstance().subscribeToTopic("pushnotify");
        }
        else
        {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("pushnotify");
        }
    }

    private void setAdviewVis() {
        if (viewPager.getCurrentItem() == 0)
        {
            mAdView.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (viewPager.getCurrentItem() == 0)
                    {
                        mAdView.setVisibility(View.GONE);
                    }
                    else
                    {
                        mAdView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.vpager, fragment);
        beginTransaction.addToBackStack(null);
        beginTransaction.commitAllowingStateLoss();
    }


    private void setupViewPager(ViewPager viewPager) {

        Fragment fragment1,fragment2,fragment3,fragment4,fragment5,fragment6;
        fragment1 = AppUtils.getFrag(new PostListFragment(), 972);
        fragment2 = AppUtils.getFrag(new PostListFragment(), 145);
        fragment3 = AppUtils.getFrag(new PostListFragment(), 427);
        fragment4 = AppUtils.getFrag(new PostListFragment(), 958);
        fragment5 = AppUtils.getFrag(new PostListFragment(), 273);
        fragment6 = AppUtils.getFrag(new PostListFragment(), 131);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        menu_notification = menu.findItem(R.id.action_notification);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        ImageView icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        icon.setImageResource(R.drawable.ic_search);
        icon.setColorFilter(R.color.defaultcolor);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.defaultcolor));
        searchView.setQueryHint(getString(R.string.search));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.action_notification) {
            Intent intent = new Intent(mContext, NotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(AppConstant.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).registerReceiver(newNotificationReceiver, intentFilter);

        AdUtils.getInstance(mContext).loadFullScreenAd(mActivity);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newNotificationReceiver);
    }

    // received new broadcast
    private final BroadcastReceiver newNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //initNotification();
        }
    };

    @Override
    public void onBackPressed() {
        if (getDrawerLayout().isDrawerOpen(GravityCompat.START))
        {
            getDrawerLayout().closeDrawers();
            bottomNavigation.show(1, true);

        }
    }
}