package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.snackbar.Snackbar;
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

class ScheduleDataModel {
    private Context context;
    private String scheduleJSON;
    private static final String TAG = "SCHEDULE_DATA_MODEL";
    private static final String URL = "https://github.com/arnabmaji19/Cloud/raw/master/Scheduler/Schedules/";
    private final static String FILE_EXTENSION = ".json";
    private int currentPeriod;
    private Time time;
    private final static int PERIOD_MAX_DURATION = 60;
    private final String selectedSchedule;
    private Snackbar snackbar;
    private View view;

    ScheduleDataModel(Context context, View view){
        this.context = context;
        this.view = view;
        time = new Time();
        selectedSchedule = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("selected_schedule",null);
        currentPeriod = time.getPeriod();
        Log.d(TAG, "ScheduleDataModel:"+selectedSchedule);
    }

    void syncSchedule(){ //Sync schedule via internet on github page.
        snackbar = Snackbar.make(view,"Syncing your schedule",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL+selectedSchedule+FILE_EXTENSION, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                writeToInternalStorage(response.toString()); //On success, save it to internal storage
                scheduleJSON = response.toString(); //Assign response for current use.
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context,"Failed to get your Schedule",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: failed to get schedule for "+selectedSchedule);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                snackbar.dismiss(); //Dismiss the snack bar on finish.
            }
        });
    }

    private void writeToInternalStorage(String string){ //Saving schedule as Text to Internal Storage.
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

    void readFromInternalStorage(){ //Reading schedule from Internal Storage for offline usage.
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
            Log.e(TAG, "readFromInternalStorage: failed to read from internal storage");
            e.printStackTrace();
        }
    }

    void updateTextViewFields(Object[] fields, boolean isOngoingClass){ //Updates Dashboard Classes
        try{
            String today = Time.getDayString(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
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
                ((TextView) fields[1]).setText(R.string.no_lectures);
                ((TextView) fields[2]).setVisibility(View.INVISIBLE);
                ((TextView) fields[3]).setVisibility(View.INVISIBLE);
                ((TextView) fields[5]).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "updateTextViewFields: error while parsing json");
        }
    }

    private void updateClassInfo(JSONArray day, Object[] fields, int period){ //Updates Dashboard fields
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
    private void updateCircularProgressBar(CircleProgressBar circleProgressBar){
        circleProgressBar.setMax(PERIOD_MAX_DURATION);
        circleProgressBar.setProgress(time.getElapsedTime()); //Sets Circular Progressbar for current time.
    }

    String getFullScheduleJson(){
        return this.scheduleJSON;
    }
}
