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
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016-05-07.
 */
public class RandomMainQuiz extends AppCompatActivity{

    //뒤로 버튼 2회시 종료 위함
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    String[] rType = { "Atype","Btype","Ctype" };
    String[] rKeyA = { "king", "people", "event"};
    String[] rKeyB = { "event", "event", "event"};
    String[] rKeyC = { "event", "event", "event"};
    String[] rOtype = {"title", "content"};

    String[] qText = { "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오",

            "보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오",
            "보기의 사건 이후에 발생한 사건을 고르시오"};
    // 랜덤 문제유형 위한 배열
    /*String[] rType = { "AB1E1A", "AB1E1A", "AB1E1A",
                       "AB1E2A", "AB1E2A", "AB1E2A",
                       "ABC1E1A", "ABC1E1A",
                       "sort", "sort",
                       "between", "between",
                       "after", "after" };
    String[] rKeyA = { "king", "king", "people",
                       "king", "king", "people",
                       "king", "king",
                       "event", "culture",
                       "event", "culture",
                       "event", "culture"};
    String[] rKeyB = { "event", "culture", "event",
                       "event", "culture", "event",
                       "event", "culture", "null",
                       "null", "null",
                       "null", "null",
                       "null", "null"};
    String[] rKeyC = { "null", "null", "null",
                       "null", "null", "null",
                       "culture", "event",
                       "null", "null",
                       "null", "null",
                       "null", "null"};
    String[] rOtype = {"title", "content"};

    String[] qText = { "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오",
                       "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 고르시오",
                       "보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오",

                       "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 모두 고르시오",
                       "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 모두 고르시오",
                       "보기의 사건을 일으킨 인물과 관련이 없는 사건을 모두 고르시오",

                       "보기의 문화재가 제작된 시기(왕)에 일어난 사건이 아닌것을 고르시오",
                       "보기의 사건이 일어난 시기(왕)에 제작된 문화재가 아닌것을 고르시오",

                       "보기의 사건이 발생한 순서대로 바르게 정렬된것을 고르시오",
                       "보기의 문화재가 제작된 순서대로 바르게 정렬된것을 고르시오",

                       "보기의 두 사건 사이에 발생한 사건으로 옳은것을 고르시오",
                       "보기의 두 문화재 사이에 제작된 문화재로 옳은것을 고르시오",

                       "보기의 사건 이후에 발생한 사건을 고르시오",
                       "보기의 문화재가 제작된 이후에 만들어진 문화재를 고르시오"};
*/


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
    String type, keyA, keyB, keyC, oType;
    String typemsg = "";

    // 서버에서 받은 정보
    String serverMSG = "";

    // 서버에서 받은 데이터 처리
    String tempA;
    String example, example2, example3, sel1, sel2, sel3, sel4;
    String st1, st2, st3;
    String bw1, bw2, bw3, bw4, bw5, bw6;
    String order1,order2,order3,order4,order5,order6;
    //String af1, af2, af3, af4, af5,af6;
    int cid1, cid2, cid3;

    ImageLoaderTask imageLoaderTask;
    ImageLoaderTask imageLoaderTask2;
    ImageLoaderTask imageLoaderTask3;

    ImageView iv, iv2, iv3;

    // 결과 페이지로 넘겨줌
    ArrayList<String> oxresult = new ArrayList<>();
    ArrayList<String> qzcontent= new ArrayList<>();
    int qid1, qid2, qid3, qid4, qid5, qid6;
    String mType1, mType2;

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

        imgview.setVisibility(View.GONE);
        numview.setVisibility(View.GONE);
        ques.setVisibility(View.GONE);

        c1 = (TextView)findViewById(R.id.tv);
        c2 = (TextView)findViewById(R.id.tv2);
        c3 = (TextView)findViewById(R.id.tv3);

        iv = (ImageView)findViewById(R.id.iv);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);

        final Bundle extras = getIntent().getExtras();
        qMaxCount = extras.getInt("rcnt");

        maxcnt.setText(String.valueOf(qMaxCount));

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

                        arExample.clear();
                        arSelect.clear();

                        //ExAdapter.notifyDataSetChanged();
                        //SelAdapter.notifyDataSetChanged();

                        StringTokenizer stk = new StringTokenizer(serverMSG, "!");

                        if (type.equals("Atype")) {
                        //if (type.equals("AB1E1A")) { // 4 정답
                            tempA = stk.nextToken();
                            sel4 = stk.nextToken();

                            StringTokenizer atk = new StringTokenizer(tempA, "/");
                            example = atk.nextToken();
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
                            if (keyB.equals("event")) {
                            //if (keyB.equals("culture")) {
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

                        } else if (type.equals("Btype")) {
                        // /else if (type.equals("AB1E2A")) { // 3,4 정답
                            tempA = stk.nextToken();
                            sel3 = stk.nextToken();
                            sel4 = stk.nextToken();

                            StringTokenizer atk = new StringTokenizer(tempA, "/");
                            example = atk.nextToken();
                            sel1 = atk.nextToken();
                            sel2 = atk.nextToken();

                            StringTokenizer idtk1 = new StringTokenizer(example, "$");
                            StringTokenizer idtk2 = new StringTokenizer(sel1, "$");
                            StringTokenizer idtk3 = new StringTokenizer(sel2, "$");
                            StringTokenizer idtk4 = new StringTokenizer(sel3, "$");
                            StringTokenizer idtk5 = new StringTokenizer(sel4, "$");

                            example = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());
                            if (keyB.equals("event")) {
                            //if (keyB.equals("culture")) {
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

                        } /*else if (type.equals("ABC1E1A")) { // 4 정답
                            tempA = stk.nextToken();
                            sel4 = stk.nextToken();
                            example = stk.nextToken();

                            StringTokenizer atk = new StringTokenizer(tempA, "/");
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
                            mType2 = keyC;

                        } else if (type.equals("sort")) { // 4 정답 (나, 가, 다)
                            st1 = stk.nextToken();
                            st2 = stk.nextToken();
                            st3 = stk.nextToken();


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
                            mType1 = keyA;
                            mType2 = "null";

                            example = "가. "+st1;
                            example2 = "나. "+st2;
                            example3 = "다. "+st3;
                            sel1 = "가 -> 나 -> 다";
                            sel2 = "가 -> 다 -> 나";
                            sel3 = "다 -> 나 -> 가";
                            sel4 = "나 -> 가 -> 다";
                        } else if (type.equals("between")) { // 4 정답
                            bw1 = stk.nextToken();
                            bw2 = stk.nextToken();
                            bw3 = stk.nextToken();
                            bw4 = stk.nextToken();
                            bw5 = stk.nextToken();
                            bw6 = stk.nextToken();

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

                            if (keyA.equals("culture")) {
                                cid1 = qid2;
                                cid2 = qid3;
                            }

                            mType1 = keyA;
                            mType2 = "null";

                            example = "가. "+bw2;
                            example2 = "나. "+bw3;
                            sel1 = bw4;
                            sel2 = bw5;
                            sel3 = bw6;
                            sel4 = bw1;
                        } */  else if (type.equals("Ctype")){ // 정답 4
                    //else if (type.equals("after")) { // 정답 4
                            order1 = stk.nextToken();
                            order2 = stk.nextToken();
                            order3 = stk.nextToken();
                            order4 = stk.nextToken();
                            order5 = stk.nextToken();
                            order6 = stk.nextToken();

                            StringTokenizer idtk1 = new StringTokenizer(order1, "$");
                            StringTokenizer idtk2 = new StringTokenizer(order2, "$");
                            StringTokenizer idtk3 = new StringTokenizer(order3, "$");
                            StringTokenizer idtk4 = new StringTokenizer(order4, "$");
                            StringTokenizer idtk5 = new StringTokenizer(order5, "$");
                            StringTokenizer idtk6 = new StringTokenizer(order6, "$");


                            order1 = idtk1.nextToken();
                            qid1 = Integer.parseInt(idtk1.nextToken());

                            order2= idtk2.nextToken();
                            qid2 = Integer.parseInt(idtk2.nextToken());

                            order3= idtk3.nextToken();
                            qid3 = Integer.parseInt(idtk3.nextToken());

                            order4= idtk4.nextToken();
                            qid4 = Integer.parseInt(idtk4.nextToken());

                            order5= idtk5.nextToken();
                            qid5 = Integer.parseInt(idtk5.nextToken());

                            order6= idtk6.nextToken();  //추가
                            qid6 = Integer.parseInt(idtk6.nextToken());

                            //qid6 = 0;


                            if (keyA.equals("event")) {
                            //if (keyA.equals("culture")) {
                                cid1 = qid3;
                            }

                            mType1 = keyA;
                            mType2 = "null";

                            example = order6;
                            sel1 = order2;
                            sel2 = order4;
                            sel3 = order5;
                            sel4 = order1;
                        }
                        /*
                        arExample.add(example);
                        if (type.equals("between")) {
                            arExample.add(example2);
                        } else if (type.equals("sort")) {
                            arExample.add(example2);
                            arExample.add(example3);
                        }
                        arSelect.add(sel1);
                        arSelect.add(sel2);
                        arSelect.add(sel3);
                        arSelect.add(sel4);

                        Collections.shuffle(arSelect);

//                        ExAdapter.notifyDataSetChanged();
//                        SelAdapter.notifyDataSetChanged();

                        if ((type.equals("AB1E1A") || type.equals("AB1E2A")) && keyB.equals("culture")) {
                            img(cid1, 0, 0);
                            imageLoaderTask.execute();

                        } else if (type.equals("ABC1E1A") && keyC.equals("culture")) {
                            img(cid1, 0, 0);
                            imageLoaderTask.execute();
                        } else if ((type.equals("sort") || type.equals("between") || type.equals("after")) && keyA.equals("culture")) {
                            if (type.equals("sort")) {
                                img(cid1, cid2, cid3);
                                imageLoaderTask.execute();
                                imageLoaderTask2.execute();
                                imageLoaderTask3.execute();
                            } else if (type.equals("between")) {
                                img (cid1, cid2, 0);
                                imageLoaderTask.execute();
                                imageLoaderTask2.execute();
                            } else if (type.equals("after")) {
                                img (cid1, 0, 0);
                                imageLoaderTask.execute();
                            }
                        }
                        */
                        if (type.equals("Ctype")) {
                            c1.post(new Runnable() {
                                @Override
                                public void run() {
                                        c1.setText(order6);
                                    }
                            });
                        }
                        /*if (type.equals("sort") || type.equals("between") || type.equals("after")) {
                            c1.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (type.equals("sort")) {
                                        c1.setText(st1);
                                        c2.setText(st2);
                                        c3.setText(st3);
                                    } else if (type.equals("between")) {
                                        c1.setText(bw2);
                                        c2.setText(bw3);
                                    } else if (type.equals("after")) {
                                        c1.setText(af3);
                                    }
                                }
                            });
                        }*/
                        if (type.equals("Atype") || type.equals("Btype")) {
                            c1.post(new Runnable() {
                                @Override
                                public void run() {
                                    if ((type.equals("Atype") || type.equals("Btype")) && keyB.equals("event")) {
                                        c1.setText(example);
                                    }
                                }
                            });
                        }
                        /*if (type.equals("AB1E1A") || type.equals("AB1E2A") || type.equals("ABC1E1A")) {
                            c1.post(new Runnable() {
                                @Override
                                public void run() {
                                    if ((type.equals("AB1E1A") || type.equals("AB1E2A")) && keyB.equals("culture")) {
                                        c1.setText(example);
                                    } else if (type.equals("ABC1E1A") && keyC.equals("culture")) {
                                        c1.setText(example);
                                    }
                                }
                            });
                        }*/

                        ExList.post(new Runnable() {
                            @Override
                            public void run() {
                                ExList.setVisibility(View.VISIBLE);
                                imgview.setVisibility(View.VISIBLE);

                                if ((type.equals("Atype") || type.equals("Btype")) && keyB.equals("event")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                                } if ((type.equals("Ctype")) && keyA.equals("event")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                } else {
                                    ExList.setAdapter(ExAdapter);
                                    imgview.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


                                /*if ((type.equals("AB1E1A") || type.equals("AB1E2A")) && keyB.equals("culture")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    if (type.equals("AB1E2A"))
                                        SelList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                    SelList.setAdapter(SelAdapter);
                                } else if (type.equals("ABC1E1A") && keyC.equals("culture")){
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                } else if ((type.equals("sort") || type.equals("between") || type.equals("after")) && keyA.equals("culture")) {
                                    ExList.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    SelList.setAdapter(SelAdapter);
                                } else {
                                    ExList.setAdapter(ExAdapter);
                                    imgview.setVisibility(View.GONE);
                                    SelList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    if (type.equals("AB1E2A"))
                                        SelList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                    SelList.setAdapter(SelAdapter);*/
                                }
                            }
                        });
                    }
                } catch (IOException e) {
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

                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(R.drawable.white);
                    }
                });

                iv2.post(new Runnable() {
                    @Override
                    public void run() {
                        iv2.setImageResource(R.drawable.white);
                    }
                });

                iv3.post(new Runnable() {
                    @Override
                    public void run() {
                        iv3.setImageResource(R.drawable.white);
                    }
                });

                c1.post(new Runnable() {
                    @Override
                    public void run() {
                        c1.setText("");
                    }
                });

                c2.post(new Runnable() {
                    @Override
                    public void run() {
                        c2.setText("");
                    }
                });

                c3.post(new Runnable() {
                    @Override
                    public void run() {
                        c3.setText("");
                    }
                });


                if (sel4 != null) {
                    if (SelList.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
                        int pos = SelList.getCheckedItemPosition();
                        seltext = (String) SelList.getItemAtPosition(pos);
                        if (seltext != null) {
                            if (seltext.equals(sel4)) {
                                //Toast.makeText(getApplicationContext(), "정답입니다", Toast.LENGTH_SHORT).show();
                                oxresult.add("o");
                                okcount += 100;
                                score.setText(String.valueOf(okcount));
                            } else {
                                //Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();
                                oxresult.add("x");
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(getApplicationContext(), "정답입니다", Toast.LENGTH_SHORT).show();
                                oxresult.add("o");
                                okcount += 100;
                                score.setText(String.valueOf(okcount));
                            } else {
                                //Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();
                                oxresult.add("x");
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();
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

                if (qLiveCount > qMaxCount) {
                    Intent qz = new Intent(getApplicationContext(), ResultList.class);
                    qz.putStringArrayListExtra("ox", oxresult);
                    qz.putStringArrayListExtra("cont", qzcontent);
                    startActivity(qz);
                    onStop();
                    finish();
                }

                Random rnd = new Random();
                int s = rnd.nextInt(3);
                ques.setText(qText[s]);
                type = rType[s];
                keyA = rKeyA[s];
                keyB = rKeyB[s];
                keyC = rKeyC[s];
                if (keyA.equals("event") || keyB.equals("event") || keyC.equals("event")) {
                    int ss = rnd.nextInt(2);
                    oType = rOtype[ss];
                } else {
                    oType = "null";
                }
                typemsg = type + "/" + keyA + "/" + keyB + "/" + keyC + "/" + oType;
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
            //onStop();
            System.exit(0);
            //super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void img(int a, int b, int c) {
        if (c == 0) {
            imageLoaderTask = new ImageLoaderTask(
                    iv, "ftp://203.250.77.122:21/"+a+".png"
            );
            imageLoaderTask2 = new ImageLoaderTask(
                    iv2, "ftp://203.250.77.122:21/"+b+".png"
            );
        } else if (b == 0 && c == 0) {
            imageLoaderTask = new ImageLoaderTask(
                    iv, "ftp://203.250.77.122:21/"+a+".png"
            );
        } else {
            imageLoaderTask = new ImageLoaderTask(
                    iv, "ftp://203.250.77.122:21/"+a+".png"
            );
            imageLoaderTask2 = new ImageLoaderTask(
                    iv2, "ftp://203.250.77.122:21/"+b+".png"
            );
            imageLoaderTask3 = new ImageLoaderTask(
                    iv3, "ftp://203.250.77.122:21/"+c+".png"
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
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
            return true;
        } else if (id == R.id.finish){
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
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}