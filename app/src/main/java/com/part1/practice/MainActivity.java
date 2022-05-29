package com.example.opensource1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//여기까지 안드로이드 기본 제공하는 UI 라이브러리.


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//추가적으로 넣은 Java Simple 라이브러리. API에서 가져온 데이터 Parsing에 필요


import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.URLEncoder;
// 자바에서 기본적으로 제공하는 URL 이용에 필요한 라이브러리

import java.util.Timer;
import java.util.TimerTask;
// 팁 부분의 시간에 따른 순차적 변화를 위한 Timer 라이브러리 사용

public class MainActivity extends AppCompatActivity {

    private static MainActivity inst;
    TextView tip; //팁 UI
    LinearLayout back; //아이콘 배경 이미지 - 기본
    Button mdbtn, wtbtn, cotbtn, covidbtn; //제공하는 데이터에 따른 항목 조회 버튼
    ImageView mainImg; // 어플 아이콘 이미지

    String[] tipStr = new String[7]; // 안내할 팁에 대한 String 배열

    int y=0; // tipStr을 순회하는 용도의 itrator 역할
    int tip_num = 0; // 오늘 안내할 팁의 개수
    int sum = 0; // 오늘의 위험도를 총 계산한 값
    int sky, dust; // 오늘 날씨와 미세먼지 값

    data input = new data(); //각 API가 제공하는 데이터들을 담을 클래스를 따로 만들어서 사용

    NowTime t = new NowTime(); // 각 API가 필요로하는 형태로 날짜 및 시간의 String형태로 알맞게 가공하여 제공하는 클래스 따로 만들어서 사용

    Timer timer;
    TimerTask timerTask;
    // 팁을 일정 시간당 한번씩 바꿔서 보여주는 역할 수행 위해 timer 사용

    Thread setui = new Thread((new Runnable() { //UI 변경에 대한 수행 스레드
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 각 위험도에 따른 메인 화면에 표시되는 위험도 아이콘 이미지 변경
                    if((sum/4)==0){
                        mainImg.setImageResource(R.drawable.emoji0);
                    }
                    else if((sum/4)==1)
                    {
                        mainImg.setImageResource(R.drawable.emoji1);
                    }
                    else if((sum/4)==2)
                    {
                        mainImg.setImageResource(R.drawable.emoji2);
                    }
                    else
                    {
                        mainImg.setImageResource(R.drawable.emoji3);
                    }
                    // 오늘 날씨에 따른 날씨 데이터를 제공하는 창으로 이동하는 버튼 변경
                    if(sky<1)
                    {
                        wtbtn.setBackgroundResource(R.drawable.sunny);
                        back.setBackgroundResource(R.drawable.background_sunny);
                    }
                    else if(sky==3||sky==7)
                    {
                        back.setBackgroundResource(R.drawable.background_snow);
                        wtbtn.setBackgroundResource(R.drawable.snow);

                    }
                    else
                    {
                        back.setBackgroundResource(R.drawable.background_rain);
                        wtbtn.setBackgroundResource(R.drawable.rain);
                    }
                    // 오늘 미세먼지 농도에 따라 미세먼지에 대한 정확한 수치를 보여주는 창으로 이동하는 버튼 이미지 변경
                    if(dust==0){
                        mdbtn.setBackgroundResource(R.drawable.dust0);
                    }
                    else if(dust==1)
                    {
                        mdbtn.setBackgroundResource(R.drawable.dust1);
                    }
                    else if(dust==2)
                    {
                        mdbtn.setBackgroundResource(R.drawable.dust2);
                    }
                    else
                    {
                        mdbtn.setBackgroundResource(R.drawable.dust3);
                    }
                }

            });
        }
    }));

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inst = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 액션바 삭제

        // 각 UI객체 불러오기
        back = findViewById(R.id.back);
        tip = findViewById(R.id.tipText);
        mainImg = findViewById(R.id.mainImg);
        mdbtn = findViewById(R.id.mdButton);
        wtbtn = findViewById(R.id.weatherButton);
        covidbtn = findViewById(R.id.covidButton);
        cotbtn = findViewById(R.id.cotButton);

        // timer 생성
        timer = new Timer();

        //미세먼지 버튼 Listener 선언
        mdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Microdust.class); // 누를 때 마다 선언된 클래스의 역할을 수행하여 매번 갱신
                startActivity(intent);
            }
        });

        //소나무 참나무 잡초 알러지 항목 버튼 Listener 선언
        cotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Cotgaru.class); // 누를 때 마다 선언된 클래스의 역할을 수행하여 매번 갱신
                startActivity(intent);
            }
        });

        //날씨에 대한 항목 버튼 Listener 선언
        wtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Weather.class); // 누를 때 마다 선언된 클래스의 역할을 수행하여 매번 갱신
                startActivity(intent);
            }
        });

        //감기 천식 항목 버튼 Listener 선언
        covidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Covid.class); // 누를 때 마다 선언된 클래스의 역할을 수행하여 매번 갱신
                startActivity(intent);
            }
        });

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    // 각 API들 데이터를 불러옴
                    input.get_Allergy_charmTree(inst);
                    input.get_Allergy_SoTree(inst);
                    input.get_Allergy_Jopcho(inst);
                    input.getWeather(inst);
                    input.getAirKor(inst);
                    input.get_cold(inst);
                    input.get_Allergy_Chunsik(inst);

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Create 시에 처음으로 받아오는 데이터에 따른 이미지 변경
                        if((sum/4)==0){
                            mainImg.setImageResource(R.drawable.emoji0);
                        }
                        else if((sum/4)==1)
                        {
                            mainImg.setImageResource(R.drawable.emoji1);
                        }
                        else if((sum/4)==2)
                        {
                            mainImg.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            mainImg.setImageResource(R.drawable.emoji3);
                        }
                        if(sky<1)
                        {
                            wtbtn.setBackgroundResource(R.drawable.sunny);
                            back.setBackgroundResource(R.drawable.background_sunny);
                        }
                        else if(sky==3||sky==7)
                        {
                            back.setBackgroundResource(R.drawable.background_snow);
                            wtbtn.setBackgroundResource(R.drawable.snow);

                        }
                        else
                        {
                            back.setBackgroundResource(R.drawable.background_rain);
                            wtbtn.setBackgroundResource(R.drawable.rain);
                        }
                        if(dust==0){
                            mdbtn.setBackgroundResource(R.drawable.dust0);
                        }
                        else if(dust==1)
                        {
                            mdbtn.setBackgroundResource(R.drawable.dust1);
                        }
                        else if(dust==2)
                        {
                            mdbtn.setBackgroundResource(R.drawable.dust2);
                        }
                        else
                        {
                            mdbtn.setBackgroundResource(R.drawable.dust3);
                        }
                    }
                });
            }
        })).start();

        // 팁 문구에 대한 Timer 수행
        timerTask = new TimerTask() {
            @Override
            public void run() {
                tip.setText(tipStr[y]); // 팁 텍스트 갱신
                if(y==tip_num) { // 팁 텍스트 루프하기 위한 수행
                    y = -1;
                }
                y++;
                // API에 대한 정보가 버튼을 누르는 등의 수행으로 갱신되면 다시 UI를 갱신하도록 초에 한번씩 UI 갱신 수행
                if (setui.getState() == Thread.State.NEW)
                    setui.start();
            }
        };
        //Timer 수행
        timer.schedule(timerTask,1000,1000);
    }
}