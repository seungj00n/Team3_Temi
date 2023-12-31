package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;

public class SelectSongActivity extends Activity implements Robot.AsrListener{
    String id;
    String seed;

    Robot robot;
    String stt_data;

    int stt_responce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_song);

        robot = Robot.getInstance();

        Intent intent0 = getIntent();
        id = intent0.getStringExtra("ID");

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        ImageButton playSong = (ImageButton) findViewById(R.id.start_button);
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                server s = new server();
                s.run(8, "장윤정의 어머나 들려줘");
                while(!s.flag) continue;
                String videoid = s.data;

                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", videoid);
                startActivity(intent);
                */
                robot.askQuestion("어떤 노래를 듣고 싶으세요?");
            }
        });

        ImageButton goToRecom = (ImageButton) findViewById(R.id.recom_button);
        goToRecom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dummy file
                String dummy = "[[\"곡명\", \"가수\", \"ID\", \"Energy\", \"Valence\"], [\"mistaken\", \"양다일\", \"7c8cPVLWvtZwxDxA3KkWFP\", 0.419, 0.29], [\"천년의 사랑\", \"박완규\", \"29FS4aopbu0HWQ2GSFalvy\", 0.559, 0.153], [\"사랑이란 멜로는 없어\", \"Jeon Sang Geun\", \"0CGovscVJUxLASfj6KnVwO\", 0.476, 0.316], [\"눈의 꽃 - 미안하다, 사랑한다 (Original Television Soundtrack)\", \"박효신\", \"2KLL68vmqVDdhL7J9lquMb\", 0.354, 0.239], [\"그대라는 사치\", \"한동근\", \"37dkyQQNJLaqk09kkNr7In\", 0.444, 0.269], [\"Gangnam Style (강남스타일)\", \"싸이\", \"5c58c6sKc2JK3o75ZBSeL1\", 0.932, 0.731]]";
                file_io f = new file_io();
                //f.writeToFile(dummy, getApplicationContext(), f.string_to_file(id));

                id = "1";
                seed = f.readFromFile(getApplicationContext(), f.string_to_file(id));

                server s = new server();

                //recommend
                s.run(6, seed);
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

    public void onAsrResult(String asrResult){
        //stt_responce에 따라 인텐트 넘어가게 한다.
        stt_data = asrResult;
        Log.d("da",stt_data);
        robot.finishConversation();

        // 하드코딩된 문자열
        String hardcodedString = stt_data;
        hardcodedString = hardcodedString.replaceAll("\\s+", "");
        // 문자열 처리 결과를 가져오는 함수 호출
        String result = processString(hardcodedString);

        // 결과를 소켓 통신을 통해 서버에 전달
        /*if (result != "-1") {
            new SocketTask(Integer.toString(result)).execute();
        }*/

        if (result == "6"){
            file_io f = new file_io();
            seed = f.readFromFile(getApplicationContext(), f.string_to_file(id));

            server s = new server();

            Log.d("seed", seed);
            //recommend
            s.run(6, seed);
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
        else if (result == "8"){
            //System.out.println(result);
            //new SocketTask(result).execute();


            server s = new server();
            s.run(8, hardcodedString);
            while(!s.flag) continue;
            String data = s.data;
            Log.d("Video Before", data);
            String[] datas = data.split("<1>");
            String videoid = datas[0];
            videoid = videoid.replace(System.getProperty("line.separator"), "");

            s = null;

            try{
                String title = datas[1];
                String singer = datas[2];

                file_io f = new file_io();
                String txt = f.readFromFile(getApplicationContext(), f.string_to_file(id));

                String new_data = "[['" + title + "', '" + singer + "']]";
                s = new server();
                s.run(9, txt, new_data);
                while(!s.flag) continue;
                String data_from_server = s.data;
                Log.d("Added Data", data_from_server);

                f.writeToFile(data_from_server, getApplicationContext(), f.string_to_file(id));
            }catch(Exception e){
                Log.d("Exception", e.getMessage());
            };


            Log.d("Video ID", videoid);
            Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
            intent.putExtra("VideoID", videoid);
            intent.putExtra("ID", id);
            startActivity(intent);
        }

    }
    private String processString(String inputString) {
        System.out.println(inputString);
        if (inputString.contains("감정") || inputString.contains("우울증")) {
            //return "7" + "|" + seed;
            return "7";
        } else if (inputString.contains("들려") || inputString.contains("틀어") || inputString.contains("알려")) {
            //return "8" + "|" + inputString;
            return "8";
        } else if (inputString.contains("추천")) {
            //return "6" + "|" + seed;
            return "6";
        } else {
            //System.out.println(-1);
            return "-1";
        }
    }
}
