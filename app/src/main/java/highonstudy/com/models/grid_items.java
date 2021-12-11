package highonstudy.com.models;

public class grid_items {

    private String name;
    private String catID;

    public grid_items(String name, String catID) {
        this.name = name;
        this.catID = catID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }
}
