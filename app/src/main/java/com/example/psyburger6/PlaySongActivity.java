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

public class PlaySongActivity extends Activity implements
        OnMovementStatusChangedListener { //리스너 추가 부분

    //테미 움직임 추가 부분
    Robot robot;
    String movement_state;

    private WebView webView;
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
            "                height: '600'," +
            "                width: '1080'," +
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
            "        }" +
            "    </script>" +
            "</body>" +
            "</html>";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);

        //테미 움직임 추가 부분
        robot = Robot.getInstance();
        movement_state = "";
        robot.turnBy(60, (float)1);

        //Youtube 실행
        webView = findViewById(R.id.webView);
        // JavaScript를 활성화합니다.
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // JavaScript 인터페이스를 설정합니다.
        webView.addJavascriptInterface(new PlaySongActivity.JavaScriptInterface(), "Android");
        //webView.addJavascriptInterface(new PlaySongActivity.JavaScriptInterface());

        // WebViewClient를 설정하여 외부 링크가 기본 브라우저로 열리지 않도록 합니다.
        webView.setWebViewClient(new WebViewClient());
        // WebChromeClient를 설정하여 JavaScript의 alert 등을 처리합니다.
        webView.setWebChromeClient(new WebChromeClient());

        Intent intent = getIntent();
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

    // JavaScript에서 Android 메서드를 호출할 수 있도록 하는 인터페이스
    public class JavaScriptInterface {
        @JavascriptInterface
        public void onVideoEnded(boolean isEnded) {
            // JavaScript에서 호출되면 영상 종료 여부를 처리합니다.
            if (isEnded) {
                // 영상이 종료된 경우
                Log.d("main", "done");
                Intent intent = new Intent(getApplicationContext(), FindNextSingerActivity.class);
                startActivity(intent);
            } else {
                // 영상이 종료되지 않은 경우
                Log.d("main", "running");
            }
        }
        public void onVideoPlaying(boolean isPlaying){
            if(isPlaying){
                Log.d("Main", "Playing");
            }
            else{
                Log.d("Main", "Pass");
            }
        }
    }


}
