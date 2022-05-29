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
                    getWeather(input);
                } catch (IOException /*| JSONException*/ | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int k;
                        k = Integer.parseInt(input.weather_data.pty); //pty가 현재 날씨를 의미하는 변수임.
                        nowC.setText(input.weather_data.t1h); // 현 기온 텍스트 설정
                        nowH.setText(input.weather_data.reh); // 현 습도 텍스트 설정
                        if(k<1)
                        {
                            nowSky.setText("맑음");
                        }
                        else if(k==3||k==7)
                        {
                            nowSky.setText("눈");
                        }
                        else
                        {
                            nowSky.setText("비");
                        }
                    }
                });
            }
        })).start();


    }
    void getWeather(data in) throws IOException, ParseException {
        //초단기 실황 조회
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환값은 JSON
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(t.getBase_date(), "UTF-8")); //API 마다 필요한 날짜 형식을 만들어주는 클래스 만들어서 사용
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

        // 현 시간을 가져와서 가장 가까운 시간에 맞춰야함: 기준 시간은 30분마다 생성이며 기준 시간 + 10분이후부터 가능
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(t.getBase_time(), "UTF-8")); //API 마다 필요한 날짜 형식을 만들어주는 클래스 만들어서 사용
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("62", "UTF-8")); //  학교 좌표
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("126", "UTF-8")); // 학교 좌표

        /*
        POP = 강수확률 % 단위
        PTY = 강수형태 0 = 없음 1 = 비 2 = 눈과 비 3 = 눈 4 = 소나기 5 = 빗방울 6 = 빗방울과 눈 날림 7 = 눈날림
        PCP 1시간 강수량 mm 단위
        REH 습도 % 단위
        SNO 1시간 적설량 cm 단위
        SKY 하늘상태 코드값 0 ~ 5 맑음 6 ~ 8 구름많음 9~10 흐림
        T1H 1시간 기온
        TMN 일 최저 기온
        TMX 일 최고 기온
         */

        // 위에서 만든 URL 주소 String에서 URL 객체로 생성
        URL url = new URL(urlBuilder.toString());

        //URL에서 받아온 데이터 읽기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        //가져온 JSON 형식 데이터 연결
        String result = bf.readLine();

        //파싱
        JSONParser jsonParser = new JSONParser(); //Parser로 파싱 수행
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result); //모든 데이터를 하나의 string 형식으로 가진 result를 항목 별로 파싱 수행
        JSONObject response = (JSONObject)jsonObject.get("response"); // response의 항목들 파싱
        JSONObject body = (JSONObject)response.get("body"); // response의 항목들 중 body 항목 파싱
        JSONObject items = (JSONObject) body.get("items"); // body 항목들 중 items 항목 파싱
        JSONArray itemArr = (JSONArray)items.get("item"); // items의 데이터들 저장
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.weather_data.setValue(item.get("category").toString(), item.get("obsrValue").toString()); //data 클래스에 category에 해당하는 obsrValue(=예보 값) 을 저장하는 함수 구현하여 사용
        }
        in.weather_data.set_dif_temp(); // 일교차에 대한 데이터 저장
    }

}
