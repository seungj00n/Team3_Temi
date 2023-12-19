package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class HomeActivity extends Activity implements
        Robot.AsrListener {

    Robot robot;
    Button finish_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        robot = Robot.getInstance();

        ImageButton randomButton = (ImageButton) findViewById(R.id.random);
        ImageButton goSetting = (ImageButton) findViewById(R.id.settingbutton);
        ImageButton targetButton = (ImageButton) findViewById(R.id.target);
        ImageButton callFriend = (ImageButton) findViewById(R.id.call);

        finish_button = findViewById(R.id.finish);

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

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), log_activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        robot.addAsrListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeAsrListener(this);
    }

    //asrResult가 음성 string 데이터다.
    public void onAsrResult(String asrResult) {

            String stt_data;
            stt_data = asrResult;
            Log.i("words", stt_data);
    }

}
