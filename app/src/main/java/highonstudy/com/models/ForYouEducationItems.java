package highonstudy.com.models;

public class ForYouEducationItems {

    private String name;
    private String id;

    public ForYouEducationItems(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
}
