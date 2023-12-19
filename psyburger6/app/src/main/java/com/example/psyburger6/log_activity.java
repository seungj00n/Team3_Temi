package com.example.psyburger6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.app.Activity;


public class log_activity extends AppCompatActivity {

    ListView log_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        log_List = findViewById(R.id.log_List);

        file_io f = new file_io();
        String logData = f.readFromFile(getApplicationContext(), "Log.txt");
        Log.d("Show Log", logData);

        //input data를 여기에 넣어주면 됩니다.
        //String[] input_data = {"data1", "data2", "data3"};
        String[] input_data = logData.split("\n");


        //리스트뷰 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, input_data);

        log_List.setAdapter(adapter);

        ImageButton home_button = (ImageButton) findViewById(R.id.home_button);

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}