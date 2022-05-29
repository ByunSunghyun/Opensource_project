package com.example.opensource1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class data {
    air_kor_dust airKorDust_data; // 미세먼지 데이터 클래스
    chunsik chunsik_data; // 천식 데이터 클래스
    soTree soTree_data; // 소나무 알러지 데이터 클래스
    charmTree charmTree_data; // 참나무 알러지 데이터 클래스
    jopcho jopcho_data; // 잡초류 알러지 데이터 클래스
    weather weather_data; // 날씨 데이터 클래스
    cold cold_data; // 감기 가능지수 데이터 클래스
    NowTime t;

    data(){ // 생성자에서 각 데이터 클래스 인스턴스 생성
        t = new NowTime();
        airKorDust_data = new air_kor_dust();
        chunsik_data = new chunsik();
        soTree_data = new soTree();
        charmTree_data = new charmTree();
        jopcho_data = new jopcho();
        weather_data = new weather();
        cold_data = new cold();
    }

    public class air_kor_dust {
        //Air Kor의 데이터는 가장 최신의 데이터만 전해주기에 time에 대한 data 필요 없음

        // 측정소 이름, 각 미세먼지 지수 및 등급을 저장
        String sidoName;
        String stationName;
        String pm25val;
        String pm10val;
        String pm25Grade1h;
        String pm10Grade1h;

        // 클래스 멤버의 setter
        public void setPm25Grade1h(String pm25Grade1h) {
            this.pm25Grade1h = pm25Grade1h;
        }
        public void setPm10Grade1h(String pm10Grade1h) {
            this.pm10Grade1h = pm10Grade1h;
        }
        public void setStationName(String stationName) {
            this.stationName = stationName;
        }
        public void setPm25val(String pm25val) {
            this.pm25val = pm25val;
        }
        public void setPm10val(String pm10val) {
            this.pm10val = pm10val;
        }
    }


    public class chunsik {
        // 천식 가능 지수에 대한 데이터를 담을 클래스

        //데이터 측정일, 오늘 내일 값을 저장
        String date;
        String today_val = new String("0");
        String tomorrow_val = new String("0");

        // 멤버에 대한 setter
        public void setDate(String date) {
            this.date = date;
        }
        public void setToday_val(String today_val) {
            this.today_val = today_val;
        }
        public void setTomorrow_val(String tomorrow_val) {
            this.tomorrow_val = tomorrow_val;
        }
    }

    //각 알러지 데이터는 위험도가 0 1 2 3 으로 0이 낮음 1이 보통 2가 높음 3이 매우높음임
    public class soTree {
        // 소나무 알러지에 대한 오늘과 내일에 대한 위험도 저장
        String date;
        String today_val = new String("0");
        String tomorrow_val = new String("0");

        // 소나무 알러지가 발생하지 않는 주기에는 데이터를 제공하지 않기에 유효한 날짜에만 데이터를 받도록함
        public boolean checkValid(NowTime t){
            int month_int = Integer.parseInt(t.getMonth());
            if (month_int < 4 || month_int > 6) // 제공 시기는 4~6월 사이
                return false;
            return true;
        }
        // 멤버들에 대한 setter 설정
        public void setDate(String date) {
            this.date = date;
        }
        public void setToday_val(String today_val) {
            this.today_val = today_val;
        }
        public void setTomorrow_val(String tomorrow_val) {
            this.tomorrow_val = tomorrow_val;
        }
    }

    public class charmTree {
        // 참나무 알러지에 대한 데이터 저장
        String date;
        String today_val = new String("0");
        String tomorrow_val = new String("0");
        
        //유요한 날짜가 아닌 경우 데이터를 제공하지 않기에 이를 체크하는 함수
        public boolean checkValid(NowTime t){
            int month_int = Integer.parseInt(t.getMonth());
            if (month_int < 4 || month_int > 6) // 4~6월 사이에만 데이터를 제공함
                return false;
            return true;
        }
        // 멤버에 대한 setter 설정
        public void setDate(String date) {
            this.date = date;
        }
        public void setToday_val(String today_val) {
            this.today_val = today_val;
        }
        public void setTomorrow_val(String tomorrow_val) {
            this.tomorrow_val = tomorrow_val;
        }
    }

    
    public class jopcho {
        // 잡초류 알러지에 대한 데이터 저장
        String date;
        String today_val = new String("0");
        String tomorrow_val = new String("0");

        public boolean checkValid(NowTime t){
            int month_int = Integer.parseInt(t.getMonth());
            if (month_int < 8 || month_int > 10) //잡초류 알러지 데이터는 8 ~ 10 월 사이에 제공
                return false;
            return true;
        }

        // 멤버에 대한 setter 정의
        public void setDate(String date) {
            this.date = date;
        }
        public void setToday_val(String today_val) {
            this.today_val = today_val;
        }
        public void setTomorrow_val(String tomorrow_val) {
            this.tomorrow_val = tomorrow_val;
        }
    }


    public class cold {
        // 감기 위험지수에 대한 데이터 저장
        String date;
        String today_val = new String("0");
        String tomorrow_val = new String("0");

        // 감기 가능 지수를 제공하는 시기 체크
        public boolean checkValid(NowTime t){
            int month_int = Integer.parseInt(t.getMonth());
            if (month_int < 9 && month_int > 4) // 감기가능 지수 제공 시기는 9월 이상, 4월 이하임
                return false;
            return true;
        }

        public void setDate(String date) {
            this.date = date;
        }
        public void setToday_val(String today_val) {
            this.today_val = today_val;
        }
        public void setTomorrow_val(String tomorrow_val) {
            this.tomorrow_val = tomorrow_val;
        }
    }

    //기상청 데이터
    //입력 받는 category에 따라 데이터를 입력 받아옴.
    public class weather {
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
        String base_date;
        String pop = "-1";
        String pty = "-1";
        String pcp = "-1";
        String reh = "-1";
        String sno = "-1";
        String sky = "-1";
        String t1h = "-1";
        String tmn = "-1";
        String tmx = "-1";
        String dif_temp = "-1";

        // 일교차를 저장
        public void set_dif_temp(){
            int max, min;
            max = Integer.parseInt(tmx);
            min = Integer.parseInt(tmn);
            this.dif_temp = Integer.toString(max - min);
        }

        // catergory에 따른 value 저장
        public void setValue(String type, String val) {
            switch (type)
            {
                case "POP":
                    pop = val;
                    break;
                case "PTY":
                    pty = val;
                    break;
                case "PCP":
                    pcp = val;
                    break;
                case "REH":
                    reh = val;
                    break;
                case "SNO":
                    sno = val;
                    break;
                case "SKY":
                    sky = val;
                    break;
                case "T1H":
                    t1h = val;
                    break;
                default: break;
            }
        }
    }
    //참나무 알러지에 대한 API 가져오기
    void get_Allergy_charmTree(MainActivity act) throws IOException, ParseException {
        if (!charmTree_data.checkValid(t)) // 자료를 제공하는 기간이 아닌 경우
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
            charmTree_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                charmTree_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            charmTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        if (act != null)
        {
            act.sum += Integer.parseInt(act.input.charmTree_data.today_val); // 참나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
            if((Integer.parseInt(act.input.charmTree_data.today_val)>1)) // 참나무 알러지가 위험 수준인 경우 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.chamtree_bad);
        }
    }

    //소나무 알러지에 대한 API 가져오기
    void get_Allergy_SoTree(MainActivity act) throws IOException, ParseException {
        if (!soTree_data.checkValid(t))// 자료를 제공하는 기간이 아닌 경우
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
            soTree_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                soTree_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            soTree_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        if (act != null)
        {
            act.sum += Integer.parseInt(act.input.soTree_data.today_val); // 소나무 알러지 항목의 위험도 값을 총 위험도 값에 포함
            if((Integer.parseInt(act.input.soTree_data.today_val)>1)) // 소나무 알러지가 위험 수준인 경우 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.sotree_bad);
        }
    }


    void get_Allergy_Jopcho(MainActivity act) throws IOException, ParseException {
        if (!jopcho_data.checkValid(t))
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
            jopcho_data.setDate(item.get("date").toString()); // item에서 date 항목에 대해 저장
            if(!item.get("today").toString().isEmpty()) // 오늘에 해당하는 값이 비어있지 않다면
                jopcho_data.setToday_val(item.get("today").toString()); // 오늘 위험도 값 저장.
            jopcho_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 위험도 값 저장.
        }
        if (act != null)
        {
            act.sum += Integer.parseInt(act.input.jopcho_data.today_val); // 잡초류 알러지 항목의 위험도 값을 총 위험도 값에 포함
            if((Integer.parseInt(act.input.jopcho_data.today_val)>1)) // 잡초류 알러지가 위험 수준인 경우 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.jabcho_bad);
        }
    }


    void getWeather(MainActivity act) throws IOException, ParseException {
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
            weather_data.setValue(item.get("category").toString(), item.get("obsrValue").toString()); //data 클래스에 category에 해당하는 obsrValue(=예보 값) 을 저장하는 함수 구현하여 사용
        }
        weather_data.set_dif_temp(); // 일교차에 대한 데이터 저장

        if (act != null) {
            if ((Integer.parseInt(act.input.weather_data.pty)) == 0) {
                act.tipStr[act.tip_num++] = String.valueOf("오늘은 낡씨가 맑아요");
            } //날이 맑으면 팁에 추가
            else if ((Integer.parseInt(act.input.weather_data.pty)) == 4 || (Integer.parseInt(act.input.weather_data.pty)) == 7) // 눈이 오면 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.is_snowing);
            else // 비가 오면 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.is_raining);
            act.sky = Integer.parseInt(act.input.weather_data.pty); // 날씨에 대한 데이터 중 Sky 항목값 메인 스레드에서 사용하기 위해 저장
        }

    }

    // Air Korea에서 제공하는 미세먼지 API
    void getAirKor(MainActivity act) throws IOException, ParseException {
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

                airKorDust_data.setPm10val(items.get("pm10Value").toString()); //pm10 관측 값 저장
                airKorDust_data.setStationName(items.get("stationName").toString()); // 관측소 이름을 디버깅용으로 저장했음
                airKorDust_data.setPm25val(items.get("pm25Value").toString()); // pm2.5 관측 값 저장
                p1 = Integer.parseInt(airKorDust_data.pm10val); // 저장한 값을 int형 변환
                p2 = Integer.parseInt(airKorDust_data.pm25val); // 저장한 값을 int형 변환
                // 공식으로 발표된 지표에 따라 위험도 구분
                if(p1<31)   airKorDust_data.setPm10Grade1h("0");
                else if(p1<81)  airKorDust_data.setPm10Grade1h("1");
                else if(p1<151) airKorDust_data.setPm10Grade1h("2");
                else    airKorDust_data.setPm10Grade1h("3");
                if (p2 < 15)    airKorDust_data.setPm25Grade1h("0");
                else if(p1<51)  airKorDust_data.setPm25Grade1h("1");
                else if(p1<101) airKorDust_data.setPm25Grade1h("2");
                else airKorDust_data.setPm25Grade1h("3");
            }
        }
        if (act != null)
        {
            // 미세먼지 위험 지수 값을 전체 위험지수 값에 더함
            if (act.input.airKorDust_data.pm10Grade1h != null)
                act.sum += Integer.parseInt(act.input.airKorDust_data.pm10Grade1h);
            if (act.input.airKorDust_data.pm25Grade1h != null)
                act.sum += Integer.parseInt(act.input.airKorDust_data.pm25Grade1h);
            act.sum += Integer.parseInt(act.input.chunsik_data.today_val); // 오늘 천식 위험지수를 전체 위험 지수에 더함
            if((Integer.parseInt(act.input.chunsik_data.today_val)>1)) // 천식 위험지수가 나쁠 경우 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf("천식지수가 나쁘니 주의하세요");

            // 미세먼지 위험 지수 값에 따라 팁을 추가
            if(act.input.airKorDust_data.pm10Grade1h != null ) {
                if ((Integer.parseInt(act.input.airKorDust_data.pm10Grade1h)>1))
                    act.tipStr[act.tip_num++] = String.valueOf(R.string.md_bad);
            }
            else
            {
                //공식으로 발표된 지표에 따라 위험도 구분
                if(Integer.parseInt(act.input.airKorDust_data.pm10val)<31)
                    act.input.airKorDust_data.setPm10Grade1h("0");
                else if(Integer.parseInt(act.input.airKorDust_data.pm10val)<81)
                    act.input.airKorDust_data.setPm10Grade1h("1");
                else if(Integer.parseInt(act.input.airKorDust_data.pm10val)<151)
                    act.input.airKorDust_data.setPm10Grade1h("2");
                else
                    act.input.airKorDust_data.setPm10Grade1h("3");
                if (Integer.parseInt(act.input.airKorDust_data.pm25val) < 15)
                    act.input.airKorDust_data.setPm25Grade1h("0");
                else if(Integer.parseInt(act.input.airKorDust_data.pm25val)<51)
                    act.input.airKorDust_data.setPm25Grade1h("1");
                else if(Integer.parseInt(act.input.airKorDust_data.pm25val)<101)
                    act.input.airKorDust_data.setPm25Grade1h("2");
                else
                    act.input.airKorDust_data.setPm25Grade1h("3");
                // 위험도에 따른 팁 추가
                if(act.input.airKorDust_data.pm10Grade1h != null && (Integer.parseInt(act.input.airKorDust_data.pm10Grade1h)>1))
                    act.tipStr[act.tip_num++] = String.valueOf(R.string.md_bad);
            }
            // 미세먼지 위험도 저장
            act.dust = Integer.parseInt(act.input.airKorDust_data.pm10Grade1h);
        }
    }

    // 천식 가능 지수 API
    void get_Allergy_Chunsik(MainActivity act) throws IOException, ParseException {
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
            chunsik_data.setDate(item.get("date").toString()); // 오늘 날짜 데이터 더장
            if(!item.get("today").toString().isEmpty()) { // 오늘 관측 값 저장
                chunsik_data.setToday_val(item.get("today").toString());
            }
            chunsik_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 관측값 저장
        }
        if (act != null)
        {
            act.sum += Integer.parseInt(act.input.chunsik_data.today_val); // 오늘 천식 위험지수를 전체 위험 지수에 더함

            if((Integer.parseInt(act.input.chunsik_data.today_val)>1)) // 천식 위험지수가 나쁠 경우 팁에 추가
                act.tipStr[act.tip_num++] = String.valueOf("천식지수가 나쁘니 주의하세요");
        }
    }

    void get_cold(MainActivity act) throws IOException, ParseException {
        if (!cold_data.checkValid(t))
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

            cold_data.setDate(item.get("date").toString()); // 디버깅 용으로 관측 날짜를 저장

            if(!item.get("today").toString().isEmpty())
                cold_data.setToday_val(item.get("today").toString()); // 오늘 감기 가능 지수 데이터 저장

            cold_data.setTomorrow_val(item.get("tomorrow").toString()); // 내일 감기 가능 지수 데이터 저장 - 디버깅용
        }
        if (act != null)
        {
            act.sum += Integer.parseInt(act.input.cold_data.today_val); // 오늘의 위험도 총합에 감기 가능지수 위험도를 더함
            if((Integer.parseInt(act.input.cold_data.today_val)>1)) //감기 가능 지수가 위험인 경우 팁에 이에 대한 대사 추가
                act.tipStr[act.tip_num++] = String.valueOf(R.string.cold_bad);
        }
    }
}
