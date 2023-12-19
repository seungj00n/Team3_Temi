package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SadResultActivity extends Activity {

    ArrayList<Button> btns = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sad_result);

        Button btn = findViewById(R.id.LogText1);
        btns.add(btn);
        btn = findViewById(R.id.LogText2);
        btns.add(btn);
        btn = findViewById(R.id.LogText3);
        btns.add(btn);

        file_io f = new file_io();
        String log_ = f.readFromFile(getApplicationContext(), "Log.txt");
        String[] logs = log_.split("\n");

        for(int i = 0; i<3; i++){
            String[] items = logs[i+1].split(",");
            btns.get(i).setText("idx: " + items[0] + ", location: " + items[1]);
        }

        //Button btn = findViewById(R.id.LogText);
        //btn.setText(log_);
        Log.d("Log Checker", log_);

        //String[] logs = log_.split("\n");
        //btn.setText(logs[2]);

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
