package com.zidian.slidingcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn_main_calendar);
    }

    public void selectDate(View view){
        Intent intent = new Intent(this, SelectDateActivity.class);
        startActivity(intent);
    }
}
