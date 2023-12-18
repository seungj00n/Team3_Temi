package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FindNextSingerActivity extends Activity implements
        OnGoToLocationStatusChangedListener,
        OnMovementStatusChangedListener,
        OnBeWithMeStatusChangedListener {

    Button start_searching;
    Robot robot;

    //움직임 저장
    String moving_status;
    int turn_count = 0;

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addOnMovementStatusChangedListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeOnMovementStatusChangedListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_next_singer);

        //테미 움직임에 추가한 부분
        robot = Robot.getInstance();
        start_searching = findViewById(R.id.start_searching);

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button temifind = findViewById(R.id.test1);

        temifind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TagCardActivity.class);
                startActivity(intent);
            }
        });

        //랜덤 찾는 버튼
        start_searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //import java.util.Random; 추가 해야됨
                //랜덤 각도 넣어서 이동하는 것

                ramdom_map();
                TtsRequest ttsRequest = TtsRequest.create("노래 부를 분을 찾습니다.", false);
                robot.speak(ttsRequest);

                //robot.beWithMe();
            }
        });

    }

    //랜덤으로 맵 찾기
    protected void ramdom_map() {

        List<String> points = robot.getLocations();
        int place_num = points.size() - 1; //저장된 장소 개수 저장(홈베이스 개수 빼야한다)
        Random random = new Random();
        int loc = random.nextInt(place_num) + 1; //0번이 홈베이스라서 하나 늘려서 빼주자
        robot.goTo(points.get(loc));
        //끝나면 밑의 리스너에 감지된다.
    }

    public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description){
        if(status.equals("complete")) {
            moving_status = "after turn"; // 지정 위치로 이동, 랜덤 각도 회전

            Random random = new Random();
            int degree = random.nextInt(360) + 1;//1에서 360까지
            robot.turnBy(degree, (float)0.5);
            Log.i("degree", Integer.toString(degree));
            robot.tiltAngle(35, (float)0.5); // -25도에서 50도가 범위
            //돌고 onMovementStatusChanged 1번으로 이동
        }
    }

    public void onMovementStatusChanged(String type, String status) { //추상매서드 구성
        //움직임이 끝났을 때
        //if(type == "turnBy")
        //{
        Log.i("movement", status);
        if(status.equals("complete")){
            //랜덤으로 회전한 이후
            // beWithMe 실행해서 찾기
            if(moving_status.equals("after turn")){
                robot.beWithMe();
            }
            else if(moving_status.equals("after find")) {
                //robot.beWithMe();
                //moving_status = -1;
                robot.turnBy(360, (float) 0.6);

                //beWuithMe 돌릴 새로운 스레드 생성
                class MyThread extends Thread {
                    public MyThread() {
                    }

                    @Override
                    public void run() {
                        Log.i("thread", "Thread is running");
                        robot.beWithMe();
                        Log.i("thread", "Thread is finish");
                    }
                }
                //스레드 실행
                MyThread thread = new MyThread();
                thread.start();
            }
        }
    }

    public static void sleep(long millis) throws InterruptedException {
        Thread.currentThread().sleep(millis);
    }

    public void onBeWithMeStatusChanged(String status){
        if(status.equals("search")){
            // 90도 회전합니다.
            robot.turnBy(30, (float) 0.6);
            turn_count++;

            //만약 한바퀴 돈거면
            if(turn_count == 12) {
                //값 초기화 하고
                turn_count = 0;

                //다시 랜덤지점으로 이동한다.
                ramdom_map();
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    robot.beWithMe();
                }
            }, 1500); //딜레이 타임 조절
        }
    }

}
