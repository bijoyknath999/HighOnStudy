package highonstudy.com.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import highonstudy.com.R;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.SavedJObsModel;
import highonstudy.com.data.sqlite.SavedJobsDbController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppUtils {

    private static final long backPressed = 0;

    private static AppUtils appUtils = null;
    private static final List<SavedJObsModel> savedJObsModels = new ArrayList<>();
    private static SavedJobsDbController savedJobsDbController;

    public static AppUtils getInstance() {
        if (appUtils == null) {
            appUtils = new AppUtils();
        }
        return appUtils;
    }

    public static void youtubeLink(Activity activity) {
        updateLink(activity, activity.getString(R.string.youtube_url));
    }

    public void invokeWebView(Activity activity, Class<?> tClass, String title, String content, String url, String lastdate, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.JOBTITLE, title);
        intent.putExtra(AppConstant.JOBCONTENT, content);
        intent.putExtra(AppConstant.JOBURL, url);
        intent.putExtra(AppConstant.JOBLASTDATE, lastdate);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeGoWeb(Activity activity, Class<?> tClass, String title, String url, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.JOBTITLE, title);
        intent.putExtra(AppConstant.JOBURL, url);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }


    public static boolean isValidFormat(String value) {
        Date date = null;
        boolean valid = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        valid = date != null;

        return valid;
    }

    public static void goWeb(Context context, String jobtitle, String jobUrl)
    {
        Intent webSingle = new Intent(context, WebViewActivity.class);
        webSingle.putExtra(AppConstant.JOBTITLE, jobtitle);
        webSingle.putExtra(AppConstant.JOBURL, jobUrl);
        webSingle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(webSingle);
    }

    /*public static boolean checkdatevalidity(String date)
    {
        Date enteredDate = null;
        boolean valid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        try {
            enteredDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();// enteredDate will be null if date="287686";
        }

        Date currentDate = new Date();
        if (enteredDate.after(currentDate)) {
            valid = true;
        } else
            valid = false;

        return valid;
    }*/

    public static String checkdatevalidity(String date)
    {
        Date enteredDate = null;
        String valid = null;
        long diff = 0;
        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        try {
            enteredDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();// enteredDate will be null if date="287686";
        }

        Date currentDate = new Date();
        if (enteredDate.after(currentDate))
        {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = simpleDateFormat.format(c);

            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMMM yyyy");
            String inputString1 = formattedDate;
            String inputString2 = date;

            try {
                Date date1 = myFormat.parse(inputString1);
                Date date2 = myFormat.parse(inputString2);
                diff = date2.getTime() - date1.getTime();
                days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (days<7)
            {
                valid = "red";
            }
            else
            {
                if (days<15)
                {
                    valid = "yellow";
                }
                else{
                    valid = "green";
                }
            }
        }
        else {
            valid = "default";
        }

        return valid;
    }


    public static String checkdate(String date)
    {
        Date enteredDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        try {
            enteredDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();// enteredDate will be null if date="287686";
        }

        return "";
    }



    public static String getMonth(String date){
        Date d = null;
        try {
            d = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }


    public static String getDate(String date){
        Date d = null;
        try {
            d = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateS = new SimpleDateFormat("dd").format(cal.getTime());
        return dateS;
    }



    public static void faceBookLink(Activity activity) {
        try {
            ApplicationInfo applicationInfo = activity.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                updateLink(activity, "fb://facewebmodal/f?href=" + activity.getString(R.string.facebook_url));
                return;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        updateLink(activity, activity.getString(R.string.facebook_url));
    }

    public static void twitterLink(Activity activity) {
        updateLink(activity, activity.getString(R.string.twitter_url));
    }

    public static void telegramLink(Activity activity) {
        updateLink(activity, activity.getString(R.string.telegram_link));
    }

    public static void instagramLink(Activity activity) {
        updateLink(activity, activity.getString(R.string.instagram_link));
    }

    public static void setLink(Activity activity,String url) {
        updateLink(activity, url);
    }

    private static void updateLink(Activity activity, String text) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.resolveActivity(i,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            activity.startActivity(i);
        }
    }

    public static Fragment getFrag(Fragment fragment,int CatID)
    {
        Bundle args = new Bundle();
        args.putInt(AppConstant.BUNDLE_KEY_CATEGORY_ID, CatID);
        args.putString(AppConstant.BUNDLE_KEY_SEARCH_TEXT, "");
        fragment.setArguments(args);

        return fragment;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void noInternetWarning(View view, final Context context) {
        if (!isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(context.getString(R.string.connect), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            snackbar.show();
        }
    }


    public static boolean CheckSaveJobs(Context context, String JobUrl)
    {
        savedJobsDbController = new SavedJobsDbController(context);
        return savedJobsDbController.checkOne(JobUrl);
    }


    public static boolean SaveJob(Context mContext, String JobTitle, String JobContent, String JobURL, String JobLastDate)
    {
        boolean isSaved = false;
        if (savedJObsModels!=null)
        {
            if (JobContent != null && JobURL!= null && JobTitle!=null)
            {
                SavedJobsDbController savedJobsDbController = new SavedJobsDbController(mContext);
                if (!CheckSaveJobs(mContext,JobURL))
                {
                    // insert data into database
                    if (savedJobsDbController.insertSavedJobsData(JobTitle,JobContent,JobURL,JobLastDate)) {
                        isSaved = true;
                        Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    savedJobsDbController.deleteSavedJobs(JobURL,mContext);
                    isSaved = false;
                }

            }
        }
        return isSaved;
    }
}
