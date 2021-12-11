package highonstudy.com.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class SarkariResultFragment extends Fragment {
    private TextView failed_message;
    private Button failed_retry;
    private LinearLayout noInternetLayout;
    private ProgressBar progressBar;
    private WebView webview;
    private String JobURL;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }
                break;
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_job_universal, container, false);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        JobURL = "https://www.highonstudy.com/sarkari-result/?utm_source=app";
        setHasOptionsMenu(true);

        init(view);


        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (JobURL != null) {
            if (!haveNetworkConnection()) {
                displayNoInternetMessage();
            } else {
                loadWeb();
            }
        } else {
            displaynoDataMessage();
        }
    }

    private void init(View view) {
        webview = view.findViewById(R.id.free_webview);
        progressBar = view.findViewById(R.id.freewebprogressBar);
        noInternetLayout = view.findViewById(R.id.no_internet);
        failed_message = view.findViewById(R.id.failed_message);
        failed_retry = view.findViewById(R.id.failed_retry);

        failed_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoader();
            }
        });
    }

    private void loadWeb() {
        try {
            Document document = Jsoup.connect(JobURL).get();
            if (document.hasClass("rank-math-breadcrumb")) {
                document.getElementsByClass("rank-math-breadcrumb").first().remove();
            }
            document.getElementsByClass("header").first().remove();
            document.getElementsByClass("mshare").remove();
            document.getElementsByClass("bottommenu").remove();
            document.getElementsByClass("hi-bar").remove();
            document.getElementsByClass("inside-right-sidebar").remove();
            document.getElementsByClass("hi-row hi-black").remove();
            document.getElementsByTag("footer").remove();




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
        webview.setScrollbarFadingEnabled(false);
        webview.setPadding(0, 0, 0, 0);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if(str.contains("highonstudy.com")){
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

        webview.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webview.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }

        });

    }

    private void webViewGoBack(){
        webview.goBack();
    }

    private boolean haveNetworkConnection() {
        NetworkInfo[] allNetworkInfo = ((ConnectivityManager) getContext().getSystemService(WebViewActivity.CONNECTIVITY_SERVICE)).getAllNetworkInfo();
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
        failed_retry.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    private void displaynoDataMessage() {
        failed_message.setText(getResources().getString(R.string.fail_to_load));
        progressBar.setVisibility(View.GONE);
        failed_retry.setVisibility(View.GONE);
        webview.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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

    private void restartLoader()
    {
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

}