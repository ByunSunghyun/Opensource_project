package com.example.opensource1;

public class data {
    air_kor_dust airKorDust_data; // 미세먼지 데이터 클래스
    chunsik chunsik_data; // 천식 데이터 클래스
    soTree soTree_data; // 소나무 알러지 데이터 클래스
    charmTree charmTree_data; // 참나무 알러지 데이터 클래스
    jopcho jopcho_data; // 잡초류 알러지 데이터 클래스
    weather weather_data; // 날씨 데이터 클래스
    cold cold_data; // 감기 가능지수 데이터 클래스

    data(){ // 생성자에서 각 데이터 클래스 인스턴스 생성
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

}
