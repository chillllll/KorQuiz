package com.example.suhyun.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Administrator on 2016-05-07.
 */
public class RandomOptionMenu extends AppCompatActivity {

    Button nextBtn;
    Spinner qcount;

    int cntInt = 0;
    String cntStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_menu);

        nextBtn = (Button)findViewById(R.id.ranNextBtn);
        qcount = (Spinner)findViewById(R.id.spinner_qcnt);

        ArrayAdapter qcountAdapter = ArrayAdapter.createFromResource(
                this, R.array.r_count, android.R.layout.simple_spinner_item);
        qcountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qcount.setAdapter(qcountAdapter);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntStr = String.valueOf(qcount.getSelectedItem());
                if (cntStr.equals("10 문제")) {
                    cntInt = 10;
                } else if (cntStr.equals("20 문제")) {
                    cntInt = 20;
                } else if (cntStr.equals("30 문제")) {
                    cntInt = 30;
                } else if (cntStr.equals("무제한")) {
                    cntInt = 99999;
                }
                Intent countData = new Intent(getApplicationContext(), RandomMainQuiz.class);
                countData.putExtra("rcnt", cntInt);
                Log.d("PUT", ""+cntInt);
                startActivity(countData);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        finish();
    }
}
