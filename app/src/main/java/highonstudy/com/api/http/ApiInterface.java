package highonstudy.com.api.http;


import highonstudy.com.api.models.Bank;
import highonstudy.com.api.models.Result;
import highonstudy.com.api.models.Post;
import highonstudy.com.api.models.youtube.YoutubeDetails;
import highonstudy.com.api.params.HttpParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(HttpParams.API_FEATURED_POSTS)
    Call<List<highonstudy.com.api.models.Post>> getFeaturedPosts(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_CATEGORIES_TEXT) int catID);

    @GET(HttpParams.API_LATEST_JOBS)
    Call<List<highonstudy.com.api.models.Post>> getPosts(@Query(HttpParams.API_CATEGORIES_TEXT) int catID);

    @GET(HttpParams.API_SEARCHED_POSTS)
    Call<List<highonstudy.com.api.models.Post>> getSearchedPosts(@Query(HttpParams.API_CATEGORIES_TEXT) int catID, @Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_CUSTOM_FIELD_PSU)
    Call<List<highonstudy.com.api.models.Post>> getSearchPSU(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_CUSTOM_FIELD_BANK)
    Call<List<Bank>> getSearchBank(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_CUSTOM_FIELD_EXAM)
    Call<List<highonstudy.com.api.models.Post>> getSearchEXAM(@Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_CUSTOM_FIELD)
    Call<List<highonstudy.com.api.models.Post>> getSearchTags(@Query(HttpParams.API_TAGS_TEXT) int TagsID, @Query(HttpParams.API_TEXT_PAGE) int pageCount, @Query(HttpParams.API_TEXT_SEARCH) String searchText);

    @GET(HttpParams.API_CUSTOM_FIELD)
    Call<List<Post>> getTopStories(@Query(HttpParams.API_CATEGORIES_TEXT) int ID, @Query(HttpParams.API_TEXT_PAGE) int pageCount);

    @GET("/youtube/v3/search")
    Call<Result> getYoutbeFeeds(@Query("key") String developerKey, @Query("channelId") String channelId, @Query("part") String id);

}
