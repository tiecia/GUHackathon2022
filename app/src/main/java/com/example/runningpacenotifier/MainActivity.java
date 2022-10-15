package com.example.runningpacenotifier;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.runningpacenotifier.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.*;


public class MainActivity extends Activity {

    private TextView timeView;
    private TextView currentPaceView;
    private TextView targetPaceView;
    private ActivityMainBinding binding;

    private ScrollView scrollView;

    private TimerTask timeChangeTask;
    private Timer timeChangeTimer;

    private int MIN_TARGET_SIZE = 20;
    private int MAX_TARGET_SIZE = 90;

    private int SCROLL_BOTTOM = 450;
    private int SCROLL_TOP = 0;

    private int MARGIN_CHANGE = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timeView = binding.timeOfDay;
        currentPaceView = binding.currentPace;
        targetPaceView = binding.targetPace;

        scrollView = binding.scrollView;

        updateDisplayedTime();

        timeChangeTask = new TimerTask() {
            @Override
            public void run() {
                updateDisplayedTime();
            }
        };

        timeChangeTimer = new Timer(true);
        timeChangeTimer.scheduleAtFixedRate(timeChangeTask, 0, 1000);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                System.out.println(scrollY);


                int newSize = scale(scrollY, SCROLL_TOP, SCROLL_BOTTOM, MIN_TARGET_SIZE, MAX_TARGET_SIZE);
                int newTopMargin = scale(scrollY, SCROLL_TOP, SCROLL_BOTTOM, 0, MARGIN_CHANGE);
                System.out.println("ScrollHeight: " + SCROLL_BOTTOM);
                System.out.println("NewSize: " + newSize);
                targetPaceView.setTextSize(newSize);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0,newTopMargin,0,MARGIN_CHANGE-newTopMargin);
                targetPaceView.setLayoutParams(params);
            }
        });

    }

    private String getFormattedDisplayTime() {
        Calendar calendar = new GregorianCalendar(GregorianCalendar.getInstance().getTimeZone());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int am_pm_id = calendar.get(Calendar.AM_PM);
        String am_pm = "";
        switch (am_pm_id){
            case 0:
                am_pm = "AM";
            case 1:
                am_pm = "PM";
        }
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    private void updateDisplayedTime() {
        timeView.setText(getFormattedDisplayTime());
    }

    private int scale(int number, int inMin, int inMax, int outMin, int outMax) {
        return (number - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
