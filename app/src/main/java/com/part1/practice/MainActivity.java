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
                    get_Allergy_charmTree(input);
                    get_Allergy_SoTree(input);
                    get_Allergy_Jopcho(input);
                    getWeather(input);
                    getAirKor(input);
                    get_cold(input);
                    get_Allergy_Chunsik(input);

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Create 시에 처음으로 받아오는 데이터에 따른 이미지 변경
                        System.out.println("전체 위험지수 : " + sum);
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


    //참나무 알러지에 대한 API 가져오기
    void get_Allergy_charmTree(data in) throws IOException, ParseException {
        if (!in.charmTree_data.checkValid(t)) // 자료를 제공하는 기간이 아닌 경우
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }

        // 가져올 API 데이터 주소에 URL 연결
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getOakPollenRiskIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환값 => JSON 형식
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // t.getTime은 직접 구현한 각 API 항목에 맞는 날짜 제공을하는 NowTime의 인스턴스
       // 위에서 만든 URL 주소 String에서 URL 객체로 생성
        URL url = new URL(urlBuilder.toString());

        //URL에서 받아온 데이터 읽기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        //가져온 JSON 형식 데이터 연결
        String result = bf.readLine();

        //파싱 작업
        JSONParser jsonParser = new JSONParser(); //Parser로 파싱 수행
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result); //모든 데이터를 하나의 string 형식으로 가진 result를 항목 별로 파싱 수행
        JSONObject response = (JSONObject)jsonObject.get("response"); // response에 해당하는 항목들 파싱
        JSONObject body = (JSONObject)response.get("body"); // response에서 body에 해당하는 항목들 파싱
        JSONObject items = (JSONObject) body.get("items"); // body에서 items 해당하는 항목들 파싱
        JSONArray itemArr = (JSONArray)items.get("item"); // items의 항목들을 배열로 저장
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i); // itemArr를 순회하여 이 데이터를 임시로 가짐
            in.charmTree_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                in.charmTree_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            in.charmTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        sum += Integer.parseInt(in.charmTree_data.today_val); // 참나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
        if((Integer.parseInt(in.charmTree_data.today_val)>1)) // 참나무 알러지가 위험 수준인 경우 팁에 추가
            tipStr[tip_num++] = String.valueOf(R.string.chamtree_bad);
    }

    //소나무 알러지에 대한 API 가져오기
    void get_Allergy_SoTree(data in) throws IOException, ParseException {
        if (!in.soTree_data.checkValid(t))// 자료를 제공하는 기간이 아닌 경우
        {
            // 데이터를 제공하는 기간이 아닌 경우
            // 원래 데이터를 표시하는 오브젝트에 데이터 제공 기간이 아님을 표시하는 명령을 넣으면 됨
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getPinePollenRiskIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환값 => JSON 형식
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); //t.getTime은 직접 구현한 각 API 항목에 맞는 날짜 제공을하는 NowTime의 인스턴스준
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

        // 위에서 만든 URL 주소 String에서 URL 객체로 생성
        URL url = new URL(urlBuilder.toString());

        //URL에서 받아온 데이터 읽기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        //가져온 JSON 형식 데이터 연결
        String result = bf.readLine();

        //파싱 작업
        JSONParser jsonParser = new JSONParser(); //Parser로 파싱 수행
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result); //모든 데이터를 하나의 string 형식으로 가진 result를 항목 별로 파싱 수행
        JSONObject response = (JSONObject)jsonObject.get("response"); // response에 해당하는 항목들 파싱
        JSONObject body = (JSONObject)response.get("body"); // response에서 body에 해당하는 항목들 파싱
        JSONObject items = (JSONObject) body.get("items"); // body에서 items 해당하는 항목들 파싱
        JSONArray itemArr = (JSONArray)items.get("item"); // items의 항목들을 배열로 저장
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i); // itemArr를 순회하여 이 데이터를 임시로 가짐
            in.soTree_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                in.soTree_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            in.soTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        sum += Integer.parseInt(in.soTree_data.today_val); // 소나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
        if((Integer.parseInt(in.soTree_data.today_val)>1)) // 소나무 알러지가 위험 수준인 경우 팁에 추가
            tipStr[tip_num++] = String.valueOf(R.string.sotree_bad);
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
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환값 => JSON 형식
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); //t.getTime은 직접 구현한 각 API 항목에 맞는 날짜 제공을하는 NowTime의 인스턴스준
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("30", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));

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
        JSONObject response = (JSONObject)jsonObject.get("response"); // response에 해당하는 항목들 파싱
        JSONObject body = (JSONObject)response.get("body");// response에서 body에 해당하는 항목들 파싱
        JSONObject items = (JSONObject) body.get("items"); // body에서 items 해당하는 항목들 파싱
        JSONArray itemArr = (JSONArray)items.get("item"); // items의 항목들을 배열로 저장
        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i); // itemArr를 순회하여 이 데이터를 임시로 가짐
            in.jopcho_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                in.jopcho_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            in.jopcho_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        sum += Integer.parseInt(in.jopcho_data.today_val); // 잡초류 알러지 항목의 위험도 값을 총 위험도 값에 포함
        if((Integer.parseInt(in.jopcho_data.today_val)>1)) // 잡초류 알러지가 위험 수준인 경우 팁에 추가
            tipStr[tip_num++] = String.valueOf(R.string.jabcho_bad);
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
        if((Integer.parseInt(in.weather_data.pty))==0) {tipStr[tip_num++] = String.valueOf("오늘은 낡씨가 맑아요");} //날이 맑으면 팁에 추가
        else if((Integer.parseInt(in.weather_data.pty))==4||(Integer.parseInt(in.weather_data.pty))==7) // 눈이 오면 팁에 추가
            tipStr[tip_num++] = String.valueOf(R.string.is_snowing);
        else // 비가 오면 팁에 추가
            tipStr[tip_num++] = String.valueOf(R.string.is_raining);
        sky = Integer.parseInt(input.weather_data.pty); // 날씨에 대한 데이터 중 Sky 항목값 메인 스레드에서 사용하기 위해 저장
    }

    // Air Korea에서 제공하는 미세먼지 API
    void getAirKor(data in) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); //반환값 = JSON
        urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); // 서울에 있는 관측소 기준
        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8"));

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
        JSONObject totalCount = (JSONObject)jsonObject.get("totalCount"); // 디버깅을 위해 저장했던, 총 항목 수
        JSONArray item = (JSONArray)body.get("items"); // items 항목 저장

        for (int i = 0; i < item.size(); i++)
        {
            JSONObject items = (JSONObject) item.get(i); // item 항목 순회하며 해당 위치값
            String str = items.get("stationName").toString(); //서울에 있는 관측소 중 현재 항목의 관측소 명 가져옴
            String sung = "성동구"; // 학교 근처 관측소는 성동구가 제일 가까움
            if(str.equals(sung)){ // 성동구 관측소의 경우 데이터들 저장
                int p1, p2; // pm10, pm2.5 값 저장하기 위한 int형 데이터

                in.airKorDust_data.setPm10val(items.get("pm10Value").toString()); //pm10 관측 값 저장
                in.airKorDust_data.setStationName(items.get("stationName").toString()); // 관측소 이름을 디버깅용으로 저장했음
                in.airKorDust_data.setPm25val(items.get("pm25Value").toString()); // pm2.5 관측 값 저장
                p1 = Integer.parseInt(in.airKorDust_data.pm10val); // 저장한 값을 int형 변환
                p2 = Integer.parseInt(in.airKorDust_data.pm25val); // 저장한 값을 int형 변환
                // 공식으로 발표된 지표에 따라 위험도 구분
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
            }
        }
        // 미세먼지 위험 지수 값을 전체 위험지수 값에 더함
        if (in.airKorDust_data.pm10Grade1h != null)
            sum += Integer.parseInt(in.airKorDust_data.pm10Grade1h);
        if (in.airKorDust_data.pm25Grade1h != null)
            sum += Integer.parseInt(in.airKorDust_data.pm25Grade1h);

        // 미세먼지 위험 지수 값에 따라 팁을 추가
        if(in.airKorDust_data.pm10Grade1h != null ) {
            if ((Integer.parseInt(in.airKorDust_data.pm10Grade1h)>1))
                tipStr[tip_num++] = String.valueOf(R.string.md_bad);
        }
        else
        {
            //공식으로 발표된 지표에 따라 위험도 구분
            if(Integer.parseInt(in.airKorDust_data.pm10val)<31)
            {
                in.airKorDust_data.setPm10Grade1h("0");
            }
            else if(Integer.parseInt(in.airKorDust_data.pm10val)<81)
            {
                in.airKorDust_data.setPm10Grade1h("1");
            }
            else if(Integer.parseInt(in.airKorDust_data.pm10val)<151)
            {
                in.airKorDust_data.setPm10Grade1h("2");
            }
            else
            {
                in.airKorDust_data.setPm10Grade1h("3");
            }
            if (Integer.parseInt(in.airKorDust_data.pm25val) < 15)
            {
                in.airKorDust_data.setPm25Grade1h("0");
            }
            else if(Integer.parseInt(in.airKorDust_data.pm25val)<51)
            {
                in.airKorDust_data.setPm25Grade1h("1");
            }
            else if(Integer.parseInt(in.airKorDust_data.pm25val)<101)
            {
                in.airKorDust_data.setPm25Grade1h("2");
            }
            else
            {
                in.airKorDust_data.setPm25Grade1h("3");
            }
            // 위험도에 따른 팁 추가
            if(in.airKorDust_data.pm10Grade1h != null && (Integer.parseInt(in.airKorDust_data.pm10Grade1h)>1))
                tipStr[tip_num++] = String.valueOf(R.string.md_bad);
        }
        // 미세먼지 위험도 저장
        dust = Integer.parseInt(input.airKorDust_data.pm10Grade1h);
    }

    // 천식 가능 지수 API
    void get_Allergy_Chunsik(data in) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV2/getAsthmaIdxV2"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=T%2FL6AKT1fsWyadGE1j3QYFXgSmOWWV375pu6qQxuQ612F22wflZp0Ey%2BdjuPCZ8XeoShMs%2FaOPn3QJfpkbTlTw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환 값 = JSON
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // API 마다 필요한 날짜 데이터 형식을 구현하여 사용
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));

        // 위에서 만든 URL 주소 String에서 URL 객체로 생성
        URL url = new URL(urlBuilder.toString());

        // URL 에서 데이터 읽고 저장
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        //파싱 위와 동일
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");


        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);
            in.chunsik_data.setDate(item.get("date").toString()); // 오늘 날짜 데이터 더장
            if(!item.get("today").toString().isEmpty()) { // 오늘 관측 값 저장
                in.chunsik_data.setToday_val(item.get("today").toString());
            }
            in.chunsik_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 관측값 저장
        }
        sum += Integer.parseInt(in.chunsik_data.today_val); // 오늘 천식 위험지수를 전체 위험 지수에 더함

        if((Integer.parseInt(in.chunsik_data.today_val)>1)) // 천식 위험지수가 나쁠 경우 팁에 추가
        {
            tipStr[tip_num++] = String.valueOf("천식지수가 나쁘니 주의하세요");
        }
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
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 반환값 = JSON
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); //서울 코드: 1100000000
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(t.getTime(), "UTF-8")); // API 마다 필요로 하는 날짜 형식 구현하여 제공
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));

        // URL객체 생성
        URL url = new URL(urlBuilder.toString());

        // URL 객체를 통해 데이터 읽어오기
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = bf.readLine();

        //파싱 위와 동일
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray itemArr = (JSONArray)items.get("item");

        for (int i = 0; i < itemArr.size(); i++)
        {
            JSONObject item = (JSONObject) itemArr.get(i);

            in.cold_data.setDate(item.get("date").toString()); // 디버깅 용으로 관측 날짜를 저장

            if(!item.get("today").toString().isEmpty())
                in.cold_data.setToday_val(item.get("today").toString()); // 오늘 감기 가능 지수 데이터 저장

            in.cold_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 감기 가능 지수 데이터 저장 - 디버깅용
        }
        this.sum += Integer.parseInt(in.cold_data.today_val); // 오늘의 위험도 총합에 감기 가능지수 위험도를 더함

        if((Integer.parseInt(in.cold_data.today_val)>1)) //감기 가능 지수가 위험인 경우 팁에 이에 대한 대사 추가
            tipStr[tip_num++] = String.valueOf(R.string.cold_bad);
    }
}