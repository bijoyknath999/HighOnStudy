
package highonstudy.com.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("lastdate")
    @Expose
    private Lastdate lastdate;
    @SerializedName("topnews")
    @Expose
    private Topnews topnews;
    @SerializedName("trending")
    @Expose
    private Trending trending;
    @SerializedName("jobs")
    @Expose
    private Jobs jobs;
    @SerializedName("short")
    @Expose
    private Short shorts;
    @SerializedName("Image")
    @Expose
    private Image image;

    public Post(String link, Title title, Lastdate lastdate,Jobs jobs,Short shorts,Image image) {
        this.date = date;
        this.link = link;
        this.title = title;
        this.content = content;
        this.lastdate = lastdate;
        this.topnews = topnews;
        this.trending = trending;
        this.jobs = jobs;
        this.shorts = shorts;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Lastdate getLastdate() {
        return lastdate;
    }

    public void setLastdate(Lastdate lastdate) {
        this.lastdate = lastdate;
    }

    public Topnews getTopnews() {
        return topnews;
    }

    public void setTopnews(Topnews topnews) {
        this.topnews = topnews;
    }

    public Trending getTrending() {
        return trending;
    }

    public void setTrending(Trending trending) {
        this.trending = trending;
    }

    public Jobs getJobs() {
        return jobs;
    }

    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    public Short getShort() {
        return shorts;
    }

    public void setShort(Short shorts) {
        this.shorts = shorts;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
