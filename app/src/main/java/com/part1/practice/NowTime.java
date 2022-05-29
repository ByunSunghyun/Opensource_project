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
    public String getMonth() {
        return this.month;
    }
    public String getDay() {
        return this.day;
    }
    public String getYear() {
        return this.year;
    }
    public String getHour() {
        return this.hour;
    }

    public void Initalization() {
        //오늘
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        // 1시간전
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.HOUR, -1);    // 포맷변경 ( 년월일 시분초)
        this.year = new SimpleDateFormat("yyyy").format(cal.getTime());
        this.month = new SimpleDateFormat("MM").format(cal.getTime());
        this.day = new SimpleDateFormat("dd").format(cal.getTime());

        this.hour = new SimpleDateFormat("HH").format(cal.getTime());
        this.min = new SimpleDateFormat("mm").format(cal.getTime());

        this.time = new SimpleDateFormat("yyyyMMddHH").format(cal.getTime());

        this.base_date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        this.base_time = new SimpleDateFormat("HH").format(cal.getTime()) +"00";

        int time_min = Integer.parseInt(this.hour + this.min);
        if (time_min < 40)
        {
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -1);    // 포맷변경 ( 년월일 시분초)
            this.year = new SimpleDateFormat("yyyy").format(cal.getTime());
            this.month = new SimpleDateFormat("MM").format(cal.getTime());
            this.day = new SimpleDateFormat("dd").format(cal.getTime());

            this.hour = new SimpleDateFormat("HH").format(cal.getTime());
            this.min = new SimpleDateFormat("mm").format(cal.getTime());

            this.time = new SimpleDateFormat("yyyyMMddHH").format(cal.getTime());

            this.base_date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
            this.base_time = "2300";
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