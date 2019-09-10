package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Schedule {
    private Context context;
    private String scheduleJSON;
    private static final String TAG = "SCHEDULE_CLASS";

    public Schedule(Context context){
        this.context = context;
    }

    public void syncScheduleJSON(){

    }

    public void updateScheduleJSON(String string){
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

    public void getScheduleJSON(){
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
}
