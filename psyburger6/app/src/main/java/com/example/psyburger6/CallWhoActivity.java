package com.example.psyburger6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class CallWhoActivity extends Activity {

    //--jsw part
    Robot robot;
    ListView menber_List;
    //--jsw part
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_friend);

        //--jsw part
        //로봇 가져오기
        robot = Robot.getInstance();
        //멤버 띄울 리스트뷰
        menber_List = findViewById(R.id.menber_List);

        //멤버 정보 불러오고 배열에 이름 저장
        List<UserInfo> menbers_info =  robot.getAllContact();
        List<String> Menber = new ArrayList<>();
        for (int i = 0; i < menbers_info.size(); i++) {
            Menber.add(menbers_info.get(i).getName()) ;
        }
        //리스트뷰 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Menber);
        menber_List.setAdapter(adapter);
        //--jsw part

        ImageButton goHome = (ImageButton) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //리스트뷰 클릭시 발생하는 일 처리파트
        menber_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position이 눌린 위치이다.
                TtsRequest ttsRequest = TtsRequest.create("통화 실행", false);
                robot.speak(ttsRequest);
                robot.startTelepresence("moblie", menbers_info.get(position).getUserId());
            }
        });

    }



}
