package highonstudy.com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import highonstudy.com.R;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.utility.AppUtils;

import static highonstudy.com.utility.AppUtils.goWeb;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private Activity mActivity;

    // global toolbar
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ActionBarDrawerToggle toggle;

    private final boolean HIDE_DRAWER = false;



    private LinearLayout loadingView, noDataView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = BaseActivity.this;
        mContext = mActivity.getApplicationContext();

        // uncomment this line to disable ad
        //AdUtils.getInstance(mContext).disableBannerAd();
        //AdUtils.getInstance(mContext).disableInterstitialAd();

    }

    public void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.app_name));
    }

    public void initDrawer(boolean enable) {

        // Initialize drawer view
        mDrawerLayout = findViewById(R.id.drawer_layout);

        if (enable) {
            toggle = new ActionBarDrawerToggle
                    (this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }
            };


            toggle.setDrawerIndicatorEnabled(false);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();

            mNavigationView = findViewById(R.id.navigationView);
            mNavigationView.setItemIconTintList(null);
            getNavigationView().setNavigationItemSelectedListener(this);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void goWay(String CatID,String Catname)
    {
        Intent slidejonIntent = new Intent(mContext, UniversalActivity.class);
        slidejonIntent.putExtra(AppConstant.JOBCATID, CatID);
        slidejonIntent.putExtra(AppConstant.JOBCATNAME, Catname);
        startActivity(slidejonIntent);
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void enableBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Access toolbar
     */
    public Toolbar getToolBar() {
        return mToolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home)
            mDrawerLayout.closeDrawers();
        else if (id == R.id.action_govt)
            goWay("972","other");
        else if (id == R.id.action_jobs_qualification)
            goWeb(mContext,"Jobs By Qualification", "https://www.highonstudy.com/qualification-wise-jobs/");
        else if ((id == R.id.action_jobs_location))
            goWeb(mContext,"Jobs By Location", "https://www.highonstudy.com/jobs-by-location/");
        else if ((id == R.id.action_exam))
            goWay("","exam");
        else if (id == R.id.action_university_updates)
            showdialog();
        else if ((id == R.id.action_syllabus))
            goWeb(mContext,"Syllabus", "https://www.highonstudy.com/syllabus/");
        else if ((id == R.id.action_scholarship))
            goWeb(mContext,"Scholarship", "https://www.highonstudy.com/scholarship/");
        else if ((id == R.id.action_quiz_mock_test))
            goWeb(mContext,"Shop.Highonstudy.com","https://shop.highonstudy.com");
        else if (id == R.id.action_youtube)
            AppUtils.youtubeLink(mActivity);
        else if (id == R.id.action_facebook)
            AppUtils.faceBookLink(mActivity);
        else if (id == R.id.action_twitter)
            AppUtils.twitterLink(mActivity);
        else if ((id == R.id.action_telegram))
            AppUtils.telegramLink(mActivity);
        else if ((id == R.id.action_instagram))
            AppUtils.instagramLink(mActivity);
        else if (id == R.id.action_rate)
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
            }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
        else if ((id == R.id.action_official_site))
            AppUtils.setLink(mActivity, "https://www.highonstudy.com/");
        else if ((id == R.id.action_about))
            goWeb(mContext,"About Us", "https://www.highonstudy.com/about/");
        else if ((id == R.id.action_contact_us))
            goWeb(mContext,"Contact Us", "https://www.highonstudy.com/contact/");
        else if ((id == R.id.action_privacy))
            goWeb( mContext,"Privacy Policy", "https://www.highonstudy.com/privacy-policy/");
        else if ((id == R.id.action_t_and_c))
            goWeb(mContext,"Terms and Conditions", "https://www.highonstudy.com/terms-and-condition/");
        else if ((id == R.id.action_save_job))
        {
            Intent intent = new Intent(mContext,SavedJobsActivity.class);
            startActivity(intent);
        }
        else if ((id == R.id.action_settings))
        {
            Intent intent = new Intent(mContext,Settings.class);
            startActivity(intent);
        }




        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;

    }
    
    public void opendrawer(){
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showdialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,R.style.TransparentDialog);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.menudialog, null);
        builder.setView(view)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();

        TextView info,jobs,admission,admit_card,result,time_table;
        info = view.findViewById(R.id.action_info);
        jobs = view.findViewById(R.id.action_jobs);
        admission = view.findViewById(R.id.action_admission);
        admit_card = view.findViewById(R.id.action_admit_catd);
        result = view.findViewById(R.id.action_result);
        time_table = view.findViewById(R.id.action_time_table);
        ImageView back;
        back = view.findViewById(R.id.dialog_back);


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Info", "https://www.highonstudy.com/university/");
                alertDialog.dismiss();
            }
        });

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Jobs", "https://www.highonstudy.com/university/#jobs");
            }
        });

        admission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Admission", "https://www.highonstudy.com/university/#admission");
            }
        });

        admit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Admit Card", "https://www.highonstudy.com/university/#admit-card");
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Result", "https://www.highonstudy.com/university/#result");
            }
        });

        time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeb(mContext,"Time Table","https://www.highonstudy.com/university/#time-table");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

}