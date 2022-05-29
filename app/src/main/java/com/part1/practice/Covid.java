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
                    get_cold(input);
                    get_Allergy_Chunsik(input);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 각 API 데이터에서 받아온 위험도 int형 저장
                        int x=0;
                        int y=0;
                        x = Integer.parseInt(input.cold_data.today_val);
                        y = Integer.parseInt(input.chunsik_data.today_val);
                        // 각 위험도에 따른 이미지 설정
                        if(x==0){
                            cold.setImageResource(R.drawable.emoji0);
                        }
                        else if(x==1)
                        {
                            cold.setImageResource(R.drawable.emoji1);
                        }
                        else if(x==2)
                        {
                            cold.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            cold.setImageResource(R.drawable.emoji3);
                        }
                        if(y==0){
                            chunsik.setImageResource(R.drawable.emoji0);
                        }
                        else if(y==1)
                        {
                            chunsik.setImageResource(R.drawable.emoji1);
                        }
                        else if(y==2)
                        {
                            chunsik.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            chunsik.setImageResource(R.drawable.emoji3);
                        }
                    }
                });
            }
        })).start();
    }
    void get_cold(data in) throws IOException, ParseException {
        if (!in.cold_data.checkValid(t))
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getColdIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // JSON 형식으로 저장
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8"));  // 시간 데이터 알맞는 형식으로 주는 함수 getTime
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));

        // URL 객체 생성
        URL url = new URL(urlBuilder.toString());

        // URL 객체를 통해 데이터 저장
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        //파싱이 아직 안됨
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");

        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.cold_data.setDate(item.get("date").toString());
            if(item.get("today").toString().isEmpty()) // 오늘 위험도 저장
                in.cold_data.setToday_val(item.get("today").toString());
            in.cold_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 저장
        }

    }
    void get_Allergy_Chunsik(data in) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getAsthmaIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // JSON 타입으로 저장
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // getTime으로 APi가 원하는 형식 날짜 데이터 전달
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        //URL 객체 생성
        URL url = new URL(urlBuilder.toString());
        // URL에 따른 API 데이터 받아옴
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();
        //API 데이터 하나의 문자열로 받아서 PARSING 시행
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.chunsik_data.setDate(item.get("date").toString());
            if(item.get("today").toString().isEmpty())  // 오늘 위험도 데이터 저장
                in.chunsik_data.setToday_val(item.get("today").toString());
            in.chunsik_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 데이터 저장
        }
    }
}