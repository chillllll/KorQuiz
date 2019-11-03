package com.example.suhyun.one;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016-05-23.
 */
public class ResultView extends AppCompatActivity {

    String resultContent;

    // 소켓연결 관련
    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;

    // 서버에서 받은 데이터 처리
    String serverMSG = "";
    String qid1R, qid2R, qid3R, qid4R, qid5R, qid6R;

    String sendMSG = "";

    ArrayList<MyResult> arMyresult = new ArrayList<MyResult>();

    ListView resultList;
    TextView updateTv;

    ImageLoaderTask imageLoaderTask;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);

        Bundle extras = getIntent().getExtras();
        resultContent = extras.getString("conts");

        //ABC1E1A : mType1 = keyB, mType2 = keyC, qid1 = keyC, qid2~5 = keyB
        //qzcontent.add(mType1 + "/" + mType2 + "/" + qid1 + "/" + qid2 + "/" + qid3 + "/" + qid4 + "/" + qid5 + "/" + qid6);
        Log.d("ContentLog", "" + resultContent);

        final MyResultAdapter adapter = new MyResultAdapter(this, R.layout.result_list_view, arMyresult);
        resultList = (ListView)findViewById(R.id.resultView);

        updateTv = (TextView)findViewById(R.id.update);

        arMyresult.clear();

        Thread worker = new Thread() {
            public void run() {
                //소켓연결
                try {
                    socket = new Socket("192.168.0.4", 8266);
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        serverMSG = socket_in.readLine();

                        MyResult myresult1, myresult2, myresult3, myresult4, myresult5, myresult6;

                        System.out.println("serverMSG : "+serverMSG);
                        StringTokenizer svtk = new StringTokenizer(serverMSG, "/");
                        Log.d("serverMSG",serverMSG);
                        qid1R = svtk.nextToken();
                        qid2R = svtk.nextToken();
                        qid3R = svtk.nextToken();
                        qid4R = svtk.nextToken();
                        qid5R = svtk.nextToken();
                        qid6R = svtk.nextToken();

                        myresult1 = new MyResult(qid1R);
                        arMyresult.add(myresult1);

                        myresult2 = new MyResult(qid2R);
                        arMyresult.add(myresult2);

                        myresult3 = new MyResult(qid3R);
                        arMyresult.add(myresult3);

                        myresult4 = new MyResult(qid4R);
                        arMyresult.add(myresult4);

                        myresult5 = new MyResult(qid5R);
                        arMyresult.add(myresult5);

                        myresult6 = new MyResult(qid6R);
                        arMyresult.add(myresult6);


                        resultList.post(new Runnable() {
                            @Override
                            public void run() {
                                resultList.setAdapter(adapter);
                            }
                        });

                    }
                } catch (IOException e) {
                }
            }
        };

        updateTv.post(new Runnable() {
            @Override
            public void run() {
                sendMSG = "sol/" + resultContent;
                Log.d("sendMSG: ", "" + sendMSG);
                if (sendMSG != null)
                    socket_out.println(sendMSG);
            }
        });
        worker.start();
    }

    class MyResult {
        String resultcontent;

        MyResult (String arsultcontent) {
            resultcontent = arsultcontent;
        }
    }

    class MyResultAdapter extends BaseAdapter {
        Context con;
        LayoutInflater inflacter;
        ArrayList<MyResult> arD;
        int layout;

        public MyResultAdapter (Context context, int alayout, ArrayList<MyResult> aarD) {
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
            return arD.get(position).resultcontent;
        }

        @Override
        public long getItemId (int position) {
            return position;
        }

        @Override
        public View getView (final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflacter.inflate (layout, parent, false);
            }

            String result1, result2, result3;
            int cid=0;

            TextView tv1 = (TextView) convertView.findViewById(R.id.resultTv1);
            TextView tv2 = (TextView) convertView.findViewById(R.id.resultTv2);
            TextView tv3 = (TextView) convertView.findViewById(R.id.resultTv3);
            ImageView iv = (ImageView) convertView.findViewById(R.id.resultImage);

            StringTokenizer state = new StringTokenizer(arD.get(position).resultcontent, "#");
            String stat = state.nextToken();
            String stat2 = state.nextToken();

            iv.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);

            if (stat.equals("culture")) {
                StringTokenizer tvtk = new StringTokenizer(stat2, "!");
                result1 = tvtk.nextToken();
                result2 = tvtk.nextToken();
                result3 = tvtk.nextToken();
                cid = Integer.parseInt(tvtk.nextToken());
                tv1.setText("이름 : "+result1);
                tv2.setText("년도, 시기 : "+result3);
                tv3.setText("설명 : "+result2);
                imageLoaderTask = new ImageLoaderTask(iv, "ftp://192.168.0.4:21//"+cid+".png");
                imageLoaderTask.execute();
            } else if (stat.equals("event")) {
                tv1.setText(stat2);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);
            } else if (stat.equals("null")) {
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);
            }

            return convertView;
        }
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
