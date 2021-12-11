package highonstudy.com.data.sqlite;

public class SavedJObsModel {

    private int id;
    private String title;
    private String content;
    private String url;
    private String lastdate;

    public SavedJObsModel(int id, String title, String content, String url, String lastdate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
        this.lastdate = lastdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }
}
