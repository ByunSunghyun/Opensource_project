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
                    get_Allergy_charmTree(input);
                    get_Allergy_SoTree(input);
                    get_Allergy_Jopcho(input);
                } catch (IOException /*| JSONException*/ | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //x y z는 각각 소나무 잡초류 참나무 알러지에 대한 데이터를 int형으로 저장하는 것
                        int x=0;
                        int y=0;
                        int z=0;
                        x = Integer.parseInt(input.soTree_data.today_val);
                        y = Integer.parseInt(input.jopcho_data.today_val);
                        z = Integer.parseInt(input.charmTree_data.today_val);

                        // 각 알러지 value에 따른 UI 이미지 변경
                        if(x==0){
                            soTree.setImageResource(R.drawable.emoji0);
                        }
                        else if(x==1)
                        {
                            soTree.setImageResource(R.drawable.emoji1);
                        }
                        else if(x==2)
                        {
                            soTree.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            soTree.setImageResource(R.drawable.emoji3);
                        }
                        if(y==0){
                            jabcho.setImageResource(R.drawable.emoji0);
                        }
                        else if(y==1)
                        {
                            jabcho.setImageResource(R.drawable.emoji1);
                        }
                        else if(y==2)
                        {
                            jabcho.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            jabcho.setImageResource(R.drawable.emoji3);
                        }
                        if(z==0){
                            chamTree.setImageResource(R.drawable.emoji0);
                        }
                        else if(z==1)
                        {
                            chamTree.setImageResource(R.drawable.emoji1);
                        }
                        else if(z==2)
                        {
                            chamTree.setImageResource(R.drawable.emoji2);
                        }
                        else
                        {
                            chamTree.setImageResource(R.drawable.emoji3);
                        }
                    }
                });
            }
        })).start();
    }
    void get_Allergy_charmTree(data in) throws IOException, ParseException {
        if (!in.charmTree_data.checkValid(t))
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getOakPollenRiskIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); //JSON 형식으로 데이터 저장
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8"));
        // URL 객체 생성
        URL url = new URL(urlBuilder.toString());

        //URL 객체를 통해 API 데이터 받아오기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        // 하나의 문자열 데이터로 받아오기 떄문에 이를 Parisng 해줘야함
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");


        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.charmTree_data.setDate(item.get("date").toString());
            if(!item.get("today").toString().isEmpty()) // 오늘 위험지수 저장
                in.charmTree_data.setToday_val(item.get("today").toString());
            in.charmTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험 지수 저장
        }
    }

    void get_Allergy_SoTree(data in) throws IOException, ParseException {
        if (!in.soTree_data.checkValid(t))
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getPinePollenRiskIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));  //JSON 형태로 데이터 저장
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // 현재 시간 데이터를 알맞는 형식으로 URL에 제공
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

        // URL 객체 생성
        URL url = new URL(urlBuilder.toString());

        //URL에 따른 데이터 불러오기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        //가져온 하나의 문자열로 받은 데이터를 Parsing함
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);

            in.soTree_data.setDate(item.get("date").toString());
            if(item.get("today").toString().isEmpty()) // 오늘 위험도 데이터 저장
                in.soTree_data.setToday_val(item.get("today").toString());
            in.soTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 데이터 저장

        }
    }

    void get_Allergy_Jopcho(data in) throws IOException, ParseException {
        if (!in.jopcho_data.checkValid(t))
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getWeedsPollenRiskndxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // JSON 타입 저장
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // 오늘 날짜에 대한 데이터 알맞은 형식으로 바등ㅁ
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        //URL 객체 생성
        URL url = new URL(urlBuilder.toString());
        //URL에 따른 API 데이터 받아오기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        //하나의 문자열 데이터로 받기에 Parsing 필요
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");

        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.jopcho_data.setDate(item.get("date").toString());
            if(item.get("today").toString().isEmpty()) //오늘 위험도 저장
                in.jopcho_data.setToday_val(item.get("today").toString());
            in.jopcho_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 저장
        }
    }

}