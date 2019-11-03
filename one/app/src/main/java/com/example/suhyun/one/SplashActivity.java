package com.example.suhyun.one;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016-05-05.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000); // 3초간 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 메인 액티비티로 넘어감
        startActivity(new Intent(this, SelectOptionMenu2.class));
        finish();
    }

}
