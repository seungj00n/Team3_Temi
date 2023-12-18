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

public class RecomSongActivity extends Activity implements
        Robot.AsrListener{

    Robot robot;

    String stt_data;
    int stt_responce;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_song);

        robot = Robot.getInstance();

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button playSong1 = findViewById(R.id.gosong1);
        playSong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", "l20_tcPAhFA");
                startActivity(intent);
            }
        });

        Button playSong2 = findViewById(R.id.gosong2);
        playSong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", "Eijmb0QGInQ");
                startActivity(intent);
            }
        });

        Button playSong3 = findViewById(R.id.gosong3);
        playSong3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", "iCPqfZYA-BQ");
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
 //sst 받는 파트
    public void onAsrResult(String asrResult) {
        /*
        //stt_responce에 따라 인텐트 넘어가게 한다.
        stt_data = asrResult;
        robot.finishConversation();

        //반환 값에 해당하는 버튼 실행 지금은 1번이면 노래 부르는 파트로 이동하고 2번이면 추천 파트로 넘어간다.
        stt_responce = processString(stt_data);
        Log.i("words", stt_data);
        if(stt_responce == 1){
            //task_실행
        }
        if(stt_responce == 2){
            //task_실행
        }

         */
    }


}
