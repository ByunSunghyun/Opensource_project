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
                    input.get_Allergy_charmTree(null);
                    input.get_Allergy_SoTree(null);
                    input.get_Allergy_Jopcho(null);
                    input.getWeather(null);
                    input.getAirKor(null);
                    input.get_cold(null);
                    input.get_Allergy_Chunsik(null);
                    // 각 API 데이터에 따른 위험도 총합 구하기
                    sum = get_sumAll(input);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

            }
        })).start();
        // 위험도에 따른 이미지 설정

        switch (sum)
        {
            case 0: views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji0); break;
            case 1: views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji1); break;
            case 2: views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji2); break;
            default: views.setImageViewResource(R.id.appwidget_image, R.drawable.emoji3); break;
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
    static int get_sumAll(data in) {
        // 오늘 데이터에 따른 위험도 총합
        int i = 0;
        i = i + Integer.parseInt(in.cold_data.today_val);
        i = i + Integer.parseInt(in.charmTree_data.today_val);
        i = i + Integer.parseInt(in.jopcho_data.today_val);
        i = i + Integer.parseInt(in.soTree_data.today_val);
        i = i + Integer.parseInt(in.chunsik_data.today_val);
        i = i + Integer.parseInt(in.airKorDust_data.pm10Grade1h);
        i = i + Integer.parseInt(in.airKorDust_data.pm25Grade1h);
        i = i / 4;
        return i;
    }
}