package com.zidian.slidingcalendar;

import android.app.Activity;
import android.os.Bundle;

import com.zidian.slidingcalendar.view.SlidingCalendarView;

public class SelectDateActivity extends Activity {

    private SlidingCalendarView scv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        scv = findViewById(R.id.scv_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scv != null) {
            scv.onDestroy();
        }
    }
}
