package com.example.opensource1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;

import android.os.Build;
import android.os.Bundle;
import org.json.simple.JSONArray;
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

import android.os.Bundle;

public class Weather extends AppCompatActivity {
    // 데이터를 저장할 객체와 화면에 텍스트뷰 오브젝트
    data input = new data();
    TextView nowH, nowC, tvH, tvC, nowSky; // nowH가 습도, nowC가 온도, tvH, tvC가 한글 텍스트 의미, nowSky가 현재 날씨 텍스트 의미
    // 현재 시간을 API 마다 필요한 형식의 STRING으로 제공하는 클래스
    NowTime t = new NowTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 액션바 삭제

        // 각 UI 객체 할당
        nowH = findViewById(R.id.nowH);
        nowC = findViewById(R.id.nowC);
        nowSky = findViewById(R.id.nowSky);
        tvC = findViewById(R.id.nowCtv);
        tvH = findViewById(R.id.nowHtv);

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    // Weather API 가져오기
                    input.getWeather(null);
                } catch (IOException /*| JSONException*/ | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nowC.setText(input.weather_data.t1h); // 현 기온 텍스트 설정
                        nowH.setText(input.weather_data.reh); // 현 습도 텍스트 설정
                        switch (Integer.parseInt(input.weather_data.pty))
                        {
                            case 0: nowSky.setText("맑음"); break;
                            case 3:
                            case 7: nowSky.setText("눈"); break;
                            default: nowSky.setText("비"); break;
                        }
                    }
                });
            }
        })).start();
    }
}
