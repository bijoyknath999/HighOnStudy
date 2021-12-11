package highonstudy.com.utility;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.Window;

public class Tools {

    public static void RTLMode(Window window) {
    }

    public static void changeMenuIconColor(Menu menu, int i) {
        for (int i2 = 0; i2 < menu.size(); i2++) {
            Drawable icon = menu.getItem(i2).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

}
