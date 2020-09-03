package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import android.os.CountDownTimer;
import android.view.*;

import static java.lang.Thread.*;

public class MainActivity extends AppCompatActivity{
    public int count = 0;
    public static CountDownTimer timer;
    EditText inRunLow;
    EditText inRunHigh;
    EditText inRestLow;
    EditText inRestHigh;
    EditText repeat;
    Button submit, start, intro, reset, abort;
    TextView instruct, counter, repDisplay;
    CheckBox auto;
    Boolean autoStart;
    int runMin, runMax, restMin, restMax, rep, repView;
    public boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inRunLow = (EditText) findViewById(R.id.RunLow);
        inRunHigh = (EditText) findViewById(R.id.RunHigh);
        inRestLow = (EditText) findViewById(R.id.RestLow);
        inRestHigh = (EditText) findViewById(R.id.RestHigh);
        repeat = (EditText) findViewById(R.id.Repeat);
        submit = (Button) findViewById(R.id.Submit);
        start = (Button) findViewById(R.id.Start);
        intro = (Button) findViewById(R.id.Intro);
        reset = (Button) findViewById(R.id.Reset);
        abort = (Button) findViewById(R.id.Abort);
        instruct = (TextView)findViewById(R.id.instruct);
        counter = (TextView)findViewById(R.id.counter);
        auto = (CheckBox)findViewById(R.id.checkBox);
        repDisplay = (TextView)findViewById(R.id.repView);

        instruct.setText("Welcome to Fartlek Run");

        submit.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (isEmpty(inRunLow, inRunHigh, inRestLow, inRestHigh, repeat))
                {
                    instruct.setText("input ALL values and try again");
                }

                else
                {
                    runMin = (int) (Integer.parseInt(inRunLow.getText().toString()) * 1000);
                    runMax = (int) (Integer.parseInt(inRunHigh.getText().toString()) * 1000);
                    restMin = (int) (Integer.parseInt(inRestLow.getText().toString()) * 1000);
                    restMax = (int) (Integer.parseInt(inRestHigh.getText().toString()) * 1000);
                    rep = (int) (Integer.parseInt(repeat.getText().toString())*2);
                    repView = rep/2;
                    autoStart = auto.isChecked();
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                setStart(v);
                rep--;
                intro.setVisibility(v.GONE);
                submit.setVisibility(v.GONE);
            }
        });

        intro.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                instruct.setPadding(20,0,20,0);
                instruct.setTextSize(18);
                instruct.setText("The word “fartlek” is a Swedish term which means “speed play.” It is a randomized interval training plan that mixes hardcore sprinting with slower jogging or walking. It has been shown that fartlek runs helps burn more fat, preserves muscle mass and improves cardiovascular fitness when compared to continuous linear runs.");
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if (rep <= 0){
                    count = 0;
                    inRunLow.setText("");
                    inRunHigh.setText("");
                    inRestLow.setText("");
                    inRestHigh.setText("");
                    repeat.setText("");
                    instruct.setVisibility(v.VISIBLE);
                    instruct.setTextSize(24);
                    instruct.setText("Welcome to Fartlek Run");
                    start.setVisibility(v.VISIBLE);
                    submit.setVisibility(v.VISIBLE);
                    counter.setVisibility(v.VISIBLE);
                    repDisplay.setText("");
                    counter.setText("");
                    intro.setVisibility(v.VISIBLE);
                }
            }
        });

        abort.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                rep = 0;
                timer.cancel();
                instruct.setText("Run Aborted");
                start.setVisibility(v.GONE);
                counter.setVisibility(v.GONE);
                repDisplay.setText("");
            }
        });
    }

    public void setStart(View v)
    {
        int time;

        if (rep <= 0)
        {
            instruct.setText("Run Completed!");
            start.setVisibility(v.GONE);
            counter.setVisibility(v.GONE);
        }

        else{
            if(run){
                time = (int)(runMin + (runMax-runMin)*Math.random());
                instruct.setText("RUN!");
                repView--;
            }

            else{
                time = (int)(restMin + (restMax-restMin)*Math.random());
                instruct.setText("Take a break!");
            }

            repDisplay.setText("Runs left: " + repView);
            start.setVisibility(v.GONE);
            startTimer(v,time);
            counter.setText("");
            run = !run;
        }
    }

    public boolean isEmpty(EditText a, EditText b, EditText c, EditText d, EditText e){
        boolean empty=false;
        if (TextUtils.isEmpty(a.getText()) || TextUtils.isEmpty(b.getText()) || TextUtils.isEmpty(c.getText()) || TextUtils.isEmpty(d.getText()) || TextUtils.isEmpty(e.getText())){
            empty = true;
        }
        return empty;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "1";
            String description = "Test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
                channel.setDescription(description);
                channel.enableVibration(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void startTimer(View view, int n){
        final TextView counter = findViewById(R.id.counter);
        final TextView instruct = findViewById(R.id.instruct);
        final boolean k = run;
        final View v = view;
        count = n/1000;

            timer = new CountDownTimer (n,1000)
            {

                @Override
                public void onTick(long l)
                {
                    counter.setText(String.valueOf(count));
                    count--;
                }

                @Override
                public void onFinish()
                {
                    start.setVisibility(v.VISIBLE);

                    if (k)
                    {
                        instruct.setText("RUN!");
                    }

                    else
                    {
                        instruct.setText("Take a break!");
                    }

                    if (autoStart)
                    {
                        start.callOnClick();
                    }

                    createNotificationChannel();

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "1")
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("Fartlek Run")
                            .setContentText("Timer done!")
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

                    notificationManager.notify(1, builder.build());

                }
            }.start();

        }

    }
