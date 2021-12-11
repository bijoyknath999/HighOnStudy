
package highonstudy.com.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trending {

    @SerializedName("Trending")
    @Expose
    private String trending;

    public String getTrending() {
        return trending;
    }

    public void setTrending(String trending) {
        this.trending = trending;
    }

}
