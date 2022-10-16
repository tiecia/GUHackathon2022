package com.example.runningpacenotifier;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.runningpacenotifier.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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

    private Button addButton;
    private Button subButton;

    private int MIN_TARGET_SIZE = 20;
    private int MAX_TARGET_SIZE = 90;

    private int SCROLL_BOTTOM = 450;
    private int SCROLL_TOP = 0;

    private int MARGIN_CHANGE = 80;


    private LocationService locationService;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationService = new LocationService(this, this, fusedLocationProviderClient);

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

        addButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
                String newTime = addTime((String) binding.targetPace.getText());
           }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newTime = subTime((String) binding.targetPace.getText());
            }
        });

        timeChangeTimer = new Timer(true);
        timeChangeTimer.scheduleAtFixedRate(timeChangeTask, 0, 1000);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                System.out.println(scrollY);


                int newSize = scale(scrollY, SCROLL_TOP, SCROLL_BOTTOM, MIN_TARGET_SIZE, MAX_TARGET_SIZE);
                int newTopMargin = scale(scrollY, SCROLL_TOP, SCROLL_BOTTOM, 0, MARGIN_CHANGE);
//                System.out.println("ScrollHeight: " + SCROLL_BOTTOM);
//                System.out.println("NewSize: " + newSize);
                targetPaceView.setTextSize(newSize);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0,newTopMargin,0,MARGIN_CHANGE-newTopMargin);
                targetPaceView.setLayoutParams(params);
            }
        });
    }

    private String subTime(String time) {
        String[] times = time.split(":");
        int hour = Integer.valueOf(times[0]);
        int min = Integer.valueOf(times[1]);
        if(min < 10) {
            min = (60 + min) - 10;
            hour --;
        } else {
            min -= 10;
        }
        return String.format("%02d", hour) + ":" + String.format("%02d", min);
    }

    private String addTime(String time) {
        String[] times = time.split(":");
        int hour = Integer.valueOf(times[0]);
        int min = Integer.valueOf(times[1]);
        if(min >= 50) {
            min = (min - 60) + 10;
        } else {
            min += 10;
        }
        return String.format("%02d", hour) + ":" + String.format("%02d", min);
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
