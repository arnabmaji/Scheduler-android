package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dinuscxj.progressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private final String scheduleJson;
    private Context context;
    private final int MAX_PERIODS = 8;
    private Period[] periods;
    private View view;
    private final static String TAG = "SCHEDULE FRAGMENT";
    private final static int PERIOD_MAX_DURATION = 60;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_schedule,container,false);
        periods = new Period[MAX_PERIODS];
        initializeAllPeriodsFields();
        String[] WEEK_DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
        Spinner weekDaySpinner = view.findViewById(R.id.weekDaySpinner); //Setting up spinner for week days
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,WEEK_DAYS);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekDaySpinner.setAdapter(arrayAdapter);
        weekDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showScheduleForDay(Time.getDayString(i+2));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return this.view;
    }

    ScheduleFragment(Context context , String scheduleJson){
        this.context = context;
        this.scheduleJson = scheduleJson;
    }

    private void showScheduleForDay(String day){ //Gets all schedules for current day
        try{
            JSONArray periodJson = new JSONObject(scheduleJson).getJSONArray(day);
            for(int i=0;i<MAX_PERIODS;i++){
                JSONObject currentPeriod = periodJson.getJSONObject(i);
                periods[i].setPeriodInformation("Lecture No. "+ (i+1),
                        currentPeriod.getString("Subject"),
                        currentPeriod.getString("Faculty"),
                        currentPeriod.getString("Room"),
                        currentPeriod.getString("Duration"));
                Time time = new Time();
                int currentPeriodNo = time.getPeriod();
                if(Time.getDayNo(day) < Calendar.getInstance().get(Calendar.DAY_OF_WEEK) || (Time.getDayNo(day) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && (i+1) < currentPeriodNo) || (Time.getDayNo(day) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && currentPeriodNo == -1)){
                    periods[i].setCircleProgressBar(PERIOD_MAX_DURATION,PERIOD_MAX_DURATION);
                } else if(Time.getDayNo(day) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && (i+1) == currentPeriodNo){
                    periods[i].setCircleProgressBar(PERIOD_MAX_DURATION,time.getElapsedTime());
                } else {
                    periods[i].setCircleProgressBar(PERIOD_MAX_DURATION,0);
                }
            }
        } catch (Exception e){
            Log.e(TAG, "showScheduleForDay: error while parsing json");
        }
    }

    private void initializeAllPeriodsFields(){ //links all view in a period
        periods[0] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView0),
                (TextView) view.findViewById(R.id.subjectTextView0),
                (TextView) view.findViewById(R.id.subjectTeacherTextView0),
                (TextView) view.findViewById(R.id.classRoomTextView0),
                (TextView) view.findViewById(R.id.timeTextView0),
                (CircleProgressBar) view.findViewById(R.id.circular_progress0));
        periods[1] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView1),
                (TextView) view.findViewById(R.id.subjectTextView1),
                (TextView) view.findViewById(R.id.subjectTeacherTextView1),
                (TextView) view.findViewById(R.id.classRoomTextView1),
                (TextView) view.findViewById(R.id.timeTextView1),
                (CircleProgressBar) view.findViewById(R.id.circular_progress1));
        periods[2] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView2),
                (TextView) view.findViewById(R.id.subjectTextView2),
                (TextView) view.findViewById(R.id.subjectTeacherTextView2),
                (TextView) view.findViewById(R.id.classRoomTextView2),
                (TextView) view.findViewById(R.id.timeTextView2),
                (CircleProgressBar) view.findViewById(R.id.circular_progress2));
        periods[3] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView3),
                (TextView) view.findViewById(R.id.subjectTextView3),
                (TextView) view.findViewById(R.id.subjectTeacherTextView3),
                (TextView) view.findViewById(R.id.classRoomTextView3),
                (TextView) view.findViewById(R.id.timeTextView3),
                (CircleProgressBar) view.findViewById(R.id.circular_progress3));
        periods[4] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView4),
                (TextView) view.findViewById(R.id.subjectTextView4),
                (TextView) view.findViewById(R.id.subjectTeacherTextView4),
                (TextView) view.findViewById(R.id.classRoomTextView4),
                (TextView) view.findViewById(R.id.timeTextView4),
                (CircleProgressBar) view.findViewById(R.id.circular_progress4));
        periods[5] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView5),
                (TextView) view.findViewById(R.id.subjectTextView5),
                (TextView) view.findViewById(R.id.subjectTeacherTextView5),
                (TextView) view.findViewById(R.id.classRoomTextView5),
                (TextView) view.findViewById(R.id.timeTextView5),
                (CircleProgressBar) view.findViewById(R.id.circular_progress5));
        periods[6] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView6),
                (TextView) view.findViewById(R.id.subjectTextView6),
                (TextView) view.findViewById(R.id.subjectTeacherTextView6),
                (TextView) view.findViewById(R.id.classRoomTextView6),
                (TextView) view.findViewById(R.id.timeTextView6),
                (CircleProgressBar) view.findViewById(R.id.circular_progress6));
        periods[7] = new Period((TextView) view.findViewById(R.id.lectureNumberTextView7),
                (TextView) view.findViewById(R.id.subjectTextView7),
                (TextView) view.findViewById(R.id.subjectTeacherTextView7),
                (TextView) view.findViewById(R.id.classRoomTextView7),
                (TextView) view.findViewById(R.id.timeTextView7),
                (CircleProgressBar) view.findViewById(R.id.circular_progress7));
    }
}
