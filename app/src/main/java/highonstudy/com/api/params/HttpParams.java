package highonstudy.com.api.params;


public class HttpParams {

    // replace by your site url
    public static final String BASE_URL = "https://www.highonstudy.com/";

    public static final String API_TEXT_PER_PAGE = "per_page";
    public static final String API_TEXT_PAGE = "page";
    public static final String API_TEXT_CATEGORIES = "categories";
    public static final String API_RECENT_ITEM_PER_PAGE = "20";
    public static final String API_MAXIMUM_ITEM_PER_PAGE = "20";
    public static final String API_TEXT_ID = "id";
    public static final String API_EMBED = "_embed=true";
    public static final String API_FEATURED = "sticky=true";
    public static final String API_POST = "post";
    public static final String API_PARENT = "parent";
    public static final String API_TEXT_SEARCH = "search";

    public static final String API_CATEGORIES_TEXT="categories";
    public static final String API_TAGS_TEXT="tags";

    public static final String COMMENT_AUTHOR_NAME = "author_name";
    public static final String COMMENT_AUTHOR_EMAIL = "author_email";
    public static final String COMMENT_CONTENT = "content";

    public static final String API_CUSTOM_FIELD_PSU = "wp-json/wp/v2/psu?per_page="+ API_MAXIMUM_ITEM_PER_PAGE;
    public static final String API_CUSTOM_FIELD_BANK = "wp-json/wp/v2/bank?per_page="+ API_MAXIMUM_ITEM_PER_PAGE;
    public static final String API_CUSTOM_FIELD_EXAM = "wp-json/wp/v2/exam?per_page="+ API_MAXIMUM_ITEM_PER_PAGE;
    public static final String API_CUSTOM_FIELD = "wp-json/wp/v2/posts?per_page="+ API_MAXIMUM_ITEM_PER_PAGE;

    public static final String API_CATEGORIES = "wp-json/wp/v2/categories?page=1&";
    public static final String API_FEATURED_POSTS = "wp-json/wp/v2/posts?per_page=8";
    public static final String API_TEST = "wp-json/wp/v2/posts?categories=214";
    public static final String API_LATEST_JOBS = "wp-json/wp/v2/posts?per_page=100";
    public static final String API_RECENT_POSTS = "wp-json/wp/v2/posts?per_page=" + API_RECENT_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_CATEGORISED_ALL_POST = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_CATEGORISED_TOP_POST = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_SEARCHED_POSTS = "wp-json/wp/v2/posts?per_page=" + API_MAXIMUM_ITEM_PER_PAGE + "&" + API_EMBED;
    public static final String API_POST_DETAILS = "wp-json/wp/v2/posts/{" + API_TEXT_ID + "}?" + "&" + API_EMBED;
    public static final String API_MENUS = "wp-json/wp-api-menus/v2/menus/";
    public static final String API_SUB_MENUS = "wp-json/wp-api-menus/v2/menus/{" + API_TEXT_ID + "}";
    public static final String API_POST_A_COMMENT = "wp-json/wp/v2/comments?";


    public static final String HEADER_TOTAL_ITEM = "x-wp-total";
    public static final String HEADER_TOTAL_PAGE = "x-wp-totalpages";
    public static final String API_YOUTUBE_LINK = "?part=snippet&order=date&channelId=UCLCZBjFU3VsWzj27UtWIlnw&maxResults=10&key=AIzaSyCoQYzIh-wTy7x0YybbuPRoaIcIky_4DqY" ;


    public static String BASE_URL_Youtube = "https://www.googleapis.com";
}
