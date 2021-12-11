package highonstudy.com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.firebase.messaging.FirebaseMessaging;

import highonstudy.com.R;
import highonstudy.com.data.AppPreference;
import highonstudy.com.utility.DialogUtils;

public class Settings extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        LabeledSwitch labeledSwitch = findViewById(R.id.notify_switch);

        labeledSwitch.setOn(AppPreference.getInstance(Settings.this).isNotificationOn());
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    AppPreference.getInstance(Settings.this).setNotify(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("pushnotify");
                }
                else
                {
                    AppPreference.getInstance(Settings.this).setNotify(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("pushnotify");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Settings.this,MainActivity.class));
        finish();
        super.onBackPressed();

    }
}