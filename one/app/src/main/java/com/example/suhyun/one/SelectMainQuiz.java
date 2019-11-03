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
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.w3c.dom.Text;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class SelectMainQuiz extends AppCompatActivity {

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
    TextView ques, maxcnt, nowcnt, score,lvView;
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
    BufferedReader socket_in2;
    PrintWriter socket_out;

    // 서버에 보낼 정보
    String type, keyA, keyB, keyC, level;
    String typemsg = "";

    // 서버에서 받은 정보
    String serverMSG = "";
    byte[] cimg=null;
    InputStream is=null;
    ObjectInputStream ois=null;

    // 서버에서 받은 데이터 처리
    String tempA, tempB;
    String example, example2, example3, sel1, sel2, sel3, sel4;
    String st1, st2, st3;
    String bw1, bw2, bw3, bw4, bw5, bw6;
    String af1, af2, af3, af4, af5;
    int cid1, cid2, cid3 = 0;
    String lv;

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
        setContentView(R.layout.activity_qu_main);

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
        imgview = findViewById(R.id.imglayout);
        numview = findViewById(R.id.qcntscore);
        lvView= (TextView)findViewById(R.id.lvView);

        imgview.setVisibility(View.GONE);
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
        level = extras.getString("level");
        qztext = extras.getString("qztext");

        ques.setText(qztext);
        maxcnt.setText("∞");
        Thread worker = new Thread() {
            public void run() {
                // 소켓연결
                try {
                    socket = new Socket("192.168.0.4", 8266);
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 서버에서 넘어오는 데이터 처리
                try {
                    while (true) {
                        serverMSG = socket_in.readLine();
                        System.out.println(socket.getReceiveBufferSize());
                        arExample.clear();
                        arSelect.clear();
                        System.out.println(serverMSG);

                        StringTokenizer stk = new StringTokenizer(serverMSG, "!");


                        if (type.equals("Btype")) { // 4 정답
                            tempA = stk.nextToken();
                            sel4 = stk.nextToken();
                            lv=stk.nextToken();
                            example = stk.nextToken();

                            StringTokenizer atk = new StringTokenizer(tempA, "/");
                            Log.d("tempA", tempA);

                            sel1 = atk.nextToken();
                            sel2 = atk.nextToken();
                            sel3 = atk.nextToken();

                            StringTokenizer idtk1 = new StringTokenizer(example, "$");
                            StringTokenizer idtk2 = new StringTokenizer(sel1, "$");
                            StringTokenizer idtk3 = new StringTokenizer(sel2, "$");
                            StringTokenizer idtk4 = new StringTokenizer(sel3, "$");
                            StringTokenizer idtk5 = new StringTokenizer(sel4, "$");


                            example = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());
                            if (keyB.equals("culture")) {
                                cid1 = qid1;
                            }

                            sel1 = idtk2.nextToken();
                            qid2 = Integer.parseInt(idtk2.nextToken());

                            sel2 = idtk3.nextToken();
                            qid3 = Integer.parseInt(idtk3.nextToken());

                            sel3 = idtk4.nextToken();
                            qid4 = Integer.parseInt(idtk4.nextToken());

                            sel4 = idtk5.nextToken();
                            qid5 = Integer.parseInt(idtk5.nextToken());

                            qid6 = 0;
                            mType1 = keyB;
                            mType2 = "null";



                        } else if (type.equals("Atype")) { // 4 정답
                            tempA = stk.nextToken();
                            sel4 = stk.nextToken();
                            lv= stk.nextToken();
                            example = stk.nextToken();

                            StringTokenizer atk = new StringTokenizer(tempA, "/");
                            Log.d("tempA", tempA);
                            sel1 = atk.nextToken();
                            sel2 = atk.nextToken();
                            sel3 = atk.nextToken();


                            StringTokenizer idtk1 = new StringTokenizer(example, "$");
                            StringTokenizer idtk2 = new StringTokenizer(sel1, "$");
                            StringTokenizer idtk3 = new StringTokenizer(sel2, "$");
                            StringTokenizer idtk4 = new StringTokenizer(sel3, "$");
                            StringTokenizer idtk5 = new StringTokenizer(sel4, "$");

                            example = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());
                            if (keyB.equals("culture")) {
                                cid1 = qid1;

                                }

                            sel1 = idtk2.nextToken();
                            qid2 = Integer.parseInt(idtk2.nextToken());

                            sel2 = idtk3.nextToken();
                            qid3 = Integer.parseInt(idtk3.nextToken());

                            sel3 = idtk4.nextToken();
                            qid4 = Integer.parseInt(idtk4.nextToken());

                            sel4 = idtk5.nextToken();
                            qid5 = Integer.parseInt(idtk5.nextToken());

                            qid6 = 0;
                            mType1 = keyC;
                            mType2 = keyB;

                        }
                        else if(type.equals("Ctype")){
                            if (keyA.equals("sort")) { // 4 정답 (나, 가, 다)
                            st1 = stk.nextToken();
                            st2 = stk.nextToken();
                            st3 = stk.nextToken();
                            lv = stk.nextToken();


                            StringTokenizer stk1 = new StringTokenizer(st1, "$");
                            StringTokenizer stk2 = new StringTokenizer(st2, "$");
                            StringTokenizer stk3 = new StringTokenizer(st3, "$");

                            st1 = stk1.nextToken();
                            cid1 = Integer.parseInt(stk1.nextToken());

                            st2 = stk2.nextToken();
                            cid2 = Integer.parseInt(stk2.nextToken());

                            st3 = stk3.nextToken();
                            cid3 = Integer.parseInt(stk3.nextToken());



                            qid1 = cid1;
                            qid2 = cid2;
                            qid3 = cid3;
                            qid4 = 0;
                            qid5 = 0;
                            qid6 = 0;
                            mType1 = keyB;
                            mType2 = "null";

                            example = "가. "+st1;
                            example2 = "나. "+st2;
                            example3 = "다. "+st3;
                            sel1 = "가 -> 나 -> 다";
                            sel2 = "가 -> 다 -> 나";
                            sel3 = "다 -> 나 -> 가";
                            sel4 = "나 -> 가 -> 다";
                        }

                        else if (keyA.equals("between")) { // 4 정답
                            bw1 = stk.nextToken();
                            bw2 = stk.nextToken();
                            bw3 = stk.nextToken();
                            bw4 = stk.nextToken();
                            bw5 = stk.nextToken();
                            bw6 = stk.nextToken();
                            lv = stk.nextToken();

                            StringTokenizer idtk1 = new StringTokenizer(bw1, "$");
                            StringTokenizer idtk2 = new StringTokenizer(bw2, "$");
                            StringTokenizer idtk3 = new StringTokenizer(bw3, "$");
                            StringTokenizer idtk4 = new StringTokenizer(bw4, "$");
                            StringTokenizer idtk5 = new StringTokenizer(bw5, "$");
                            StringTokenizer idtk6 = new StringTokenizer(bw6, "$");

                            bw1 = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());

                            bw2 = idtk2.nextToken();
                            qid2 = Integer.parseInt(idtk2.nextToken());

                            bw3 = idtk3.nextToken();
                            qid3 = Integer.parseInt(idtk3.nextToken());

                            bw4 = idtk4.nextToken();
                            qid4 = Integer.parseInt(idtk4.nextToken());

                            bw5 = idtk5.nextToken();
                            qid5 = Integer.parseInt(idtk5.nextToken());

                            bw6 = idtk6.nextToken();
                            qid6 = Integer.parseInt(idtk6.nextToken());

                            if (keyB.equals("culture")) {
                                cid1 = qid2;
                                cid2 = qid3;
                            }

                            mType1 = keyB;
                            mType2 = "null";

                            example = "가. "+bw2;
                            example2 = "나. "+bw3;
                            sel1 = bw4;
                            sel2 = bw5;
                            sel3 = bw6;
                            sel4 = bw1;
                        } else if (keyA.equals("after")) { // 정답 4
                            af1 = stk.nextToken();
                            af2 = stk.nextToken();
                            af3 = stk.nextToken();
                            af4 = stk.nextToken();
                            af5 = stk.nextToken();
                            lv = stk.nextToken();

                            StringTokenizer idtk1 = new StringTokenizer(af1, "$");
                            StringTokenizer idtk2 = new StringTokenizer(af2, "$");
                            StringTokenizer idtk3 = new StringTokenizer(af3, "$");
                            StringTokenizer idtk4 = new StringTokenizer(af4, "$");
                            StringTokenizer idtk5 = new StringTokenizer(af5, "$");

                            af1 = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());

                            af2 = idtk2.nextToken();
                            qid2 = Integer.parseInt(idtk2.nextToken());

                            af3 = idtk3.nextToken();
                            qid3 = Integer.parseInt(idtk3.nextToken());

                            af4 = idtk4.nextToken();
                            qid4 = Integer.parseInt(idtk4.nextToken());

                            af5 = idtk5.nextToken();
                            qid5 = Integer.parseInt(idtk5.nextToken());

                            qid6 = 0;

                            if (keyB.equals("culture")) {
                                cid1 = qid3;
                            }

                            mType1 = keyB;
                            mType2 = "null";

                            example = af3;
                            sel1 = af2;
                            sel2 = af4;
                            sel3 = af5;
                            sel4 = af1;
                        }
                        }
                        arExample.add(example);
                        if (keyA.equals("between")) {
                            arExample.add(example2);
                        } else if (keyA.equals("sort")) {
                            arExample.add(example2);
                            arExample.add(example3);
                        }
                        arSelect.add(sel1);
                        arSelect.add(sel2);
                        arSelect.add(sel3);
                        arSelect.add(sel4);

                        Collections.shuffle(arSelect);
////////////////////////////////////////////////////////////////////////////////////////////이미지출력/////////

                        if (type.equals("Atype") && keyB.equals("culture")) {

                            System.out.println("이미지출력준비");
                            System.out.println(cid1);
                            is=socket.getInputStream();
                            System.out.println("이미지출력준비2");
                            System.out.println(is);
                            System.out.println(socket.getReceiveBufferSize());
                            ois = new ObjectInputStream(is);//이 라인부터 안되는것 같음

                            System.out.println("이미지출력준비3");

                            cimg= (byte[])ois.readObject();
                            System.out.println("이미지출력준비4");

                            Bitmap bitmap=null;
                            bitmap=BitmapFactory.decodeByteArray(cimg,0,cimg.length);
                            System.out.println("이미지출력준비5");
                            iv.setImageBitmap(bitmap);
                           //img(cid1, 0, 0,lv);
                            //imageLoaderTask.execute();
                            System.out.println("이미지출력완료");


                            System.out.println("이미지소켓닫음");













                        } else if (type.equals("Ctype") && keyB.equals("culture")) {
                            if (keyA.equals("sort")) {

                                img(cid1, cid2, cid3,lv);
                                imageLoaderTask.execute();
                                imageLoaderTask2.execute();
                                imageLoaderTask3.execute();
                            } else if (keyA.equals("between")) {
                                img (cid1, cid2, 0,lv);
                                imageLoaderTask.execute();
                                imageLoaderTask2.execute();
                            } else if (keyA.equals("after")) {
                                img (cid1, 0, 0,lv);
                                imageLoaderTask.execute();
                            }
                        }
                        if(type.equals("Ctype")) {
                            if (keyA.equals("sort") || keyA.equals("between") || keyA.equals("after")) {
                                c1.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        c1.setText("");
                                        c2.setText("");
                                        c3.setText("");
                                        lvView.setText("");
                                        if (keyA.equals("sort")) {
                                            c1.setText(st1);
                                            c2.setText(st2);
                                            c3.setText(st3);
                                            lvView.setText(lv);
                                        } else if (keyA.equals("between")) {
                                            c1.setText(bw2);
                                            c2.setText(bw3);
                                            lvView.setText(lv);
                                        } else if (keyA.equals("after")) {
                                            c1.setText(af3);
                                            lvView.setText(lv);
                                        }
                                    }
                                });
                            }
                        }
                        if (type.equals("Atype") || type.equals("Btype")) {
                            c1.post(new Runnable() {
                                @Override
                                public void run() {
                                    c1.setText("");
                                    c2.setText("");
                                    c3.setText("");
                                    lvView.setText("");
                                    if (type.equals("Btype")  && keyB.equals("culture")) {
                                        c1.setText(example);
                                        lvView.setText(lv);
                                    } else if (type.equals("Atype") && keyC.equals("culture")) {
                                        c1.setText(example);
                                        lvView.setText(lv);
                                    }
                                    if (type.equals("Btype")  && keyB.equals("event")) {
                                        c1.setText(example);
                                        lvView.setText(lv);
                                    } else if (type.equals("Atype") && keyC.equals("event")) {
                                        c1.setText(example);
                                        lvView.setText(lv);
                                    }
                                }
                            });
                        }


                        ExList.post(new Runnable() {
                            @Override
                            public void run() {
                                ExList.setVisibility(View.VISIBLE);
                                imgview.setVisibility(View.VISIBLE);

                                if (type.equals("Btype") && keyB.equals("culture")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);

                                } else if (type.equals("Atype") && keyC.equals("culture")){
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                } else if (type.equals("Ctype") && keyB.equals("culture")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                } else {
                                    ExList.setAdapter(ExAdapter);
                                    imgview.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        };

        // 다음문제
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qLiveCount += 1;

                sendbtn.setText("다음문제");
                nowcnt.setText(String.valueOf(qLiveCount));
                ques.setVisibility(View.VISIBLE);
                numview.setVisibility(View.VISIBLE);

                if (sel4 != null) {
                    if (SelList.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
                        int pos = SelList.getCheckedItemPosition();
                        seltext = (String) SelList.getItemAtPosition(pos);
                        if (seltext != null) {
                            if (seltext.equals(sel4)) {
                                oxresult.add("o");
                                okcount += 100;
                                score.setText(String.valueOf(okcount));
                            } else {
                                oxresult.add("x");
                            }
                        } else {
                            oxresult.add("x");
                        }
                    } else if (SelList.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
                        SparseBooleanArray selectedList = SelList.getCheckedItemPositions();
                        StringBuilder sb = new StringBuilder();
                        if (selectedList.size() == 2) {
                            for (int i = 0; i < selectedList.size(); i++) {
                                int position = selectedList.keyAt(i);
                                if (selectedList.get(position)) {
                                    seltext = (String) SelList.getItemAtPosition(position);
                                    sb.append(seltext);
                                    sb.append("/");
                                }
                            }

                            if (sb.toString().equals(sel3 + "/" + sel4 + "/") || sb.toString().equals(sel4 + "/" + sel3 + "/")) {
                                oxresult.add("o");
                                okcount += 100;
                                score.setText(String.valueOf(okcount));
                            } else {
                                oxresult.add("x");
                            }
                        } else {
                            oxresult.add("x");
                        }
                    }
                }

                if (qLiveCount == 1) {
                    oxresult.add(0, "null");
                    qzcontent.add(0, "null");
                } else {
                    //ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
                    qzcontent.add(mType1 + "$" + mType2 + "$" + qid1 + "$" + qid2 + "$" + qid3 + "$" + qid4 + "$" + qid5 + "$" + qid6);
                }

                typemsg = type + "/" + keyA + "/" + keyB + "/" + keyC + "/" + level;
                if (typemsg != null)
                    socket_out.println(typemsg);


            }
        });
        worker.start();

    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            onStop();
            System.exit(0);
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
/////////////////////////////////////////////////////////////////////////////////
    protected void img(int a, int b, int c,String clv) {
        if (b == 0 && c == 0) {
            imageLoaderTask = new ImageLoaderTask(
                    iv, "ftp://192.168.130.4:21/" + clv + "/" + a + ".png"
            );

        } else if (c == 0) {
                imageLoaderTask = new ImageLoaderTask(
                        iv, "ftp://192.168.0.4:21/"+clv+"/"+a+".png"
                );
                imageLoaderTask2 = new ImageLoaderTask(
                        iv2, "ftp://192.168.0.4:21/"+clv+"/"+b+".png"
                );
            } else {
            imageLoaderTask = new ImageLoaderTask(
                    iv, "ftp://192.168.0.4:21/"+clv+"/"+a+".png"
            );
            imageLoaderTask2 = new ImageLoaderTask(
                    iv2, "ftp://192.168.0.4:21/"+clv+"/"+b+".png"
            );
            imageLoaderTask3 = new ImageLoaderTask(
                    iv3, "ftp://192.168.0.4:21/"+clv+"/"+c+".png"
            );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(getApplicationContext(), SelectOptionMenu2.class));
            finish();
            return true;
        } else if (id == R.id.finish) {
            Intent qz = new Intent(getApplicationContext(), ResultList.class);
            qz.putStringArrayListExtra("ox", oxresult);
            qz.putStringArrayListExtra("cont", qzcontent);
            startActivity(qz);
            onStop();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            /*if (type.equals("Atype") && keyB.equals("culture")) {
                ois.close();
                is.close();
            }*/
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
