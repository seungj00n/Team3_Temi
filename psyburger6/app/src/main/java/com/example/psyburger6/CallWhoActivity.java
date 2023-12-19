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

public class CallWhoActivity extends Activity implements
        Robot.AsrListener{

    //--jsw part
    Robot robot;
    ListView menber_List;
    String stt_data;
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
                String temi_said = menbers_info.get(position).getName() + "님에게 전화를 걸겠습니다.";
                TtsRequest ttsRequest = TtsRequest.create(temi_said, false);
                Log.i("position", Integer.toString(position));
                robot.speak(ttsRequest);
                robot.startTelepresence("moblie", menbers_info.get(position).getUserId());
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
    //sst 받는 파트
    public void onAsrResult(String asrResult) {

        //stt_responce에 따라 인텐트 넘어가게 한다.
        stt_data = asrResult;
        robot.finishConversation();

        //반환 값에 해당하는 버튼 실행 지금은 1번이면 노래 부르는 파트로 이동하고 2번이면 추천 파트로 넘어간다.
        int stt_responce = processString_tocall(stt_data);
        Log.i("words", stt_data);
        if(stt_responce == -1){
            TtsRequest ttsRequest = TtsRequest.create("해당 사용자는 목록에 없습니다.", false);
        }
        else{
            List<UserInfo> menbers_info =  robot.getAllContact();
            
            String temi_said = menbers_info.get(stt_responce).getName() + "님에게 전화를 걸겠습니다.";
            TtsRequest ttsRequest = TtsRequest.create(temi_said, false);
            //Log.i("position", Integer.toString(stt_responce));
            robot.speak(ttsRequest);
            robot.startTelepresence("moblie", menbers_info.get(stt_responce).getUserId());
        }
    }

    //전화도 stt로 하려고 할 때, 문자열 나누는 부분
    private int processString_tocall(String inputString) {
        //멤버 정보 불러오고 배열에 이름 저장
        List<UserInfo> menbers_info =  robot.getAllContact();

        int call_position = -1;

        for(int i = 0; i < menbers_info.size(); i++){
            if(inputString.contains(menbers_info.get(i).getName())) {
                call_position = i;
                break;
            }
        }
        return call_position;
    }
}
