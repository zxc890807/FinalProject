package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.project.MyService.flag;

public class MainActivity extends AppCompatActivity {
    private Boolean flag;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b=intent.getExtras();
            Sudo.getclock(b.getInt("H"),b.getInt("M"),b.getInt("S"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(receiver,new IntentFilter("MyMessage"));
        flag=MyService.flag;
        flag=true;
        Intent intent1= new Intent(MainActivity.this,MyService.class);
        startService(intent1.putExtra("flag",flag));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        Button btn_start=findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("請選擇難度")
                        .setItems(R.array.dialog_arrays, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //chooseDiff(which); //此行標註掉進入測試模式
                                Intent intent = new Intent(MainActivity.this, Sudoku.class);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(MainActivity.this, Finish.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this,MyService.class));
        unregisterReceiver(receiver);
    }



    public static void chooseDiff(int i){
        int ram=(int)(Math.random()*(3-1+1)) + 1;;
        switch (i){
            case 0:
                Sudo.difficult(ram);
                break;
            case 1:
                Sudo.difficult(ram+3);
                break;
            case 2:
                Sudo.difficult(ram+6);
                break;
        }
    }
}