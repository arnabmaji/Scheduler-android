package io.github.arnabmaji19.scheduler;
import java.util.Calendar;
import java.util.Locale;

public class Time {
    private static String TAG = "TIME_CLASS";
    private int currentTime;
    private Calendar calendar;
    static final String WEEK_END = "Off Day";
    private String periodStartTimeString;


    public Time(){
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String minuteString = String.format(Locale.getDefault(),"%02d",calendar.get(Calendar.MINUTE));
        currentTime = Integer.parseInt(hour+minuteString);
    }

    public int getPeriod(){
        int currentPeriod = -1;
        int startTime = 0;
        if(currentTime >= 900 && currentTime < 955){
            currentPeriod = 1;
            startTime = 900;
        } else if(currentTime >= 955 && currentTime < 1050){
            currentPeriod = 2;
            startTime = 955;
        } else if(currentTime >= 1050 && currentTime < 1145){
            currentPeriod = 3;
            startTime = 1050;
        } else if(currentTime >= 1145 && currentTime < 1240){
            currentPeriod = 4;
            startTime = 1145;
        } else if(currentTime >= 1240 && currentTime < 1335){
            currentPeriod = 5;
            startTime = 1240;
        } else if(currentTime >= 1335 && currentTime < 1430){
            currentPeriod = 6;
            startTime = 1335;
        } else if(currentTime >= 1430 && currentTime < 1525){
            currentPeriod = 7;
            startTime = 1430;
        } else if(currentTime >= 1525 && currentTime < 1620){
            currentPeriod = 8;
            startTime = 1525;
        }
        periodStartTimeString = String.format(Locale.getDefault(),"%04d",startTime);
        return currentPeriod;
    }

    public static String getDayString(int weekNo){
        switch (weekNo){
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
        }
        return "Off Day";
    }
    public static int getDayNo(String dayString){
        int dayNo = -1;
        switch (dayString){
            case "Monday":
                dayNo = Calendar.MONDAY;
                break;
            case "Tuesday":
                dayNo = Calendar.TUESDAY;
                break;
            case "Wednesday":
                dayNo = Calendar.WEDNESDAY;
                break;
            case "Thursday":
                dayNo = Calendar.THURSDAY;
                break;
            case "Friday":
                dayNo = Calendar.FRIDAY;
                break;
        }
        return dayNo;
    }
    public int getElapsedTime(){
        String currentTime = String.format(Locale.getDefault(),"%04d",this.currentTime);
        String startTime = periodStartTimeString;
        int currentHour = Integer.parseInt(currentTime.substring(0,2));
        int currentMinute = Integer.parseInt(currentTime.substring(2));
        int startHour = Integer.parseInt(startTime.substring(0,2));
        int startMinute = Integer.parseInt(startTime.substring(2));
        int elapsedMinute = 0;
        if(currentMinute >= startMinute){
            elapsedMinute += (currentMinute - startMinute);
        } else {
            currentHour--;
            currentMinute += 60;
            elapsedMinute += (currentMinute - startMinute);
        }
        return elapsedMinute + (currentHour - startHour) * 60;
    }
}
