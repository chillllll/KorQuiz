package com.example.suhyun.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class test extends AppCompatActivity{

    //뒤로 버튼 2회시 종료 위함
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    // 문제처리
    int qLiveCount = 0;
    int qMaxCount = 0;
    int okcount = 0;
    String seltext = "";

    // 레이아웃 관련
    ListView ExList, SelList;
    TextView ques, maxcnt, nowcnt, score;
    TextView c1, c2, c3;
    Button sendbtn;
    View imgview, numview;

    // 리스트뷰 어뎁터 관련
    ArrayList<String> arExample = new ArrayList<String>();
    ArrayList<String> arSelect = new ArrayList<String>();
    ArrayAdapter<String> ExAdapter;
    ArrayAdapter<String> SelAdapter;

    // 소켓연결 관련
    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;

    // 서버에 보낼 정보
    String   oType;
    String typemsg = "";
    String type="Atype";
    String keyA="king";
    String keyB="event";
    String keyC="event";

    // 서버에서 받은 정보
    String serverMSG = "";

    // 서버에서 받은 데이터 처리
    String tempA, tempB;
    String example, example2, example3, sel1, sel2, sel3, sel4;
    String st1, st2, st3;
    String bw1, bw2, bw3, bw4, bw5, bw6;
    String af1, af2, af3, af4, af5;
    int cid1, cid2, cid3 = 0;

    ImageLoaderTask imageLoaderTask;
    ImageLoaderTask imageLoaderTask2;
    ImageLoaderTask imageLoaderTask3;

    ImageView iv, iv2, iv3;

    // 결과 페이지로 넘겨줌
    ArrayList<String> oxresult = new ArrayList<>();
    ArrayList<String> qzcontent= new ArrayList<>();
    int qid1, qid2, qid3, qid4, qid5, qid6;
    String mType1, mType2;

    String qztext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db);


        // 리스트뷰 어댑터
        ExAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arExample);
        SelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arSelect);

        // 레이아웃 관련
        ExList = (ListView)findViewById(R.id.exListView);
        SelList = (ListView)findViewById(R.id.selListView);
        ques = (TextView)findViewById(R.id.question);
        nowcnt = (TextView)findViewById(R.id.nowqcnt);
        maxcnt = (TextView)findViewById(R.id.maxqcnt);
        sendbtn = (Button)findViewById(R.id.nextbtn);
        score = (TextView)findViewById(R.id.nowscore);

        numview = findViewById(R.id.qcntscore);


        numview.setVisibility(View.GONE);
        ques.setVisibility(View.GONE);

        c1 = (TextView)findViewById(R.id.tv);
        c2 = (TextView)findViewById(R.id.tv2);
        c3 = (TextView)findViewById(R.id.tv3);

        iv = (ImageView)findViewById(R.id.iv);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);


        // 서버에 보낼 정보
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        keyA = extras.getString("keyA");
        keyB = extras.getString("keyB");
        keyC = extras.getString("keyC");
        oType = extras.getString("oType");


        ExList.setAdapter(ExAdapter);
        SelList.setAdapter(SelAdapter);


        Thread worker = new Thread() {
            public void run() {
                // 소켓연결
                try {
                    socket = new Socket("192.168.130.154", 8266);
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 서버에서 넘어오는 데이터 처리
                try {
                    while (true) {
                        serverMSG = socket_in.readLine();

                        //System.out.println(serverMSG.)

                        arExample.clear();
                        arSelect.clear();

                        StringTokenizer stk = new StringTokenizer(serverMSG, "!");

                        if(type.equals("Atype")){
                            tempA=stk.nextToken();
                            sel1=stk.nextToken();
                        }

                        arSelect.add(sel1);





                    }
                } catch (IOException e) {
                }


            }
        };
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}