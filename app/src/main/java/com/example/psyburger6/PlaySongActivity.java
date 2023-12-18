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
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener;

public class PlaySongActivity extends Activity implements
        OnMovementStatusChangedListener { //리스너 추가 부분

    //테미 움직임 추가 부분
    Robot robot;
    String movement_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);

        //테미 움직임 추가 부분
        robot = Robot.getInstance();
        movement_state = "";
        robot.turnBy(60, (float)1);

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button happySong = findViewById(R.id.happysong);
        happySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HappyResultActivity.class);
                startActivity(intent);
            }
        });

        Button sadSong = findViewById(R.id.sadsong);
        sadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SadResultActivity.class);
                startActivity(intent);
            }
        });
    }

    //여기 start랑 stop도 테미 움직임에 추가된 부분.
    protected void onStart() {
        super.onStart();
        robot.addOnMovementStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnMovementStatusChangedListener(this);
    }

    public void onMovementStatusChanged(String type, String status) { //추상매서드 구성
        //움직임이 끝났을 때
        //if(type == "turnBy")
        //{
        Log.i("movement", status);
        if(status.equals("complete")){

        }
        else if(status.equals("abort")){
            //만약 회전이 불가능한 혼잡한 상황에 있다면 그 자리에서 따라가기 실행한다.
            robot.beWithMe();
            Log.i("abort", "혼잡 상황");
        }

    }


}
