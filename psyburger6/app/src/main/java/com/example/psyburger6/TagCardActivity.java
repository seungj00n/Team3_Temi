package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class TagCardActivity extends Activity {

    String seed;
    String id;

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
                id = "1";

                Intent intent = new Intent(getApplicationContext(), SelectSongActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });
    }



    public void read_id(){
        file_io f = new file_io();
        String txtfile = f.string_to_file(id);
        seed = f.readFromFile(getApplicationContext(), txtfile);
    }
}
