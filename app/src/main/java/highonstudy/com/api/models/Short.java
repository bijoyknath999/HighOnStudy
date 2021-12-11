package highonstudy.com.api.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Short {

    @SerializedName("Short")
    @Expose
    private String _short;

    public String getShort() {
        return _short;
    }

    public void setShort(String _short) {
        this._short = _short;
    }

}