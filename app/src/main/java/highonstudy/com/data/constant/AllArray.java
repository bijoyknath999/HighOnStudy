package highonstudy.com.data.constant;

import android.content.Context;


import java.util.Arrays;
import java.util.List;

import highonstudy.com.R;

public class AllArray {

    public static List<String> getexploreslidertitle(Context context)
    {
        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.explore_silder_title));
        return titles;
    }

    public static List<String> getexploresliderid(Context context)
    {
        List<String> ids = Arrays.asList(context.getResources().getStringArray(R.array.explore_slider_id));
        return ids;
    }


    public static List<String> getjobbyQ_title(Context context)
    {
        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.jobbyQ_title));
        return titles;
    }

    public static List<String> getjobbyQ_ID(Context context)
    {
        List<String> IDS = Arrays.asList(context.getResources().getStringArray(R.array.jobbyQ_ID));
        return IDS;
    }


    public static List<String> getjobbyL_title(Context context)
    {
        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.jobbyL_title));
        return titles;
    }

    public static List<String> getjobbyL_LINK(Context context)
    {
        List<String> link = Arrays.asList(context.getResources().getStringArray(R.array.jobbyL_link));
        return link;
    }

    public static List<String> getpublic_service_comission_name(Context context)
    {
        List<String> names = Arrays.asList(context.getResources().getStringArray(R.array.public_service_comission_name));
        return names;
    }
    public static List<String> getpublic_service_comission_link(Context context)
    {
        List<String> names = Arrays.asList(context.getResources().getStringArray(R.array.public_service_comission_link));
        return names;
    }

    public static List<String> getForYouLocationtitle(Context context)
    {
        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.ForYouLocationTitle));
        return titles;
    }

    public static List<String> getForYouLocationLINK(Context context)
    {
        List<String> link = Arrays.asList(context.getResources().getStringArray(R.array.ForYouLocationLink));
        return link;
    }

    public static List<String> getForYouEducationtitle(Context context)
    {
        List<String> titles = Arrays.asList(context.getResources().getStringArray(R.array.ForYouEducationTitle));
        return titles;
    }

    public static List<String> getForYouEducationCatID(Context context)
    {
        List<String> id = Arrays.asList(context.getResources().getStringArray(R.array.ForYouEducationCatID));
        return id;
    }

}
