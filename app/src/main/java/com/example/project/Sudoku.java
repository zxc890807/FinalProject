package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Sudoku extends AppCompatActivity {
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Sudo(this));
        activity=this;
        MyService.zero();
    }

    public static void check(){
        activity.finish();
    }

}


class Sudo extends View {
    private static int H,M,S;
    private static String STR="147253968238469175695781234"
            +"456138729379624581812597346"+"923816457564972813781345690";
    public static String Ans2="147253968238469175695781234"
            +"456138729379624581812597346"+"923816457564972813781345692";
    public static String Ans="";
    private static Context mContext;
    private float width;
    private float height;
    private int All_w,All_h;
    // 用三維陣列存放x y 軸已存在的數字
    private int usedArray[][][] = new int[9][9][];
    // 用 Button 將案件紀錄下來
    private Button bt[] = new Button[9];
    private Button btn_clear;
    public static int Cor=0;

    public Sudo(Context context) {
        super(context);
        mContext = context;
        getUsedArray();
        H=0;M=0;S=0;
    }

    public static void difficult(int i){
        switch(i){
            // 簡單1~3
            case 1:
                STR="040250008030409170000081200"+"006000720000604000012000300"+"003810000064902010700045090";
                Ans2="147253968238469175695781234"+"456138729379624581812597346"+"923816457564972813781345692";
                break;
            case 2:
                STR="010230708000800605009100040"+"090081056000000000540390020"+"020003400904008000301042080";
                Ans2="615234798432879615879156243"+"297481356183625974546397821"+"728513469954768132361942587";
                break;
            case 3:
                STR="013200700700009035200000106"+"040072009002000400900140020"+"401000002630400007009007310";
                Ans2="513264798764819235298735146"+"146372859382596471957148623"+"471983562635421987829657314";
                break;
            //普通4~6
            case 4:
                STR="302005690040096030050008000"+"190080703000000000507030061"+"000800020080960070065700309";
                Ans2="372145698841296537956378214"+"194682753638517942527439861"+"719853426283964175465721389";
                break;
            case 5:
                STR="983000050105070009040060000"+"300800700720406035009007004"+"000050070500080402090000513";
                Ans2="983214657165378249247569381"+"314825796728496135659137824"+"432651978571983462896742513";
                break;
            case 6:
                STR="090104000400005800002009400"+"106080050249000781080040206"+"008400100005200009000506070";
                Ans2="893124567471365892652978413"+"136782954249653781587941236"+"368497125715238649924516378";
                break;
            //困難7~9
            case 7:
                STR="040210000807000090200800401"+"300002905005708600706500004"+"501004009060000708000027050";
                Ans2="649215387817436592253879461"+"384162975195748623726593814"+"571684239962351748438927156";
                break;
            case 8:
                STR="000000650000107002050968104"+"006090305070603080903010200"+"307529010500401000041000000";
                Ans2="198234657634157892752968134"+"416892375275643981983715246"+"267529418529481763841376529";
                break;
            case 9:
                STR="790100805200400070004000002"+"020500010310806094080001050"+"500000900040009006908005037";
                Ans2="796132845251468379834957162"+"427593618315826794689741253"+"573684921142379586968215437";
                break;
        }
    }


    // 計算x y 軸已有的數字
    public int[] computerXAndYUsed(int x, int y) {
        int used[] = new int[9];

        //  橫軸 (X)
        for (int i = 0; i < 9; i++) {
            int point = getValue(i, y);
            if (point != 0) {
                used[point - 1] = point;
            }
        }

        // 縱軸 (Y)
        for (int i = 0; i < 9; i++) {
            int point = getValue(x, i);
            if (point != 0) {
                used[point - 1] = point;
            }
        }

        // 尋找網格
        // 計算出 x y 軸在網格中的最初位置
        int xStart = (x / 3) * 3;
        int yStart = (y / 3) * 3;

        for (int i = xStart; i < xStart + 3; i++) {
            for (int j = yStart; j < yStart + 3; j++) {
                int point = getValue(i, j);
                if (point != 0) {
                    used[point -1] = point;
                }
            }
        }
        return used;
    }

    // 得到x y軸已有的資料
    public void getUsedArray() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                usedArray[i][j] = computerXAndYUsed(i, j);
            }
        }
    }

    // 修改九宮格內數字
    public void setSTR(int x, int y, char c) {
        // 得到 x y 的长度
        int index = y * 9 + x;
        String reSTR = STR.substring(0, index);
        reSTR += c;
        reSTR += STR.substring(index + 1, STR.length());
        STR = reSTR;
    }

    // 重新繪圖
    public void reDraw() {
        // 數字刷新
        getUsedArray();
        invalidate();
    }

    // 得到 x y軸的值
    public static int getValue(int x, int y) {
        return STR.charAt(y * 9 + x) - '0';
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 得到 九宫格 的高度和寬度
        All_w=getWidth();
        All_h=getHeight()-300;
        width = All_w / 9f;
        height = All_h / 9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Paint paint1= new Paint();
        //除鋸齒
        paint.setAntiAlias(true);

        //空心的矩形作為外背景
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(mContext, R.color.top2));
        canvas.drawRect(0, 0, All_w, All_h, paint);

        // 再繼續畫9條横軸 縱軸
        for (int i = 0; i < 9; i++) {
            paint.setColor(ContextCompat.getColor(mContext, R.color.back));

            // 横軸
            canvas.drawLine(0, i * height, All_w, i * height, paint);
            // 縱軸
            canvas.drawLine(i * width, 0, i * width, All_h, paint);

            //加深線條
            paint.setColor(ContextCompat.getColor(mContext, R.color.top1));

            // 横軸
            canvas.drawLine(0, i * height + 3, All_w, i * height + 3, paint);
            // 縱軸
            canvas.drawLine(i * width + 3, 0, i * width + 3, All_h, paint);

            //將目前的 九宫格 分為3塊
            if (i % 3 == 0) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.top2));
                // 横軸
                canvas.drawLine(0, i * height + 3, All_w, i * height + 3, paint);
                // 縱軸
                canvas.drawLine(i * width + 3, 0, i * width + 3, All_h, paint);
            }
        }

        //寫入數字在九宮格內
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize((float) (height * 0.75));
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int point = getValue(j, i);
                if (point != 0) {
                    canvas.drawText(String.valueOf(point), j * width + x, i * height + y, paint);
                }
            }
        }

        //寫入時間資料在下方
        paint1.setColor(Color.BLACK);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
        paint1.setTextAlign(Paint.Align.CENTER);
        paint1.setTextSize((float) (height * 1.3));
        String text=String.format("%02d:%02d:%02d",H,M,S);
        canvas.drawText(text,All_w-550,All_h+250,paint1);
        reDraw();
        super.onDraw(canvas);
    }

    public int[] getUsedToArray(int x, int y) {
        return usedArray[x][y];
    }

    // 觸控事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY()<=All_h) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final int x = (int) (event.getX() / width);
                final int y = (int) (event.getY() / height);
                int used[] = getUsedToArray(x, y);
                // 出現對話框，讓使用者填入數字
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();
                View item = LayoutInflater.from(mContext).inflate(R.layout.item_choose_layout, null);
                findAllBt(item);

                // 隱藏x y 軸已經出現的數字
                for (int i = 0; i < 9; i++) {
                    if (used[i] != 0) {
                        bt[i].setVisibility(View.INVISIBLE);
                    }
                }
                // 監聽數字按鈕
                for (int i = 0; i < 9; i++) {
                    final char t = (char) (i + 1 + '0');
                    bt[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSTR(x, y, t);
                            reDraw();
                            dialog.dismiss();
                        }
                    });
                }
                //監聽清除按鈕
                btn_clear.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final char zero = (char) (0+ '0');
                        setSTR(x,y,zero);
                        reDraw();
                        dialog.dismiss();
                    }
                });
                //Dialog對話框
                dialog.setView(item);
                dialog.setTitle("請選擇數字");
                dialog.setCancelable(true);
                dialog.show();

            }
        }
        else if (event.getY()>All_h)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("是否提交呢");
            dialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    CheckAnswer();
                }

            });
            dialog.setNeutralButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(mContext, "取消提交",Toast.LENGTH_SHORT).show();
                }

            });
            dialog.show();
        }

        return super.onTouchEvent(event);
    }

    public static void getclock(int Hour, int Minute, int Second){
        H=Hour;
        M=Minute;
        S=Second;
    }

    // 所有的 Button
    public void findAllBt(View view) {
        bt[0] = (Button) view.findViewById(R.id.bt1);
        bt[1] = (Button) view.findViewById(R.id.bt2);
        bt[2] = (Button) view.findViewById(R.id.bt3);
        bt[3] = (Button) view.findViewById(R.id.bt4);
        bt[4] = (Button) view.findViewById(R.id.bt5);
        bt[5] = (Button) view.findViewById(R.id.bt6);
        bt[6] = (Button) view.findViewById(R.id.bt7);
        bt[7] = (Button) view.findViewById(R.id.bt8);
        bt[8] = (Button) view.findViewById(R.id.bt9);
        btn_clear=(Button) view.findViewById(R.id.btn_clear);
    }

    public static void CheckAnswer(){
        Ans="";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int point = getValue(j, i);
                Ans=Ans+point;
            }
        }
        if (Ans.equals(Ans2)){
            Toast.makeText(mContext,"完成",Toast.LENGTH_SHORT).show();
            Sudoku.check();
            Finish.finish_hour(H);
            Finish.finish_min(M);
            Finish.finish_sec(S);
        }
        else{
            Toast.makeText(mContext,"答錯",Toast.LENGTH_SHORT).show();
        }
    }

}