package com.example.opensource1;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.simple.JSONObject;

import android.os.Build;
import org.json.simple.JSONArray;
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

import org.w3c.dom.Text;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    // 데이터를 저장하는 객체와 위험도 총합을 저장하는 변수, 시간을 API 맞는 형식의 String으로 제공하는 객체
    static data input = new data();
    static int sum;
    static NowTime t = new NowTime();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji0);

        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    //각 API 데이터 받아오기
                    get_Allergy_charmTree(input);
                    get_Allergy_SoTree(input);
                    get_Allergy_Jopcho(input);
                    getWeather(input);
                    getAirKor(input);
                    get_cold(input);
                    get_Allergy_Chunsik(input);
                    // 각 API 데이터에 따른 위험도 총합 구하기
                    sum = get_sumAll(input);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

            }
        })).start();
        // 위험도에 따른 이미지 설정
        if(sum<1)
        {
            views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji0);
        }
        else if(sum<2)
        {
            views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji1);
        }
        else if(sum<3)
        {
            views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji2);
        }
        else
        {
            views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji3);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    static int get_sumAll(data in){
        // 오늘 데이터에 따른 위험도 총합
        int i=0;
        i = i+Integer.parseInt(in.cold_data.today_val);
        i = i+Integer.parseInt(in.charmTree_data.today_val);
        i = i+Integer.parseInt(in.jopcho_data.today_val);
        i = i+Integer.parseInt(in.soTree_data.today_val);
        i = i+Integer.parseInt(in.chunsik_data.today_val);
        i = i+Integer.parseInt(in.airKorDust_data.pm10Grade1h);
        i = i+Integer.parseInt(in.airKorDust_data.pm25Grade1h);
        i = i/4;
        return i;
    }
    static void get_Allergy_charmTree(data in) throws IOException, ParseException {
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
        //sum += Integer.parseInt(in.charmTree_data.today_val); // 참나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
    }

    //소나무 알러지에 대한 API 가져오기
    static void get_Allergy_SoTree(data in) throws IOException, ParseException {
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
        //sum += Integer.parseInt(in.soTree_data.today_val); // 소나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
    }

    static void get_Allergy_Jopcho(data in) throws IOException, ParseException {
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
    }

    static void getWeather(data in) throws IOException, ParseException {
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

    static void getAirKor(data in) throws IOException, ParseException {
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
        }
    }
    static void get_Allergy_Chunsik(data in) throws IOException, ParseException {
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

    static void get_cold(data in) throws IOException, ParseException {
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
}