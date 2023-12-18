package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.UserInfo;

import java.util.List;

public class CallWhoActivity extends Activity {

    Button call_test;

    Robot robot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_friend);

        //로봇 부르기 계속 불러줘야 한다.
        robot = Robot.getInstance();

        call_test = findViewById(R.id.call_test);

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //전화 테스트하는 버튼 일단 어드민한데 전화한다
        call_test.setOnClickListener(new View.OnClickListener() { //이 버튼에 리스너를 추가한것
            @Override
            public void onClick(View view) {

                //테스트용
                List<UserInfo> ids =  robot.getAllContact();

                for(int i = 0; i < ids.size(); i++){
                    Log.d("ids", ids.get(i).getName()  );
                }



                //여기까지

                TtsRequest ttsRequest = TtsRequest.create("통화 실행", false);
                robot.speak(ttsRequest);

                UserInfo id = robot.getAdminInfo();
                robot.startTelepresence("moblie", id.getUserId());
            }
        });

    }



}
