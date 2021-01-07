package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends AppCompatActivity {
    private static int hour,min,sec;
    private String time;
    private TextView finish_time;
    private Button btn_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

    }

    @Override
    protected void onResume() {
        super.onResume();
        finish_time=findViewById(R.id.finish_time);
        btn_end=findViewById(R.id.btn_end);

        if (hour==0 && min==0) time="共耗時"+sec+"秒";
        if (hour==0 && sec==0) time="共耗時"+min+"分整";
        if (min==0 && sec==0) time="共耗時"+hour+"小時整";
        if (hour==0 && min!=0 && sec!=0) time="共耗時"+min+"分"+sec+"秒";
        if (hour!=0 && min==0 && sec!=0) time="共耗時"+hour+"個小時"+sec+"秒";
        if (hour!=0 && min!=0 && sec==0) time="共耗時"+hour+"個小時"+min+"分";
        if (hour!=0 && min!=0 && sec!=0) time="共耗時"+hour+"個小時"+min+"分"+sec+"秒";
        finish_time.setText(time);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Finish.this)
                        .setTitle("請選擇難度")
                        .setItems(R.array.dialog_arrays, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //MainActivity.chooseDiff(which); //此行標註掉進入測試模式
                                Intent intent = new Intent(Finish.this, Sudoku.class);
                                startActivity(intent);
                                MyService.zero();
                            }
                        }).create().show();
            }
        });


    }

    public static void finish_sec(int second){
        sec=second;
    }
    public static void finish_min(int minute){
        min=minute;
    }
    public static void finish_hour(int hours){
        hour=hours;
    }


}