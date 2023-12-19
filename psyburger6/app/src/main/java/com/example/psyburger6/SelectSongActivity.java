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
public class SelectSongActivity extends Activity {
    String id;
    String seed;

=======
import com.robotemi.sdk.Robot;

public class SelectSongActivity extends Activity implements
        Robot.AsrListener{

    Robot robot;
    Button goToRecom;
    Button playSong;

    String stt_data;
    int stt_responce;
>>>>>>> d220a02f5b12b62a0c02bd4c8b699031218898dc
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_song);

<<<<<<< HEAD
        Intent intent0 = getIntent();
        id = intent0.getStringExtra("ID");
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
                //dummy file
                String dummy = "[[\"곡명\", \"가수\", \"ID\", \"Energy\", \"Valence\"], [\"mistaken\", \"양다일\", \"7c8cPVLWvtZwxDxA3KkWFP\", 0.419, 0.29], [\"천년의 사랑\", \"박완규\", \"29FS4aopbu0HWQ2GSFalvy\", 0.559, 0.153], [\"사랑이란 멜로는 없어\", \"Jeon Sang Geun\", \"0CGovscVJUxLASfj6KnVwO\", 0.476, 0.316], [\"눈의 꽃 - 미안하다, 사랑한다 (Original Television Soundtrack)\", \"박효신\", \"2KLL68vmqVDdhL7J9lquMb\", 0.354, 0.239], [\"그대라는 사치\", \"한동근\", \"37dkyQQNJLaqk09kkNr7In\", 0.444, 0.269], [\"Gangnam Style (강남스타일)\", \"싸이\", \"5c58c6sKc2JK3o75ZBSeL1\", 0.932, 0.731]]";
                file_io f = new file_io();
                //f.writeToFile(dummy, getApplicationContext(), f.string_to_file(id));

                seed = f.readFromFile(getApplicationContext(), f.string_to_file(id));

                server s = new server();

                s.run(2, seed);
                while(!s.flag) continue;
                //send to server seed
                //receive data

                String data_from_server = s.data;
                Log.d("SelectSongActivity Server", data_from_server);

                Intent intent = new Intent(getApplicationContext(), RecomSongActivity.class);
                intent.putExtra("recom", data_from_server);
                intent.putExtra("ID", id);
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
