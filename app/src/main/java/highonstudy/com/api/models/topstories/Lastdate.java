
package highonstudy.com.api.models.topstories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lastdate {

    @SerializedName("Validdate")
    @Expose
    private String validdate;

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }

}
