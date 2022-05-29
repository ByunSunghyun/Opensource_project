package com.example.opensource1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;

import android.os.Build;
import android.os.Bundle;
import org.json.simple.JSONArray;

import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.JSONParser;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.time.ZoneId;

import java.sql.Date;

public class Covid extends AppCompatActivity {
    //데이터와 시간에 대한 클래스 인스턴스 생성
    data input = new data();
    NowTime t = new NowTime();
    ImageView chunsik, cold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 액션바 삭제

        // 각 UI에 오브젝트 지정
        chunsik = findViewById(R.id.nowChun);
        cold = findViewById(R.id.nowCold);

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    //각 API 데이터 받아오기
                    input.get_cold(null);
                    input.get_Allergy_Chunsik(null);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 각 위험도에 따른 이미지 설정
                        switch (Integer.parseInt(input.cold_data.today_val))
                        {
                            case 0: cold.setImageResource(R.drawable.emoji0); break;
                            case 1: cold.setImageResource(R.drawable.emoji1); break;
                            case 2: cold.setImageResource(R.drawable.emoji2); break;
                            case 3: cold.setImageResource(R.drawable.emoji3); break;
                            default: break;
                        }
                        switch (Integer.parseInt(input.chunsik_data.today_val))
                        {
                            case 0: chunsik.setImageResource(R.drawable.emoji0); break;
                            case 1: chunsik.setImageResource(R.drawable.emoji1); break;
                            case 2: chunsik.setImageResource(R.drawable.emoji2); break;
                            case 3: chunsik.setImageResource(R.drawable.emoji3); break;
                            default: break;
                        }
                    }
                });
            }
        })).start();
    }
}