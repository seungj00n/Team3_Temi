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

public class SelectSongActivity extends Activity implements
        Robot.AsrListener{

    Robot robot;
    Button goToRecom;
    Button playSong;

    String stt_data;
    int stt_responce;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_song);

        robot = Robot.getInstance();

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        playSong = findViewById(R.id.gosong);
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", "2-JDFpplFLs");
                startActivity(intent);
            }
        });

        goToRecom = findViewById(R.id.recom);
        goToRecom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecomSongActivity.class);
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

    //이동 파트
    //asrResult가 음성 string 데이터다.
    public void onAsrResult(String asrResult) {

        //stt_responce에 따라 인텐트 넘어가게 한다.
        stt_data = asrResult;
        robot.finishConversation();

        //반환 값에 해당하는 버튼 실행 지금은 1번이면 노래 부르는 파트로 이동하고 2번이면 추천 파트로 넘어간다.
        stt_responce = processString(stt_data);
        Log.i("words", stt_data);
        if(stt_responce == 1){
            //해당 버튼 누르기
            playSong.performClick();
        }
        if(stt_responce == 2){
            goToRecom.performClick();
        }
    }

    private int processString(String inputString) {
        if (inputString.contains("감정") || inputString.contains("우울증")) {
            return 0;
        } else if (inputString.contains("들려줘") || inputString.contains("틀어줘")) {
            return 1;
        } else if (inputString.contains("추천해 줘")) {
            return 2;
        } else {
            return -1;
        }

    }
    //이동 파트
}
