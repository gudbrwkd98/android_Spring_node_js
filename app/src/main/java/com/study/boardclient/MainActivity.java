package com.study.boardclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    EditText t_title;
    EditText t_writer;
    EditText t_content;
    ListView listView;
    BoardAdapter boardAdapter;
    HttpManager httpManager;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리스트와 어댑터 연결!!
        //디자인이 복잡한(복합뷰) ListView는 BaseAdapter 를 재정의해야 한다..
        listView = this.findViewById(R.id.listView); //JTable
        t_title = this.findViewById(R.id.t_title);
        t_writer = this.findViewById(R.id.t_writer);
        t_content = this.findViewById(R.id.t_content);

        boardAdapter = new BoardAdapter(this);
        listView.setAdapter(boardAdapter);//리스트뷰와 어댑터와의 연결!!!
        httpManager = new HttpManager();
        handler = new Handler(Looper.getMainLooper()){
            //handleMessage 영역은 UI 를 제저할수 있는 영역
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                ArrayList<Board> boardList = bundle.getParcelableArrayList("boardList");
                boardAdapter.list = boardList; //어뎁터에 리스트  주입
                boardAdapter.notifyDataSetChanged();
            }
        };
    }

    public void regist(View view){


            Thread thread = new Thread(){
                @Override
                public void run() {

                    try {
                        //json 생성하기
                        JSONObject json = new JSONObject();
                        json.put("title", t_title.getText().toString());
                        json.put("writer", t_writer.getText().toString());
                        json.put("content", t_content.getText().toString());
                        httpManager.requestByPost("http://localhost:7777/rest/board",json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            };
            thread.start();
    }

    public void getList(View view){
        //네트워크통신을 위한 쓰레드 생성
        Thread thread = new Thread(){
            @Override
            public void run(){
                     ArrayList boardList = httpManager.requestByGet("http://localhost:7777/rest/board");

                      //핸드렁에 요청 시점
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("boardList",boardList);
                message.setData(bundle);

                handler.sendMessage(message); //UI에 대신 뭐좀 해달라고 부탁!!
            }
        };
        thread.start();

    }
}
