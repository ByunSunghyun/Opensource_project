package com.example.opensource1;

import androidx.appcompat.app.ActionBar;
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

public class Microdust extends AppCompatActivity {
    TextView pm10, pm25;
    data input = new data();
    String p1 = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microdust);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();// 액션바 삭제

        pm10 = findViewById(R.id.textView);
        pm25 = findViewById(R.id.textView2);
        p1 = "Can't access internet";
        //할당

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    getAirKor(input);
                } catch (IOException /*| JSONException*/ | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pm10.setText(input.airKorDust_data.pm10val);
                        System.out.println(input.airKorDust_data.pm10val);
                        pm25.setText(input.airKorDust_data.pm25val);
                    }
                });
            }
        })).start();

        pm10.setText(p1);
        pm25.setText(p1);

    }

    void getAirKor(data in) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*xml 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*통보시간 검색(조회 날짜 입력이 없을 경우 한달동안 예보통보 발령 날짜의 리스트 정보를 확인)*/
        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8")); /*통보코드검색(PM10, PM25, O3)*/
        URL url = new URL(urlBuilder.toString());


        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();


        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject totalCount = (JSONObject)jsonObject.get("totalCount");
        JSONArray item = (JSONArray)body.get("items");

        for (int i = 0; i < item.size(); i++) {
            JSONObject items = (JSONObject) item.get(i);
            String str = items.get("stationName").toString();
            String sung = "성동구";
            if (str.equals(sung)) {
                int p1, p2;
                in.airKorDust_data.setPm10val(items.get("pm10Value").toString());
                in.airKorDust_data.setStationName(items.get("stationName").toString());
                in.airKorDust_data.setPm25val(items.get("pm25Value").toString());
                p1 = Integer.parseInt(in.airKorDust_data.pm10val);
                p2 = Integer.parseInt(in.airKorDust_data.pm25val);
                if(p1<31)
                {
                    in.airKorDust_data.setPm10Grade1h("0");
                }
                else if(p1<81)
                {
                    in.airKorDust_data.setPm10Grade1h("1");
                }
                else if(p1<151)
                {
                    in.airKorDust_data.setPm10Grade1h("2");
                }
                else
                {
                    in.airKorDust_data.setPm10Grade1h("3");
                }
                if (p2 < 15)
                {
                    in.airKorDust_data.setPm25Grade1h("0");
                }
                else if(p1<51)
                {
                    in.airKorDust_data.setPm25Grade1h("1");
                }
                else if(p1<101)
                {
                    in.airKorDust_data.setPm25Grade1h("2");
                }
                else
                {
                    in.airKorDust_data.setPm25Grade1h("3");
                }


            }//성동구 미세먼지 할당
        }
    }
}