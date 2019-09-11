package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class ScheduleDataModel {
    private Context context;
    private String scheduleJSON;
    private static final String TAG = "SCHEDULE_CLASS";
    private static final String URL = "https://github.com/arnabmaji19/Scheduler-android/raw/master/Schedules/31_2018.json";
    private int currentPeriod;
    private Time time;
    private final int PERIOD_MAX_DURATION = 60;

    public ScheduleDataModel(Context context){
        this.context = context;
        time = new Time();
        currentPeriod = time.getPeriod();
        readFromInternalStorage();
    }

    public void syncSchedule(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                writeToInternalStorage(response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG,"Failed to update over internet");
            }
        });
    }

    public  void writeToInternalStorage(String string){
        try{
            File file  = new File(context.getFilesDir(),"schedule.json");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(string);
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (Exception e){
            Log.e(TAG,"Failed to update or write json file");
            e.printStackTrace();
        }
    }

    public void readFromInternalStorage(){
        StringBuilder jsonString = new StringBuilder();
        try{
            FileInputStream fileInputStream = context.openFileInput("schedule.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String currentLine = bufferedReader.readLine();
            while(currentLine != null){
                jsonString.append(currentLine);
                currentLine = bufferedReader.readLine();
            }
            scheduleJSON = jsonString.toString();
        } catch (Exception e){
            Log.e(TAG,"Failed to get json file");
            e.printStackTrace();
        }
    }

    public void updateTextViewFields(Object[] fields, boolean isOngoingClass){
        try{
            String today = Time.getDayString(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            Log.i(TAG, "updateTextViewFields: "+today);
            Log.i(TAG, "updateTextViewFields: "+currentPeriod);
            if(!today.equals(Time.WEEK_END) &&  currentPeriod != -1){
                JSONObject schedule = new JSONObject(scheduleJSON);
                JSONArray day = schedule.getJSONArray(today);
                if(isOngoingClass){
                    updateClassInfo(day,fields,currentPeriod);
                    updateCircularProgressBar((CircleProgressBar) fields[4]);
                } else {
                    updateClassInfo(day,fields,currentPeriod+1);
                }
            } else {
                ((TextView) fields[0]).setVisibility(View.INVISIBLE);
                ((TextView) fields[1]).setText("No more Lectures");
                ((TextView) fields[2]).setVisibility(View.INVISIBLE);
                ((TextView) fields[3]).setVisibility(View.INVISIBLE);
                ((TextView) fields[5]).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "error while parsing json");
        }
    }

    public void updateClassInfo(JSONArray day, Object[] fields, int period){
        try {
            JSONObject currentPeriodJSON = day.getJSONObject(period-1);
            String lectureTextView = "Lecture No. " + period;
            ((TextView) fields[0]).setText(lectureTextView);
            ((TextView) fields[1]).setText(currentPeriodJSON.getString("Subject"));
            ((TextView) fields[2]).setText(currentPeriodJSON.getString("Faculty"));
            ((TextView) fields[3]).setText(currentPeriodJSON.getString("Room"));
            ((TextView) fields[5]).setText(currentPeriodJSON.getString("Duration"));
        } catch (Exception e){
            Log.e(TAG, "failed to parse json");
        }
    }
    public void updateCircularProgressBar(CircleProgressBar circleProgressBar){
        circleProgressBar.setMax(PERIOD_MAX_DURATION);
        circleProgressBar.setProgress(time.getElapsedTime());
    }
}
