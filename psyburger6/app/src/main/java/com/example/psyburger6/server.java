package com.example.psyburger6;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class server {

    public String data;
    public boolean flag;

    public server(){
        flag = false;
    }

    public void run(int method, String seed){
        flag = false;
        new SocketTask(method + "|" + seed).execute();
    }
    public void run(int method, String seed, String new_data){
        flag = false;
        new SocketTask(method + "|" + seed + "**" + new_data).execute();
    }

    public void wait_for_data(){
        flag = false;
        new SocketTask("wait").execute();
    }

    private class SocketTask extends AsyncTask<Void, Void, String> {

        private final String message;

        public SocketTask(String message) {
            this.message = message;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "-1";
            /*
            if(message == "wait"){
                try{
                    //Log.d("Server", "Wait");
                    //Socket socket = new Socket("3.16.64.115", 3333);
                    Socket socket = new Socket("172.100.1.162", 3333);

                    OutputStream outputStream = socket.getOutputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg_tmp = "0|0";

                    result = "aaaaaaaaa";
                    while(result.length() > 5){
                        Log.d("Hello", Integer.toString(result.length()));
                        outputStream.write(msg_tmp.getBytes());

                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }

                        result = stringBuilder.toString();
                        //result = result.replace(System.getProperty("line.separator"), "");

                    }
                    Log.d("Flag Check", result);
                    //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //StringBuilder stringBuilder = new StringBuilder();
                    //String line;

                    //while ((line = bufferedReader.readLine()) != null) {
                    //    stringBuilder.append(line).append("\n");
                    //}
                    //Log.d("Server", "Wait Done");
                    //Log.d("server class", result);
                    bufferedReader.close();
                    socket.close();

                    //result = stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{*/
                try {
                    //Socket socket = new Socket("3.16.64.115", 3333); // 서버 IP 주소와 포트를 수정하세요
                    Socket socket = new Socket("172.100.1.162", 3333);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(message.getBytes());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    bufferedReader.close();
                    socket.close();

                    result = stringBuilder.toString();
                    //Log.d("server class", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //}

            data = result.replace(System.getProperty("line.separator"), "");;

            flag = true;
            return result;
        }


    }
}
