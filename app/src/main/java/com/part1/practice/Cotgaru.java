package com.example.opensource1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;import androidx.annotation.RequiresApi;
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

import android.os.Bundle;


// 나중에 버튼 누르면 시작할 때 이외에도 다시 데이터가 갱신되도록 만든 클래스
public class Cotgaru extends AppCompatActivity {
    // 각 데이터 표시할 UI
    TextView soTreeTv, jabchoTv, chamTreeTv;
    ImageView soTree, jabcho, chamTree;

    // 데이터 저장하는 클래스
    data input = new data();
    // 현 날짜 기준 시간 데이터 형식 반환해주는 클래스
    NowTime t = new NowTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotgaru);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 액션바 삭제

        // 각 UI 오브젝트에 할당
        soTree = findViewById(R.id.nowSoTree);
        jabcho = findViewById(R.id.nowJabcho);
        chamTree = findViewById(R.id.nowChamTree);
        soTreeTv = findViewById(R.id.soTreetv);
        jabchoTv = findViewById(R.id.jabchotv);
        chamTreeTv = findViewById(R.id.chamTreetv);

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    // 알러지에 대한 API들 다시 받아오기
                    input.get_Allergy_charmTree(null);
                    input.get_Allergy_SoTree(null);
                    input.get_Allergy_Jopcho(null);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // 각 알러지 value에 따른 UI 이미지 변경
                        switch (Integer.parseInt(input.soTree_data.today_val))
                        {
                            case 0: soTree.setImageResource(R.drawable.emoji0); break;
                            case 1: soTree.setImageResource(R.drawable.emoji1); break;
                            case 2: soTree.setImageResource(R.drawable.emoji2); break;
                            case 3: soTree.setImageResource(R.drawable.emoji3); break;
                            default: break;
                        }
                        switch (Integer.parseInt(input.jopcho_data.today_val))
                        {
                            case 0: jabcho.setImageResource(R.drawable.emoji0); break;
                            case 1: jabcho.setImageResource(R.drawable.emoji1); break;
                            case 2: jabcho.setImageResource(R.drawable.emoji2); break;
                            case 3: jabcho.setImageResource(R.drawable.emoji3); break;
                            default: break;
                        }
                        switch (Integer.parseInt(input.charmTree_data.today_val))
                        {
                            case 0: chamTree.setImageResource(R.drawable.emoji0); break;
                            case 1: chamTree.setImageResource(R.drawable.emoji1); break;
                            case 2: chamTree.setImageResource(R.drawable.emoji2); break;
                            case 3: chamTree.setImageResource(R.drawable.emoji3); break;
                            default: break;
                        }
                    }
                });
            }
        })).start();
    }
}