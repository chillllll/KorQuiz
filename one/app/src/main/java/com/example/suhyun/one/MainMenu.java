package com.example.suhyun.one;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-05-05.
 */
public class MainMenu extends AppCompatActivity {

    //뒤로 버튼 2회시 종료 위함
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    Button selectMenu, randomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        randomMenu = (Button)findViewById(R.id.ranMenuBtn);
        selectMenu = (Button)findViewById(R.id.selMenuBtn);

        randomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RandomOptionMenu.class));
                finish();
            }
        });
        selectMenu.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              startActivity(new Intent(getApplicationContext(), SelectOptionMenu2.class));
                                              finish();
                                          }
                                      }
       /* selectMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelectOptionMenu.class));
                finish();
            }
        }*/
        );
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
