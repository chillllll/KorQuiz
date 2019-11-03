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
/*
public class SelectOptionMenu extends AppCompatActivity {

    CharSequence[] sel_KP = { "시기", "인물" };
    CharSequence[] sel_EC = { "사건", "문화재" };
    CharSequence[] sel_E = { "사건" };
    CharSequence[] sel_K = { "시기" };
    CharSequence[] sel_C = { "문화재" };
    CharSequence[] sel_O = { "사건명", "사건내용" };

    Button typebtn, abtn, bbtn, cbtn, obtn, nextPage;
    Button typetv, atv, btv, ctv, otv;
    TextView qtv;
    View aView, bView, cView, oView;

    String i_type = "";
    String i_keyA = "";
    String i_keyB = "";
    String i_keyC = "";
    String i_oType = "";

    String t_type = "";
    String t_keyA = "";
    String t_keyB = "";
    String t_keyC = "";
    String t_oType = "";

    String qztext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        typebtn = (Button)findViewById(R.id.selTypeBtn);
        abtn = (Button)findViewById(R.id.selABtn);
        bbtn = (Button)findViewById(R.id.selBBtn);
        cbtn = (Button)findViewById(R.id.selCBTN);
        obtn = (Button)findViewById(R.id.selOtypeBTN);
        nextPage = (Button)findViewById(R.id.nextpage);

        typetv = (Button)findViewById(R.id.selTypeTv);
        atv = (Button)findViewById(R.id.selATv);
        btv = (Button)findViewById(R.id.selBTv);
        ctv = (Button)findViewById(R.id.selCTv);
        otv = (Button)findViewById(R.id.selOtypeTv);
        qtv = (TextView)findViewById(R.id.quesEx);

        aView = findViewById(R.id.selAview);
        bView = findViewById(R.id.selBview);
        cView = findViewById(R.id.selCview);
        oView = findViewById(R.id.selOview);

        aView.setVisibility(View.GONE);
        bView.setVisibility(View.GONE);
        cView.setVisibility(View.GONE);
        oView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        typebtn.setBackgroundResource(R.drawable.typebuttonnomal);
        typetv.setBackgroundResource(R.drawable.tvnomal);
        obtn.setBackgroundResource(R.drawable.otypebuttonover);
        otv.setBackgroundResource(R.drawable.tvover);

        qtv.setText("문제 출력 부분");

        typebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyA = "";
                i_keyB = "";
                i_keyC = "";
                i_oType = "";
                dlogType();
            }
        });

        abtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyB = "";
                i_keyC = "";
                i_oType = "";
                dlogKeyA();
            }
        });

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_keyC = "";
                i_oType = "";
                dlogKeyB();
            }
        });

        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i_oType = "";
                dlogKeyC();

            }
        });

        obtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlogOtype();
                nextPage.invalidate();
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i_type) {
                    case "AB1E1A":
                        t_type = "AB1E1A";
                        break;
                    case "AB1E2A":
                        t_type = "AB1E2A";
                        break;
                    case "ABC1E1A":
                        t_type = "ABC1E1A";
                        break;
                    case "순서정렬":
                        t_type = "sort";
                        break;
                    case "Between":
                        t_type = "between";
                        break;
                    case "After":
                        t_type = "after";
                        break;
                    case "단답형":
                        t_type = "dandap";
                        break;
                }

                switch (i_keyA) {
                    case "시기":
                        t_keyA = "king";
                        break;
                    case "인물":
                        t_keyA = "people";
                        break;
                    case "사건":
                        t_keyA = "event";
                        break;
                    case "문화재":
                        t_keyA = "culture";
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
                    case "문화재":
                        t_keyC = "culture";
                        break;
                    case "사건":
                        t_keyC = "event";
                        break;
                    case "":
                        t_keyC = "null";
                        break;
                }

                switch (i_oType) {
                    case "사건명":
                        t_oType = "title";
                        break;
                    case "사건내용":
                        t_oType = "content";
                        break;
                    case "":
                        t_oType = "null";
                        break;
                }

                Intent idata = new Intent(getApplicationContext(), SelectMainQuiz.class);
                idata.putExtra("type", t_type);
                idata.putExtra("keyA", t_keyA);
                idata.putExtra("keyB", t_keyB);
                idata.putExtra("keyC", t_keyC);
                idata.putExtra("oType", t_oType);
                idata.putExtra("qztext", qztext);
                startActivity(idata);
                finish();
            }
        });
    }


    private void dlogType() {
        final CharSequence[] tArr = { "AB1E1A", "AB1E2A", "ABC1E1A", "순서정렬", "Between", "After" };
        AlertDialog.Builder tAlert = new AlertDialog.Builder(this);
        tAlert.setTitle("유형 선택");

        aView.invalidate();
        bView.invalidate();
        cView.invalidate();
        oView.invalidate();
        nextPage.invalidate();

        atv.setText("");
        btv.setText("");
        ctv.setText("");
        otv.setText("");
        qtv.setText("");

        aView.setVisibility(View.GONE);
        bView.setVisibility(View.GONE);
        cView.setVisibility(View.GONE);
        oView.setVisibility(View.GONE);
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

                if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
                    aView.setVisibility(View.VISIBLE);
                    abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                    atv.setBackgroundResource(R.drawable.tvmid);
                    bView.setVisibility(View.VISIBLE);
                    bbtn.setBackgroundResource(R.drawable.keybbuttonover);
                    btv.setBackgroundResource(R.drawable.tvover);
                } else if (i_type.equals("ABC1E1A")) {
                    aView.setVisibility(View.VISIBLE);
                    abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                    atv.setBackgroundResource(R.drawable.tvmid);
                    bView.setVisibility(View.VISIBLE);
                    bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                    btv.setBackgroundResource(R.drawable.tvmid);
                    cView.setVisibility(View.VISIBLE);
                    cbtn.setBackgroundResource(R.drawable.keycbuttonover);
                    ctv.setBackgroundResource(R.drawable.tvover);
                } else if (i_type.equals("순서정렬") || i_type.equals("Between") || i_type.equals("After") || i_type.equals("단답형")) {
                    aView.setVisibility(View.VISIBLE);
                    abtn.setBackgroundResource(R.drawable.keyabuttonover);
                    atv.setBackgroundResource(R.drawable.tvover);
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
        otv.setText("");
        qtv.setText("");

        oView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        abtn.invalidate();
        atv.invalidate();
        bbtn.invalidate();
        btv.invalidate();
        cbtn.invalidate();
        ctv.invalidate();

        if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
            bbtn.setBackgroundResource(R.drawable.keybbuttonover);
            btv.setBackgroundResource(R.drawable.tvover);

            aAlert.setSingleChoiceItems(sel_KP, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_KP[item]);
                    i_keyA = sel_KP[item].toString();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();
        } else if (i_type.equals("단답형")) {
            abtn.setBackgroundResource(R.drawable.keyabuttonover);
            atv.setBackgroundResource(R.drawable.tvover);

            aAlert.setSingleChoiceItems(sel_C, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_C[item]);
                    i_keyA = sel_C[item].toString();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();
        } else if (i_type.equals("ABC1E1A")) {

            bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
            btv.setBackgroundResource(R.drawable.tvmid);

            cbtn.setBackgroundResource(R.drawable.keycbuttonover);
            ctv.setBackgroundResource(R.drawable.tvover);

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
        } else if (i_type.equals("순서정렬") || i_type.equals("Between") || i_type.equals("After")) {

            abtn.setBackgroundResource(R.drawable.keyabuttonover);
            atv.setBackgroundResource(R.drawable.tvover);

            aAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    atv.setText(sel_EC[item]);
                    i_keyA = sel_EC[item].toString();
                    nextPage.invalidate();
                    oView.invalidate();
                    abtn.invalidate();
                    atv.invalidate();

                    if (i_keyA.equals("사건")) {
                        oView.setVisibility(View.VISIBLE);
                        abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                        atv.setBackgroundResource(R.drawable.tvmid);
                    }
                    if (i_keyA != null && i_keyA != "사건") {
                        nextPage.setVisibility(View.VISIBLE);
                    } else {
                        nextPage.setVisibility(View.INVISIBLE);
                    }
                    if (i_type.equals("순서정렬")) {
                        if (i_keyA.equals("사건")) {
                            qtv.setText("보기의 사건이 발생한 순서대로 바르게 정렬된것을 고르시오");
                            qztext = "보기의 사건이 발생한 순서대로 바르게 정렬된것을 고르시오";
                        } else if (i_keyA.equals("문화재")) {
                            qtv.setText("보기의 문화재가 제작된 순서대로 바르게 정렬된것을 고르시오");
                            qztext = "보기의 문화재가 제작된 순서대로 바르게 정렬된것을 고르시오";
                        }
                    }

                    if (i_type.equals("Between")) {
                        if (i_keyA.equals("사건")) {
                            qtv.setText("보기의 두 사건 사이에 발생한 사건으로 옳은것을 고르시오");
                            qztext = "보기의 두 사건 사이에 발생한 사건으로 옳은것을 고르시오";
                        } else if (i_keyA.equals("문화재")) {
                            qtv.setText("보기의 두 문화재 사이에 제작된 문화재로 옳은것을 고르시오");
                            qztext = "보기의 두 문화재 사이에 제작된 문화재로 옳은것을 고르시오";
                        }
                    }

                    if (i_type.equals("After")) {
                        if (i_keyA.equals("사건")) {
                            qtv.setText("보기의 사건 이후에 발생한 사건을 고르시오");
                            qztext = "보기의 사건 이후에 발생한 사건을 고르시오";
                        } else if (i_keyA.equals("문화재")) {
                            qtv.setText("보기의 문화재가 제작된 이후에 만들어진 문화재를 고르시오");
                            qztext = "보기의 문화재가 제작된 이후에 만들어진 문화재를 고르시오";
                        }
                    }
                    qtv.invalidate();
                    dialog.cancel();
                }
            });
            AlertDialog alert = aAlert.create();
            alert.show();
        }
    }

    private void dlogKeyB() {
        AlertDialog.Builder bAlert = new AlertDialog.Builder(this);
        bAlert.setTitle("선택지 주제");

        btv.setText("");
        ctv.setText("");
        otv.setText("");
        qtv.setText("");

        oView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        if (i_type.equals("ABC1E1A")) {
            cbtn.setBackgroundResource(R.drawable.keycbuttonover);
            ctv.setBackgroundResource(R.drawable.tvover);
        } else if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
            bbtn.setBackgroundResource(R.drawable.keybbuttonover);
            btv.setBackgroundResource(R.drawable.tvover);
        }

        if (i_keyA.equals("시기")) {
            bAlert.setSingleChoiceItems(sel_EC, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    btv.setText(sel_EC[item]);
                    i_keyB = sel_EC[item].toString();
                    nextPage.invalidate();
                    oView.invalidate();
                    abtn.invalidate();
                    atv.invalidate();
                    bbtn.invalidate();
                    btv.invalidate();
                    cbtn.invalidate();
                    ctv.invalidate();
                    if (i_keyB.equals("사건")) {
                        oView.setVisibility(View.VISIBLE);
                        if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                        } else if (i_type.equals("ABC1E1A")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                            cbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                            ctv.setBackgroundResource(R.drawable.tvmid);
                        }
                    }

                    if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
                        if (i_keyA != null && i_keyB != null && i_keyB != "사건") {
                            nextPage.setVisibility(View.VISIBLE);
                        } else {
                            nextPage.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (i_type.equals("AB1E1A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건")) {
                            qtv.setText("보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        } else {
                            qtv.setText("보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 고르시오");
                            qztext = "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 고르시오";
                        }
                    } else if (i_type.equals("AB1E1A") && i_keyA.equals("인물")) {
                        qtv.setText("보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오");
                        qztext = "보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오";
                    }

                    if (i_type.equals("AB1E2A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건")) {
                            qtv.setText("보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 모두 고르시오");
                            qztext = "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 모두 고르시오";
                        } else {
                            qtv.setText("보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 모두 고르시오");
                            qztext = "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 모두 고르시오";
                        }
                    } else if (i_type.equals("AB1E2A") && i_keyA.equals("인물")) {
                        qtv.setText("보기의 사건을 일으킨 인물과 관련이 없는 사건을 모두 고르시오");
                        qztext = "보기의 사건을 일으킨 인물과 관련이 없는 사건을 모두 고르시오";
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
                    oView.invalidate();
                    abtn.invalidate();
                    atv.invalidate();
                    bbtn.invalidate();
                    btv.invalidate();
                    cbtn.invalidate();
                    ctv.invalidate();

                    if (i_keyB.equals("사건")) {
                        oView.setVisibility(View.VISIBLE);
                        if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                        } else if (i_type.equals("ABC1E1A")) {
                            abtn.setBackgroundResource(R.drawable.keyabuttonmid);
                            atv.setBackgroundResource(R.drawable.tvmid);
                            bbtn.setBackgroundResource(R.drawable.keybbuttonmid);
                            btv.setBackgroundResource(R.drawable.tvmid);
                            cbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                            ctv.setBackgroundResource(R.drawable.tvmid);
                        }
                    }

                    if (i_type.equals("AB1E1A") || i_type.equals("AB1E2A")) {
                        if (i_keyA != null && i_keyB != null && i_keyB != "사건") {
                            nextPage.setVisibility(View.VISIBLE);
                        } else {
                            nextPage.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (i_type.equals("AB1E1A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건")) {
                            qtv.setText("보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        } else {
                            qtv.setText("보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 고르시오");
                            qztext = "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 고르시오";
                        }
                    } else if (i_type.equals("AB1E1A") && i_keyA.equals("인물")) {
                        qtv.setText("보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오");
                        qztext = "보기의 사건을 일으킨 인물과 관련이 없는 사건을 고르시오";
                    }

                    if (i_type.equals("AB1E2A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건")) {
                            qtv.setText("보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 모두 고르시오");
                            qztext = "보기의 사건과 같은 시기(왕)에 발생한 사건이 아닌것을 모두 고르시오";
                        } else {
                            qtv.setText("보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 모두 고르시오");
                            qztext = "보기의 문화재와 동일한 시기(왕)에 만들어진 문화재가 아닌것을 모두 고르시오";
                        }
                    } else if (i_type.equals("AB1E2A") && i_keyA.equals("인물")) {
                        qtv.setText("보기의 사건을 일으킨 인물과 관련이 없는 사건을 모두 고르시오");
                        qztext = "보기의 사건을 일으킨 인물과 관련이 없는 사건을 모두 고르시오";
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
        cAlert.setTitle("보기 주제");
        ctv.setText("");
        otv.setText("");
        qtv.setText("");

        if (i_keyB != "사건")
            oView.setVisibility(View.GONE);
        nextPage.setVisibility(View.INVISIBLE);

        if (i_keyB.equals("사건")) {

            cbtn.setBackgroundResource(R.drawable.keycbuttonmid);
            ctv.setBackgroundResource(R.drawable.tvmid);

            cAlert.setSingleChoiceItems(sel_C, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    ctv.setText(sel_C[item]);
                    i_keyC = sel_C[item].toString();

                    if (i_type.equals("ABC1E1A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건") && i_keyC.equals("문화재")) {
                            qtv.setText("보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        } else if (i_keyB.equals("문화재") && i_keyC.equals("사건")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오";
                        }
                    }
                    qtv.invalidate();

                    dialog.cancel();
                }
            });
            AlertDialog clert = cAlert.create();
            clert.show();
        } else if (i_keyB.equals("문화재")) {
            cAlert.setSingleChoiceItems(sel_E, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    ctv.setText(sel_E[item]);
                    i_keyC = sel_E[item].toString();
                    nextPage.invalidate();
                    oView.invalidate();

                    if (i_keyC.equals("사건")) {
                        cbtn.setBackgroundResource(R.drawable.keycbuttonmid);
                        ctv.setBackgroundResource(R.drawable.tvmid);
                        oView.setVisibility(View.VISIBLE);
                    }

                    if (i_type.equals("ABC1E1A") && i_keyA.equals("시기")) {
                        if (i_keyB.equals("사건") && i_keyC.equals("문화재")) {
                            qtv.setText("보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오");
                            qztext = "보기의 문화재가 제작된 시기(왕)에 발생한 사건이 아닌것을 고르시오";
                        } else if (i_keyB.equals("문화재") && i_keyC.equals("사건")) {
                            qtv.setText("보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오");
                            qztext = "보기의 사건이 발생한 시기(왕)에 제작된 문화재가 아닌것을 고르시오";
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

    private void dlogOtype() {
        AlertDialog.Builder oAlert = new AlertDialog.Builder(this);
        oAlert.setTitle("사건 출력 형태");
        otv.setText("");

        nextPage.invalidate();
        nextPage.setVisibility(View.INVISIBLE);

        oAlert.setSingleChoiceItems(sel_O, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                otv.setText(sel_O[item]);
                i_oType = sel_O[item].toString();

                if (i_oType != null) {
                    nextPage.setVisibility(View.VISIBLE);
                } else {
                    nextPage.setVisibility(View.INVISIBLE);
                }
                dialog.cancel();
            }
        });
        AlertDialog olert = oAlert.create();
        olert.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }
}





*/