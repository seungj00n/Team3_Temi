package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class TagCardActivity extends Activity {

    String seed;
    String id;
    String id_from_server;

    Thread th = new Thread(()->{

        server s = new server();
        do{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            s.run(0, "0");
            while(!s.flag) continue;
        }while(s.data.length() > 5);

        id_from_server = s.data;

        s = null;

        Log.d("Tag From Server", id_from_server);
        Intent intent = new Intent(getApplicationContext(), SelectSongActivity.class);
        intent.putExtra("ID", id_from_server);
        startActivity(intent);
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_card);


        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Button goToSelectSong = findViewById(R.id.test1);

        goToSelectSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //id = "1";

                server s = new server();
                s.run(1, "-");
                while(!s.flag) continue;
                String id = s.data;

                Intent intent = new Intent(getApplicationContext(), SelectSongActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        th.start();
    }

    public void read_id(){
        file_io f = new file_io();
        String txtfile = f.string_to_file(id);
        seed = f.readFromFile(getApplicationContext(), txtfile);
    }
}
