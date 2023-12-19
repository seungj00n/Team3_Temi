package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

<<<<<<< HEAD
import java.util.ArrayList;

public class RecomSongActivity extends Activity {

    String data;
    ArrayList<Button> btns = new ArrayList();

    Intent intent0;
    String ID;

    String[] musics;
    ArrayList<String> title;
    ArrayList<String> singer;

    server s = new server();
    file_io f = new file_io();

=======
import com.robotemi.sdk.Robot;

public class RecomSongActivity extends Activity implements
        Robot.AsrListener{

    Robot robot;

    String stt_data;
    int stt_responce;
>>>>>>> d220a02f5b12b62a0c02bd4c8b699031218898dc
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_song);

<<<<<<< HEAD
        Button btn1 = findViewById(R.id.gosong1);
        btns.add(btn1);
        Button btn2 = findViewById(R.id.gosong2);
        btns.add(btn2);
        Button btn3 = findViewById(R.id.gosong3);
        btns.add(btn3);

        intent0 = getIntent();
        data = intent0.getStringExtra("recom");
        ID = intent0.getStringExtra("ID");
        //Log.d("RecomSongActivity", data);

        //split string
        musics = data.split("<1>");
        title = new ArrayList();
        singer = new ArrayList();

        for(int i = 0; i < musics.length; i++){
            String[] title_singer = musics[i].split("<2>");
            title.add(title_singer[0]);
            singer.add(title_singer[1]);
            btns.get(i).setText(title_singer[1] + "\n" + title_singer[0]);
        } //split string into titles and singers
=======
        robot = Robot.getInstance();
>>>>>>> d220a02f5b12b62a0c02bd4c8b699031218898dc

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
                //서버 add method
                String totxt = get_added_data_from_server(0);
                //txt로 저장하기
                f.writeToFile(totxt, getApplicationContext(), f.string_to_file(ID));

                //get youtube video id
                s.run(3, "0");
                while(!s.flag) continue;
                String videoid = s.data;

                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", videoid);
                startActivity(intent);
            }
        });

        Button playSong2 = findViewById(R.id.gosong2);
        playSong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버 add method
                String totxt = get_added_data_from_server(1);
                //txt로 저장하기
                f.writeToFile(totxt, getApplicationContext(), f.string_to_file(ID));

                //get youtube video id
                s.run(3, "1");
                while(!s.flag) continue;
                String videoid = s.data;

                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", videoid);
                startActivity(intent);
            }
        });

        Button playSong3 = findViewById(R.id.gosong3);
        playSong3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버 add method
                String totxt = get_added_data_from_server(2);
                //txt로 저장하기
                f.writeToFile(totxt, getApplicationContext(), f.string_to_file(ID));

                //get youtube video id
                s.run(3, "2");
                while(!s.flag) continue;
                String videoid = s.data;

                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", videoid);
                startActivity(intent);
            }
        });
    }

<<<<<<< HEAD
    private String get_added_data_from_server(int idx){

        String data = f.readFromFile(getApplicationContext(), f.string_to_file(ID));
        s.run(0, data);
        while(!s.flag) continue;

        String new_data = "[['" + title.get(idx) + "', '" + singer.get(idx) + "']]";
        s.run(1, data, new_data);
        while(!s.flag) continue;
        String data_from_server = s.data;
        Log.d("Added Data", data_from_server);
        return data_from_server;
    }
=======
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


>>>>>>> d220a02f5b12b62a0c02bd4c8b699031218898dc
}
