package com.catacore.coredropdownnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.catacore.dropdownnotification.CoreNotificationCenter;
import com.catacore.dropdownnotification.notifications.ErrorNotification;
import com.catacore.dropdownnotification.notifications.InfoNotification;
import com.catacore.dropdownnotification.notifications.NotificationType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoreNotificationCenter center = CoreNotificationCenter.getInstance(this);


        TextView textView = findViewById(R.id.text);
        textView.setOnClickListener(v-> {
            center.add(new InfoNotification(this, "Test"));
        });
    }
}
