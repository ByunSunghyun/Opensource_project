package com.example.opensource1;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NowTime {
    private String month;
    private String day;
    private String year;

    private String hour;
    private String min;

    private String time; // allergy, chunsik이 사용하는 time
    private String base_time; // weather에서 사용하는 base_time
    private String base_date; // weather에서 사용하는 base_date

    NowTime(){
        this.Initalization();
    }

    //현재 Month를 반환하는 함수
    public String getMonth() {
        return this.month;
    }

    public void Initalization() {
        //현 시스템 시간을 가져옴
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        // Date 클래스를 이용하여 API 기준 시간으로 맞추기 위해 시간 연산을 지원하는 Calendar 객체 생성
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        // 1시간 전 기준으로 가져옴
        cal.add(Calendar.HOUR, -1);

        // 포맷변경 ( 년월일 시분초)
        this.year = new SimpleDateFormat("yyyy").format(cal.getTime()); // 현재 시간 년 데이터
        this.month = new SimpleDateFormat("MM").format(cal.getTime()); // 현재 시간 월 데이터
        this.day = new SimpleDateFormat("dd").format(cal.getTime()); // 현재 시간 일자 데이터
        this.hour = new SimpleDateFormat("HH").format(cal.getTime()); // 현재 시간 시각 데이터
        this.min = new SimpleDateFormat("mm").format(cal.getTime()); // 현재 시간 분 데이터
        this.time = new SimpleDateFormat("yyyyMMddHH").format(cal.getTime()); // 년월일시간 기준으로 변경
        this.base_date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()); // 년월일 기준 으로 변경
        this.base_time = new SimpleDateFormat("HH").format(cal.getTime()) +"00"; // 정각 기준 시각으로 변경

        int time_min = Integer.parseInt(this.hour + this.min); // 현재 시간 데이터를 정수형 비교를 위한 변환
        if (time_min < 40) // base_time, base_date 데이터가 비교를 위해 00시 40분 미만인 경우 당일 데이터가 아직 기록된 것이 없기에 하루 전 가장 최신 데이터 가져옴
        {
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -1);    // 하루전으로 시간 변경
            this.year = new SimpleDateFormat("yyyy").format(cal.getTime()); // 년 데이터
            this.month = new SimpleDateFormat("MM").format(cal.getTime()); // 월 데이터
            this.day = new SimpleDateFormat("dd").format(cal.getTime()); // 일 데이터

            this.hour = new SimpleDateFormat("HH").format(cal.getTime()); // 시간 데이터
            this.min = new SimpleDateFormat("mm").format(cal.getTime()); // 분 데이터

            this.time = new SimpleDateFormat("yyyyMMddHH").format(cal.getTime()); // 년월일시간 데이터

            this.base_date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()); // 년월일 데이터
            this.base_time = "2300"; // 하루 전 가장 최신 시간 데이터는 23시 기준
        }

    }

    String getTime() {
        return this.time;
    }
    String getBase_time(){
        return this.base_time;
    }
    String getBase_date() {
        return this.base_date;
    }
}