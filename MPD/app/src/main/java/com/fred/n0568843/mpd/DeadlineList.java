package com.fred.n0568843.mpd;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by N0568843 on 20/03/2018.
 */

public class DeadlineList extends ArrayAdapter<Deadline> {
    private final Activity context;
    private final ArrayList<Deadline> deadline;
    private Runnable runnable;
    private Handler handler = new Handler();
    private String DATE_FORMAT = "dd-MM-yyy HH:mm";
    View rowView;
    TextView countdownTest;
    public DeadlineList(Activity context,
                         ArrayList<Deadline> deadline) {
        super(context, R.layout.list_single, deadline);
        this.context = context;
        this.deadline = deadline;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView= inflater.inflate(R.layout.deadline_list_layout, null, true);
        TextView deadlineTitle = (TextView) rowView.findViewById(R.id.deadlineTitle);
        TextView deadlineDateTime = (TextView) rowView.findViewById(R.id.deadlineDateTime);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        deadlineTitle.setText(deadline.get(position).deadlineName);
        deadlineDateTime.setText(deadline.get(position).deadlineDate + " at " + deadline.get(position).deadlineTime);
        countdownStart(deadline.get(position).deadlineDate, deadline.get(position).deadlineTime);
        return rowView;
    }
    public void countdownStart(final String deadlineDate1, final String deadlineTime1)
    {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(deadlineDate1 + " " + deadlineTime1);
                    Date current_date = new Date();
                    if (1 < 2) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        //
                        String setTimerValue = String.format("%02d", Days) + " Days " + String.format("%02d", Hours) + " Hours " + String.format("%02d", Minutes) + " Minutes";
                        countdownTest = rowView.findViewById(R.id.deadlineCountdown);

                        countdownTest.setText(setTimerValue);
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

}
