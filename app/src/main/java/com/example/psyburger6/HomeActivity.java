package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class HomeActivity extends Activity {

    Robot robot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        robot = Robot.getInstance();

        ImageButton randomButton = (ImageButton) findViewById(R.id.random);
        ImageButton goSetting = (ImageButton) findViewById(R.id.settingbutton);
        ImageButton targetButton = (ImageButton) findViewById(R.id.target);
        ImageButton callFriend = (ImageButton) findViewById(R.id.call);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindNextSingerActivity.class);
                startActivity(intent);
            }
        });

        targetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TagCardActivity.class);
                startActivity(intent);
            }
        });

        goSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        callFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CallWhoActivity.class);
                startActivity(intent);
            }
        });
    }
}
