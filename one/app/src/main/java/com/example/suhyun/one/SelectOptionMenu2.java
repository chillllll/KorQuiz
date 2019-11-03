package com.example.suhyun.one;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-05-07.
 */

public class SelectOptionMenu2 extends AppCompatActivity {

    CharSequence[] sel_SAB = {"sort", "after", "between"};
    CharSequence[] sel_KP = {"시기", "인물"};
    CharSequence[] sel_EC = {"사건", "문화재"};
    CharSequence[] sel_E = {"사건"};
    CharSequence[] sel_K = {"시기"};
    CharSequence[] sel_P = {"인물"};
    CharSequence[] sel_C = {"문화재"};
    CharSequence[] sel_L = {"1단계", "2단계", "3단계"};

    Button typebtn, abtn, bbtn, cbtn, lbtn, nextPage;
    Button typetv, atv, btv, ctv, ltv;
    TextView qtv;
    View aView, bView, cView, lView;

    String i_type = "";
    String i_keyA = "";
    String i_keyB = "";
    String i_keyC = "";
    String i_level = "";


    String t_type = "";
    String t_keyA = "";
    String t_keyB = "";
    String t_keyC = "";
    String t_level = "";

    String qztext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        typebtn = (Button) findViewById(R.id.selTypeBtn);
        abtn = (Button) findViewById(R.id.selABtn);
        bbtn = (Button) findViewById(R.id.selBBtn);
        cbtn = (Button) findViewById(R.id.selCBtn);
        lbtn = (Button) findViewById(R.id.selLBtn);
        nextPage = (Button) findViewById(R.id.nextpage);

        typetv = (Button) findViewById(R.id.selTypeTv);
        atv = (Button) findViewById(R.id.selATv);
        btv = (Button) findViewById(R.id.selBTv);
        ctv = (Button) findViewById(R.id.selCTv);
        ltv = (Button) findViewById(R.id.selLTv);
        qtv = (TextView) findViewById(R.id.quesEx);

        aView = findViewById(R.id.selAview);
        bView = findViewById(R.id.selBview);
        cView = findViewById(R.id.selCview);
        lView = findViewById(R.id.selLview);

        aView.setVisibility(View.GONE);
        bView.setVisibility(View.GONE);
        cView.setVisibility(View.GONE);
        lView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        typebtn.setBackgroundResource(R.drawable.typebuttonnomal);
        typetv.setBackgroundResource(R.drawable.tvnomal);


        qtv.setText("문제 출력 부분");

        typebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyA = "";
                i_keyB = "";
                i_keyC = "";
                i_level = "";
                dlogType();
            }
        });

        abtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyB = "";
                i_keyC = "";
                i_level = "";
                dlogKeyA();
            }
        });

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyC = "";
                i_level = "";
                dlogKeyB();
            }
        });

        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_level = "";
                dlogKeyC();
            }
        });
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlogLevel();
                nextPage.invalidate();
            }
        });


        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i_type) {
                    case "Atype":
                        t_type = "Atype";
                        break;
                    case "Btype":
                        t_type = "Btype";
                        break;
                    case "Ctype":
                        t_type = "Ctype";
                        break;

                }

                switch (i_keyA) {
                    case "시기":
                        t_keyA = "king";
                        break;
                    case "인물":
                        t_keyA = "people";
                        break;
                    case "sort":
                        t_keyA = "sort";
                        break;
                    case "after":
                        t_keyA = "after";
                        break;
                    case "between":
                        t_keyA = "between";
                        break;
                    case "":
                        t_keyA = "null";
                        break;
                }

                switch (i_keyB) {
                    case "사건":
                        t_keyB = "event";
                        break;
                    case "문화재":
                        t_keyB = "culture";
                        break;
                    case "":
                        t_keyB = "null";
                        break;
                }

                switch (i_keyC) {
                    case "사건":
                        t_keyC = "event";
                        break;
                    case "문화재":
                        t_keyC = "culture";
                        break;
                    case "":
                        t_keyC = "null";
                        break;
                }

                switch (i_level) {
                    case "1단계":
                        t_level = "1";
                        break;
                    case "2단계":
                        t_level = "2";
                        break;
                    case "3단계":
                        t_level = "3";
                        break;
                    case "":
                        t_keyC = "null";
                        break;
                }


                Intent idata = new Intent(getApplicationContext(), SelectMainQuiz.class);
                idata.putExtra("type", t_type);
                idata.putExtra("keyA", t_keyA);
                idata.putExtra("keyB", t_keyB);
                idata.putExtra("keyC", t_keyC);
                idata.putExtra("level", t_level);
                idata.putExtra("qztext", qztext);
                startActivity(idata);
                finish();
            }
        });
    }


    private void dlogType() {
        final CharSequence[] tArr = {"Atype", "Btype", "Ctype"};
        AlertDialog.Builder tAlert = new AlertDialog.Builder(this);
        tAlert.setTitle("유형 선택");

        aView.invalidate();
        bView.invalidate();
        cView.invalidate();
        lView.invalidate();
        nextPage.invalidate();

        atv.setText("");
        btv.setText("");
        ctv.setText("");
        ltv.setText("");
        qtv.setText("");

        aView.setVisibility(View.GONE);
        bView.setVisibility(View.GONE);
        cView.setVisibility(View.GONE);
        lView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        typebtn.setBackgroundResource(R.drawable.typebuttonnomal);
        typetv.setBackgroundResource(R.drawable.tvnomal);

        tAlert.setSingleChoiceItems(tArr, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                typetv.setText(tArr[item]);
                i_type = tArr[item].toString();

                typebtn.setBackgroundResource(R.drawable.typebutton);
                typetv.setBackgroundResource(R.drawable.tvtop);

                if (i_type.equals("Atype") || i_type.equals("Btype")) {
                    aView.setVisibility(View.VISIBLE);
                    abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                    atv.setBackgroundResource(R.drawable.tvmid);
                    bView.setVisibility(View.VISIBLE);
                    bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                    btv.setBackgroundResource(R.drawable.tvmid);
                    cView.setVisibility(View.VISIBLE);
                    cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                    ctv.setBackgroundResource(R.drawable.tvmid);
                    lView.setVisibility(View.VISIBLE);
                    lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                    ltv.setBackgroundResource(R.drawable.tvover);
                } else if (i_type.equals("Ctype")) {

                    aView.setVisibility(View.VISIBLE);
                    abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                    atv.setBackgroundResource(R.drawable.tvmid);
                    bView.setVisibility(View.VISIBLE);
                    bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                    btv.setBackgroundResource(R.drawable.tvmid);
                    cView.setVisibility(View.VISIBLE);
                    cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                    ctv.setBackgroundResource(R.drawable.tvmid);
                    lView.setVisibility(View.VISIBLE);
                    lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                    ltv.setBackgroundResource(R.drawable.tvover);

                }
                dialog.cancel();
            }
        });
        AlertDialog tlert = tAlert.create();
        tlert.show();
    }

    private void dlogKeyA() {
        AlertDialog.Builder aAlert = new AlertDialog.Builder(this);
        aAlert.setTitle("주제 선택");

        atv.setText("");
        btv.setText("");
        ctv.setText("");
        ltv.setText("");

        qtv.setText("");

        nextPage.setVisibility(View.INVISIBLE);

        abtn.invalidate();
        atv.invalidate();
        bbtn.invalidate();
        btv.invalidate();
        cbtn.invalidate();
        ctv.invalidate();
        lbtn.invalidate();
        ltv.invalidate();

        if (i_type.equals("Atype")) {

            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
            btv.setBackgroundResource(R.drawable.tvmid);
            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
            ctv.setBackgroundResource(R.drawable.tvmid);
            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
            ltv.setBackgroundResource(R.drawable.tvover);

            aAlert.setSingleChoiceItems(sel_K, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_K[item]);
                    i_keyA = sel_K[item].toString();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();
        } else if (i_type.equals("Btype")) {

            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
            btv.setBackgroundResource(R.drawable.tvmid);
            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
            ctv.setBackgroundResource(R.drawable.tvmid);
            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
            ltv.setBackgroundResource(R.drawable.tvover);

            aAlert.setSingleChoiceItems(sel_P, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_P[item]);
                    i_keyA = sel_P[item].toString();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();
        } else if (i_type.equals("Ctype")) {

            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
            btv.setBackgroundResource(R.drawable.tvmid);
            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
            ctv.setBackgroundResource(R.drawable.tvmid);
            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
            ltv.setBackgroundResource(R.drawable.tvover);
            aAlert.setSingleChoiceItems(sel_SAB, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_SAB[item]);
                    i_keyA = sel_SAB[item].toString();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();

        }
    }

    private void dlogKeyB() {
        AlertDialog.Builder bAlert = new AlertDialog.Builder(this);
        bAlert.setTitle("보기 주제");

        btv.setText("");
        ctv.setText("");
        ltv.setText("");
        qtv.setText("");
        nextPage.setVisibility(View.INVISIBLE);


        if (i_keyA.equals("시기")) {
            bAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    btv.setText(sel_EC[item]);
                    i_keyB = sel_EC[item].toString();
                    nextPage.invalidate();
                    abtn.invalidate();
                    atv.invalidate();
                    bbtn.invalidate();
                    btv.invalidate();
                    cbtn.invalidate();
                    ctv.invalidate();
                    lbtn.invalidate();
                    ltv.invalidate();
                    if (i_keyB.equals("사건")) {
                        if (i_type.equals("Atype")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            ctv.setBackgroundResource(R.drawable.tvmid);
                            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                            ltv.setBackgroundResource(R.drawable.tvover);
                        }
                    } else if (i_keyB.equals("문화재")) {
                        if (i_type.equals("Atype")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            ctv.setBackgroundResource(R.drawable.tvmid);
                            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                            ltv.setBackgroundResource(R.drawable.tvover);
                        }
                    }

                    qtv.invalidate();

                    dialog.cancel();
                }
            });
            AlertDialog blert = bAlert.create();
            blert.show();
        } else if (i_keyA.equals("인물")) {
            bAlert.setSingleChoiceItems(sel_E, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    btv.setText(sel_E[item]);
                    i_keyB = sel_E[item].toString();
                    nextPage.invalidate();
                    abtn.invalidate();
                    atv.invalidate();
                    bbtn.invalidate();
                    btv.invalidate();
                    cbtn.invalidate();
                    ctv.invalidate();
                    lbtn.invalidate();
                    ltv.invalidate();

                    if (i_keyB.equals("사건")) {

                        if (i_type.equals("Btype")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                            cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            ctv.setBackgroundResource(R.drawable.tvmid);
                            lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                            ltv.setBackgroundResource(R.drawable.tvover);
                        }
                    }


                    qtv.invalidate();

                    dialog.cancel();
                }
            });
            AlertDialog blert = bAlert.create();
            blert.show();
        } else if (i_keyA.equals("sort") || i_keyA.equals("after") || i_keyA.equals("between")) {
            bAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    btv.setText(sel_EC[item]);
                    i_keyB = sel_EC[item].toString();
                    nextPage.invalidate();
                    abtn.invalidate();
                    atv.invalidate();
                    bbtn.invalidate();
                    btv.invalidate();
                    cbtn.invalidate();
                    ctv.invalidate();
                    lbtn.invalidate();
                    ltv.invalidate();
                    if (i_keyB.equals("사건") && i_type.equals("Ctype")) {
                        abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                        atv.setBackgroundResource(R.drawable.tvmid);
                        bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                        btv.setBackgroundResource(R.drawable.tvmid);
                        cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                        ctv.setBackgroundResource(R.drawable.tvmid);
                        lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                        ltv.setBackgroundResource(R.drawable.tvover);

                    } else if (i_keyB.equals("문화재") && i_type.equals("Ctype")) {
                        abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                        atv.setBackgroundResource(R.drawable.tvmid);
                        bbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                        btv.setBackgroundResource(R.drawable.tvmid);
                        cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                        ctv.setBackgroundResource(R.drawable.tvmid);
                        lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                        ltv.setBackgroundResource(R.drawable.tvover);

                    }

                    qtv.invalidate();

                    dialog.cancel();
                }
            });
            AlertDialog blert = bAlert.create();
            blert.show();


        }
    }

    private void dlogKeyC() {
        AlertDialog.Builder cAlert = new AlertDialog.Builder(this);
        cAlert.setTitle("선택지 주제");
        ctv.setText("");
        ltv.setText("");
        qtv.setText("");


        if (i_type.equals("Atype") && i_keyA.equals("시기")) {
            if (i_keyB.equals("사건")) {

                cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                ctv.setBackgroundResource(R.drawable.tvmid);
                lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                ltv.setBackgroundResource(R.drawable.tvover);


                cAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ctv.setText(sel_EC[item]);
                        i_keyC = sel_EC[item].toString();
                        nextPage.invalidate();
                        lView.invalidate();
                        lView.setVisibility(View.VISIBLE);


                        if (i_keyB.equals("사건") && i_keyC.equals("문화재")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오";
                        } else if (i_keyB.equals("사건") && i_keyC.equals("사건")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        }


                        qtv.invalidate();

                        dialog.cancel();
                    }
                });
                AlertDialog clert = cAlert.create();
                clert.show();

            } else if (i_keyB.equals("문화재")) {
                cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                ctv.setBackgroundResource(R.drawable.tvmid);
                lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                ltv.setBackgroundResource(R.drawable.tvover);

                cAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ctv.setText(sel_EC[item]);
                        i_keyC = sel_EC[item].toString();
                        nextPage.invalidate();
                        lView.invalidate();
                        lView.setVisibility(View.VISIBLE);


                        if (i_keyB.equals("문화재") && i_keyC.equals("문화재")) {
                            qtv.setText("보기의 문화재가 제작된 시기(왕)에 제작된 문화재가 아닌것을 고르시오");
                            qztext = "보기의 문화재가 제작된 시기(왕)에 제작된 문화재가 아닌것을 고르시오";
                        } else if (i_keyB.equals("문화재") && i_keyC.equals("사건")) {
                            qtv.setText("보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        }
                        qtv.invalidate();

                        dialog.cancel();
                    }

                });
                AlertDialog clert = cAlert.create();
                clert.show();

            }
        } else if (i_type.equals("Btype") && i_keyA.equals("인물")) {
            if (i_keyB.equals("사건")) {

                cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                ctv.setBackgroundResource(R.drawable.tvmid);
                lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                ltv.setBackgroundResource(R.drawable.tvover);

                cAlert.setSingleChoiceItems(sel_E, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ctv.setText(sel_E[item]);
                        i_keyC = sel_E[item].toString();
                        nextPage.invalidate();
                        lView.invalidate();
                        lView.setVisibility(View.VISIBLE);


                        if (i_keyB.equals("사건") && i_keyC.equals("문화재")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오";
                        } else if (i_keyB.equals("사건") && i_keyC.equals("사건")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        }


                        qtv.invalidate();

                        dialog.cancel();
                    }
                });
                AlertDialog clert = cAlert.create();
                clert.show();

            }

        } else if (i_type.equals("Ctype") && (i_keyA.equals("sort") || i_keyA.equals("after") || i_keyA.equals("between"))) {
            if (i_keyB.equals("사건")) {
                cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                ctv.setBackgroundResource(R.drawable.tvmid);
                lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                ltv.setBackgroundResource(R.drawable.tvover);

                cAlert.setSingleChoiceItems(sel_E, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ctv.setText(sel_E[item]);
                        i_keyC = sel_E[item].toString();
                        nextPage.invalidate();
                        lView.invalidate();
                        lView.setVisibility(View.VISIBLE);

                        if (i_keyA.equals("sort")) {


                            if (i_keyC.equals("사건")) {
                                qtv.setText("보기의 사건이 발생한 순서대로 바르게 정렬된것을 고르시오");
                                qztext = "보기의 사건이 발생한 순서대로 바르게 정렬된것을 고르시오";
                            }
                        } else if (i_keyA.equals("after")) {


                            if (i_keyC.equals("사건")) {
                                qtv.setText("보기의 사건이 발생한 이후에 발생한 사건을 고르시오");
                                qztext = "보기의 사건이 발생한 이후에 발생한 사건을 고르시오";
                            }
                        } else if (i_keyA.equals("between")) {


                            if (i_keyC.equals("사건")) {
                                qtv.setText("보기의 두 사건이 발생한 시간 사이에 발생한 사건을 고르시오");
                                qztext = "보기의 두 사건이 발생한 시간 사이에 발생한 사건을 고르시오";
                            }
                        }
                        qtv.invalidate();

                        dialog.cancel();
                    }

                });
                AlertDialog clert = cAlert.create();
                clert.show();
            } else if (i_keyB.equals("문화재")) {
                cbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                ctv.setBackgroundResource(R.drawable.tvmid);
                lbtn.setBackgroundResource(R.drawable.levelbuttonover2);
                ltv.setBackgroundResource(R.drawable.tvover);

                cAlert.setSingleChoiceItems(sel_C, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        ctv.setText(sel_C[item]);
                        i_keyC = sel_C[item].toString();
                        nextPage.invalidate();
                        lView.invalidate();
                        lView.setVisibility(View.VISIBLE);


                        if (i_keyA.equals("sort")) {

                            if (i_keyC.equals("문화재")) {
                                qtv.setText("보기의 문화재가 제작된 순서대로 바르게 정렬된것을 고르시오");
                                qztext = "보기의 문화재가 제작된 순서대로 바르게 정렬된것을 고르시오";
                            }
                        } else if (i_keyA.equals("after")) {

                            if (i_keyC.equals("문화재")) {
                                qtv.setText("보기의 문화재가 제작된 이후에 제작된 문화재를 고르시오");
                                qztext = "보기의 문화재가 제작된 이후에 제작된 문화재를 고르시오";
                            }
                        } else if (i_keyA.equals("between")) {

                            if (i_keyC.equals("문화재")) {
                                qtv.setText("보기의 두 문화재가 제작된 시간 사이에에 제작된 문화재를 고르시오");
                                qztext = "보기의 두 문화재가 제작된 시간 사이에에 제작된 문화재를 고르시오";
                            }
                        }

                        qtv.invalidate();

                        dialog.cancel();
                    }

                });
                AlertDialog clert = cAlert.create();
                clert.show();
            }
        }
    }

    private void dlogLevel() {
        AlertDialog.Builder lAlert = new AlertDialog.Builder(this);
        lAlert.setTitle("난이도");
        ltv.setText("");

        nextPage.invalidate();
        nextPage.setVisibility(View.INVISIBLE);

        lAlert.setSingleChoiceItems(sel_L, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ltv.setText(sel_L[item]);
                i_level = sel_L[item].toString();

                if (i_level != null) {
                    nextPage.setVisibility(View.VISIBLE);
                } else {
                    nextPage.setVisibility(View.INVISIBLE);
                }
                dialog.cancel();
            }
        });
        AlertDialog llert = lAlert.create();
        llert.show();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),SelectOptionMenu2.class));
        finish();
    }
}

