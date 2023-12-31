package com.example.psyburger6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.Locale;

public class PlaySongActivity extends Activity implements
        OnMovementStatusChangedListener { //리스너 추가 부분

    //테미 움직임 추가 부분
    Robot robot;
    //Button dance_button;
    ImageButton dance_button;
    ImageButton nodance_button;
    String movement_state;
    String Playing_satae = "stop";
    double dance_speed = 0.1;
    int dance_flag = 1; //1이면 춤추기 허용

    private WebView webView;
    //670, 1270
    private String HTMLFormat = "<!DOCTYPE html>" +
            "<html lang=\"en\">" +
            "<head>" +
            "    <meta charset=\"UTF-8\">" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "    <meta http-equiv=\"Content-Security-Policy\" content=\"upgrade-insecure-requests\">" +
            "    <title>YouTube API Example</title>" +
            "    <script src=\"https://www.youtube.com/iframe_api\"></script>" +
            "" +
            "</head>" +
            "<body>" +
            "<div id=\"player\"></div>" +
            "" +
            "<script>" +
            "        var player;" +
            "" +
            "        function onYouTubeIframeAPIReady() {" +
            "            player = new YT.Player('player', {" +
            "                height: '670'," +
            "                width: '1270'," +
            "                videoId: 'TARGETVIDEOID'," +
            "                events: {" +
            "                    'onReady': onPlayerReady," +
            "                    'onStateChange': onPlayerStateChange" +
            "                }" +
            "            });" +
            "        }" +
            "" +
            "        function onPlayerReady(event) {" +
            "            event.target.playVideo();" +
            "        }" +
            "" +
            "        function onPlayerStateChange(event) {" +
            "            if (event.data == YT.PlayerState.ENDED) {" +
            "                Android.onVideoEnded(true);" +
            "            }" +
            "            else if (event.data == YT.PlayerState.PLAYING) {" +
            "                Android.onVideoPlaying(true);" +
            "            }" +
            "        }" +
            "    </script>" +
            "</body>" +
            "</html>";

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);

        //테미 움직임 추가 부분
        robot = Robot.getInstance();
        //그냥 버튼 지워짐
        dance_button = (ImageButton) findViewById(R.id.danceOnbutton);
        nodance_button = (ImageButton) findViewById(R.id.danceOffbutton);

        //Youtube 실행
        webView = findViewById(R.id.webView);
        // JavaScript를 활성화합니다.
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // JavaScript 인터페이스를 설정합니다.
        webView.addJavascriptInterface(new PlaySongActivity.JavaScriptInterface(), "Android");

        // WebViewClient를 설정하여 외부 링크가 기본 브라우저로 열리지 않도록 합니다.
        webView.setWebViewClient(new WebViewClient());
        // WebChromeClient를 설정하여 JavaScript의 alert 등을 처리합니다.
        webView.setWebChromeClient(new WebChromeClient());

        intent = getIntent();
        String videoid = intent.getStringExtra("VideoID");
        //영상 실행
        webView.loadData(HTMLFormat.replace("TARGETVIDEOID", videoid), "text/html", "utf-8");

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        dance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dance_flag = 1;
                    if(Playing_satae.equals("playing"))
                    {
                        movement_state = "motion1";
                        robot.turnBy(30, (float)dance_speed);
                        robot.tiltAngle(35, (float)0.5); // -25도에서 50도가 범위
                    }
            }
        });

        nodance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dance_flag = 0;
                robot.stopMovement(); //멈추기
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
            if(movement_state.equals("motion1")){
                movement_state = "motion2";
                //Log.i("movement","motion1");
                robot.turnBy(-60, (float)dance_speed);
                robot.tiltAngle(35, (float)0.5); // -25도에서 50도가 범위
            }
            else if(movement_state.equals("motion2")){
                movement_state = "motion1";
                //Log.i("movement","motion2");
                robot.turnBy(60, (float)dance_speed);
                //robot.tiltAngle(35, (float)0.5); // -25도에서 50도가 범위
            }
            else if(movement_state.equals("stop")){
                robot.stopMovement();
            }
        }
        else if(status.equals("abort")){
            //만약 회전이 불가능한 혼잡한 상황에 있다면 멈추고 춤추기로 변경.
            //dance_button.setText("춤추기");
            movement_state = "stop";
            robot.stopMovement();
            Log.i("abort", "혼잡 상황");
        }

    }

    // JavaScript에서 Android 메서드를 호출할 수 있도록 하는 인터페이스
    public class JavaScriptInterface {
        @JavascriptInterface
        public void onVideoEnded(boolean isEnded) {
            // JavaScript에서 호출되면 영상 종료 여부를 처리합니다.
            if (isEnded) {
                // 영상이 종료된 경우
                movement_state = "stop";
                Playing_satae = "stop";
                robot.stopMovement();
                Log.d("Youtube", "Video Ended");

                server s = new server();
                file_io f = new file_io();
                String id = intent.getStringExtra("ID");
                String data = f.readFromFile(getApplicationContext(), f.string_to_file(id));
                s.run(4, data);
                while(!s.flag) continue;
                String result = s.data;

                if (!"-1".equals(result)) {
                    String log_b = f.readFromFile(getApplicationContext(), "Log.txt");

                    // 현재 날짜 정보를 가져오기
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDate = dateFormat.format(new Date());

                    String log_n = log_b + "\n" + currentDate + " " + id + " " + result;
                    f.writeToFile(log_n, getApplicationContext(), "Log.txt");
                }

                Intent intent_ = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent_);
            } else {
                // 영상이 종료되지 않은 경우
                Log.d("Youtube", "Video not Ended");
            }
        }

        @JavascriptInterface
        public void onVideoPlaying(boolean isPlaying){
            if(isPlaying){
                Log.d("Youtube", "Playing");
                //dance_flag가 1이여야 춤추기 시작
                if(dance_flag == 1)
                {
                    movement_state = "motion1";
                    Playing_satae = "playing";
                    robot.turnBy(30, (float)dance_speed);
                    robot.tiltAngle(35, (float)0.5); // -25도에서 50도가 범위
                }
            }
            else{
                Log.d("Youtube", "Stopped Playing");
            }
        }
    }


}
