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
import java.util.List;

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
        String[] logs = logData.split("\n");

        // Process each log entry to format it with names based on ID
        List<String> formattedLogs = new ArrayList<>();
        for (String log : logs) {
            String[] parts = log.split(" ");
            if (parts.length >= 3) {
                String id = parts[2];
                String name = getNameById(id);
                formattedLogs.add("날짜: " + parts[0] + "   ID: " + id + "   이름: " + name + "   우을증 편차 점수: " + parts[3]);
            }
        }


        // Convert the list to an array for the ArrayAdapter
        String[] input_data = formattedLogs.toArray(new String[0]);

        //리스트뷰 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, input_data);

        log_List.setAdapter(adapter);

        ImageButton home_button = findViewById(R.id.home_button);

        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    // Helper method to get the name based on the ID
    private String getNameById(String id) {
        switch (id) {
            case "1":
                return "김을동";
            case "2":
                return "박금자";
            case "3":
                return "이옥자";
            case "4":
                return "최남진";
            case "5":
                return "아무개";
            default:
                return "Unknown";
        }
    }

}