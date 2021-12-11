package highonstudy.com.data.sqlite;

public class notifymodel {

    private int id;
    private String title;
    private String content;
    private String url;
    private boolean readStatus;

    public notifymodel(int id, String title, String content, String url, boolean readStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.url = url;
        this.readStatus = readStatus;
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

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
