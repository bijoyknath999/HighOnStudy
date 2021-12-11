
package highonstudy.com.api.models.topstories;

import java.util.List;
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
    @SerializedName("news")
    @Expose
    private List<Object> news = null;
    @SerializedName("lastdate")
    @Expose
    private Lastdate lastdate;
    @SerializedName("short")
    @Expose
    private Short _short;

    @SerializedName("Image")
    @Expose
    private Image image;

    private int ID;

    public Post(String date, String link, Title title, Content content, List<Object> news, Lastdate lastdate, Short _short, int ID, Image image) {
        this.date = date;
        this.link = link;
        this.title = title;
        this.content = content;
        this.news = news;
        this.lastdate = lastdate;
        this._short = _short;
        this.ID = ID;
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

    public List<Object> getNews() {
        return news;
    }

    public void setNews(List<Object> news) {
        this.news = news;
    }

    public Lastdate getLastdate() {
        return lastdate;
    }

    public void setLastdate(Lastdate lastdate) {
        this.lastdate = lastdate;
    }

    public Short get_short() {
        return _short;
    }

    public void set_short(Short _short) {
        this._short = _short;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
