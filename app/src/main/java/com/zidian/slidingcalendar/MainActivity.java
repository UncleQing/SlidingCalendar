package com.zidian.slidingcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.zidian.slidingcalendar.SelectDateActivity.TAG_SELECT;

public class MainActivity extends AppCompatActivity {


    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn_main_calendar);
    }

    public void selectDate(View view) {
        Intent intent = new Intent(this, SelectDateActivity.class);
        startActivityForResult(intent, 666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 666) {
            if (data != null) {
                String msg = data.getStringExtra(TAG_SELECT);
                btn.setText(msg);
            }
        }
    }
}
