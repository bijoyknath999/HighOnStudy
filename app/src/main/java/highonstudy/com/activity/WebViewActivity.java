package highonstudy.com.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import highonstudy.com.R;
import highonstudy.com.data.AppPreference;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.SavedJObsModel;
import highonstudy.com.data.sqlite.SavedJobsDbController;
import highonstudy.com.utility.Tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity{

    private TextView failed_message;
    private Button failed_retry;
    private MenuItem menu_saved;
    private LinearLayout noInternetLayout;
    private ProgressBar progressBar;
    private FloatingActionButton shareButton;
    private Toolbar toolbar;
    private WebView webview;
    private String JobTitle;
    private String JobURL, JobLastDate, JobContent;

    // Favourites view
    private List<SavedJObsModel> savedJObsModels;
    private SavedJobsDbController savedJobsDbController;
    private boolean isSaved = false;
    private int JobID;
    private int JobTitleTest;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        JobTitle = getIntent().getStringExtra(AppConstant.JOBTITLE);
        JobURL = getIntent().getStringExtra(AppConstant.JOBURL);

        if (getIntent().getStringExtra(AppConstant.JOBLASTDATE) != null)
        {
            JobLastDate = getIntent().getStringExtra(AppConstant.JOBLASTDATE);
        }
        if (getIntent().getStringExtra(AppConstant.JOBCONTENT) != null)
        {
            JobContent = getIntent().getStringExtra(AppConstant.JOBCONTENT);
        }


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        init();
        savedJObsModels = new ArrayList<>();


        if (JobURL != null)
        {
            if (!haveNetworkConnection()) {
                displayNoInternetMessage();
            }
            else
                {
                    loadWeb();
                }
            loadforbookmark();
        }
        else
        {
            displaynoDataMessage();
        }
    }

    private void init() {
        webview = findViewById(R.id.detailView);
        progressBar = findViewById(R.id.webprogressBar);
        noInternetLayout = findViewById(R.id.no_internet);
        failed_message = findViewById(R.id.failed_message);
        failed_retry = findViewById(R.id.failed_retry);
        shareButton = findViewById(R.id.shareButton);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Html.fromHtml(JobTitle));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        failed_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoader();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JobTitle!=null && JobURL!=null)
                {
                    shareJob(JobTitle, JobURL+"?utm_source=app");
                }
            }
        });
    }

    private void loadforbookmark(){
        savedJobsDbController = new SavedJobsDbController(this);
        savedJObsModels.addAll(savedJobsDbController.getAllSavedJObsData());
        for (int i = 0; i < savedJObsModels.size(); i++) {
            if (savedJObsModels.get(i).getUrl().equals(JobURL)) {
                JobID = savedJObsModels.get(i).getId();
                isSaved = true;
                break;
            }
        }
    }

    private void loadWeb() {
        try {
            Document document = Jsoup.connect(JobURL).get();

            if (JobURL.contains("https://shop.highonstudy.com"))
            {
                document.getElementsByTag("footer").remove();
            }
            else
            {
                if (document.hasClass("rank-math-breadcrumb")) {
                    document.getElementsByClass("rank-math-breadcrumb").remove();
                }
                document.getElementsByClass("header").remove();
                document.getElementsByClass("mshare").remove();
                document.getElementsByClass("bottommenu").remove();
                document.getElementsByClass("hi-bar").remove();
                document.getElementsByClass("inside-right-sidebar").remove();
                document.getElementsByClass("hi-row hi-black").remove();
                document.getElementsByTag("footer").remove();
            }

            String content = document.html();
            String mime = "text/html";
            String encoding = "utf-8";
            webview.loadDataWithBaseURL(JobURL,content, mime, encoding,"");

            webview.getSettings().setJavaScriptEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);
        webview.setPadding(0, 0, 0, 0);
        webview.setWebChromeClient(new WebChromeClient());

        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if(str.startsWith("https://www.highonstudy.com") ||
                        str.startsWith("https://highonstudy.com") ||
                        str.startsWith("http://www.highonstudy.com") ||
                        str.startsWith("http://highonstudy.com")){
                    webView.loadUrl(str);
                }else{
                    webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                }
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean haveNetworkConnection() {
        NetworkInfo[] allNetworkInfo = ((ConnectivityManager) getSystemService(WebViewActivity.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
        boolean z = false;
        boolean z2 = false;
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo.getTypeName().equalsIgnoreCase(WebViewActivity.WIFI_SERVICE) && networkInfo.isConnected()) {
                z = true;
            }
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE") && networkInfo.isConnected()) {
                z2 = true;
            }
        }
        return z || z2;
    }

    private void displayNoInternetMessage() {
        failed_message.setText(getResources().getString(R.string.no_connection));
        webview.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
        failed_retry.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    private void displaynoDataMessage() {
        failed_message.setText(getResources().getString(R.string.fail_to_load));
        progressBar.setVisibility(View.GONE);
        failed_retry.setVisibility(View.GONE);
        webview.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    private void shareJob(String jobTitle, String jobURL) {
        String string = getString(R.string.app_name);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        String sharetext = jobTitle + ": \n" + jobURL + "\n";

        intent.putExtra("android.intent.extra.SUBJECT", string);
        intent.putExtra("android.intent.extra.TEXT", sharetext);
        startActivity(Intent.createChooser(intent, "Sharing is Caring"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            if (JobURL.contains("https://shop.highonstudy.com"))
            {
                startActivity(new Intent(WebViewActivity.this,MainActivity.class));
            }
            finish();
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_news_details, menu);
        menu_saved = menu.findItem(R.id.action_saved);
        if (JobContent==null)
        {
            menu_saved.setVisible(false);
        }
        else
        {
            setBookmark();
        }
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.defaultcolor));
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.action_refresh) {
            restartLoader();
        } else if (itemId == R.id.action_saved) {
            saveJob();
        }
        else if (itemId == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void saveJob() {


        if (savedJObsModels!=null)
        {
            if (JobContent != null && JobURL!= null && JobTitle!=null)
            {
                isSaved = !isSaved;
                SavedJobsDbController savedJobsDbController = new SavedJobsDbController(WebViewActivity.this);
                if (isSaved)
                {
                    // insert data into database
                    if (savedJobsDbController.insertSavedJobsData(JobTitle,JobContent,JobURL,JobLastDate)) {
                        Toast.makeText(WebViewActivity.this, "Saved", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    savedJobsDbController.deleteSavedJobs(JobURL,WebViewActivity.this);
                }

                invalidateOptionsMenu();
            }
        }

    }

    private void setBookmark()
    {
        if (isSaved) {
            menu_saved.setIcon(R.drawable.ic_bookmark);
        } else {
            menu_saved.setIcon(R.drawable.ic_bookmark_border);
        }
    }

    private void restartLoader() {
        progressBar.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);
        if (JobURL != null)
        {
            if (!haveNetworkConnection()) {
                displayNoInternetMessage();
            }
            else
            {
                loadWeb();
            }
        }
        else
        {
            displaynoDataMessage();
        }

    }

    @Override
    protected void onStart() {
        loadforbookmark();
        super.onStart();
    }

}