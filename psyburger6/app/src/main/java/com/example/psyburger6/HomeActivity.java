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

        Button createDummy = findViewById(R.id.dummybtn);
        createDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_io f = new file_io();

                String normal = "[[\"곡명\", \"가수\", \"ID\", \"Energy\", \"Valence\"], [\"mistaken\", \"양다일\", \"7c8cPVLWvtZwxDxA3KkWFP\", 0.419, 0.29], [\"천년의 사랑\", \"박완규\", \"29FS4aopbu0HWQ2GSFalvy\", 0.559, 0.153], [\"사랑이란 멜로는 없어\", \"Jeon Sang Geun\", \"0CGovscVJUxLASfj6KnVwO\", 0.476, 0.316], [\"눈의 꽃 - 미안하다, 사랑한다 (Original Television Soundtrack)\", \"박효신\", \"2KLL68vmqVDdhL7J9lquMb\", 0.354, 0.239], [\"그대라는 사치\", \"한동근\", \"37dkyQQNJLaqk09kkNr7In\", 0.444, 0.269], [\"Gangnam Style (강남스타일)\", \"싸이\", \"5c58c6sKc2JK3o75ZBSeL1\", 0.932, 0.731]]";
                f.writeToFile(normal, getApplicationContext(), "1.txt");

                String abnormal = "[[\"강남스타일\", \"싸이\"], [\"Doc와 함께 춤을\", \"DJ Doc\"], [\"칵테일 사랑\", \"마로니에\"], [\"Yes or Yes\", \"Twice\"], [\"너를 사랑하지 않아\", \"양다일\"], [\"천년의 사랑\", \"박완규\"], [\"사랑이란 멜로는 없어\", \"전상근\"], [\"눈의 꽃\", \"박효신\"], [\"그대라는 사치\", \"한동근\"]]";
                server s = new server();
                s.run(9, "[]", abnormal);
                while(!s.flag) continue;
                abnormal = s.data;

                f.writeToFile(abnormal, getApplicationContext(), "2.txt");
                f.writeToFile(abnormal, getApplicationContext(), "3.txt");
                f.writeToFile(abnormal, getApplicationContext(), "4.txt");
                f.writeToFile(abnormal, getApplicationContext(), "5.txt");

                f.writeToFile(" , \n , \n , \n , ", getApplicationContext(), "Log.txt");

                Log.d("Dummy Create", "Done");
            }
        });

        Button CheckLog = findViewById(R.id.checklog);
        CheckLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SadResultActivity.class);
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
