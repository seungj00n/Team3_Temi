package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class SelectSongActivity extends Activity {
    String id;
    String seed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_song);

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

        Button playSong = findViewById(R.id.gosong);
        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                intent.putExtra("VideoID", "2-JDFpplFLs");
                startActivity(intent);
            }
        });

        Button goToRecom = findViewById(R.id.recom);
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
}
