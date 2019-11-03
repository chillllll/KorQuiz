package com.example.suhyun.one;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-05-09.
 */
public class ResultList extends AppCompatActivity {

    //뒤로 버튼 2회시 종료 위함
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    ArrayList<String> oxresult = new ArrayList<>();
    ArrayList<String> qzcontents = new ArrayList<>();
    ArrayList<MyResult> arMyresult = new ArrayList<MyResult>();

    ListView resultList;
    Button homebtn;

    int cnt = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras = getIntent().getExtras();
        oxresult = extras.getStringArrayList("ox");
        qzcontents = extras.getStringArrayList("cont");

        //ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
        //qzcontent.add(mType1 + "/" + mType2 + "/" + qid1 + "/" + qid2 + "/" + qid3 + "/" + qid4 + "/" + qid5 + "/" + qid6);

        homebtn = (Button)findViewById(R.id.homebtn);

        MyResult myresult;
        MyResult mycontent;

        for (int i=1; i < oxresult.size(); i++) {
            myresult = new MyResult(oxresult.get(i));
            arMyresult.add(myresult);
        }

        MyResultAdapter adapter = new MyResultAdapter(this, R.layout.list_view, arMyresult);
        resultList = (ListView)findViewById(R.id.resultListView);
        resultList.setAdapter(adapter);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelectOptionMenu2.class));
                finish();
            }
        });
    }

    class MyResult {
        String ox;

        MyResult (String aOX) {
            ox = aOX;
        }
    }

    class MyResultAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflacter;
        ArrayList<MyResult> arD;
        int layout;

        public MyResultAdapter(Context context, int alayout, ArrayList<MyResult> aarD) {
            con = context;
            inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arD = aarD;
            layout = alayout;
        }

        @Override
        public int getCount() {
            return arD.size();
        }

        @Override
        public Object getItem(int position) {
            return arD.get(position).ox;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflacter.inflate(layout, parent, false);
            }

            TextView txt = (TextView) convertView.findViewById(R.id.listtv);

            cnt += 1;

            if (arD.get(position).ox.equals("o")) {
                txt.setText(String.valueOf(position+1));
                txt.setBackgroundColor(Color.rgb(222,250,222));
            } else {
                txt.setText(String.valueOf(position+1));
                txt.setBackgroundColor(Color.rgb(250,222,222));
            }

            Button btn = (Button) convertView.findViewById(R.id.solbtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent rs = new Intent(getApplicationContext(), ResultView.class);
                    rs.putExtra("conts", qzcontents.get(position+1).toString());
                    startActivity(rs);
                }
            });
            return convertView;
        }
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            System.exit(0);
            //super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
