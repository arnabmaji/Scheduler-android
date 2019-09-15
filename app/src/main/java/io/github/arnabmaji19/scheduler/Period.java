package io.github.arnabmaji19.scheduler;

import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

class Period {
    private TextView lectureNumber;
    private TextView subject;
    private TextView subjectTeacher;
    private TextView room;
    private TextView time;
    private CircleProgressBar circleProgressBar;

    Period(TextView lectureNumber, TextView subject, TextView subjectTeacher, TextView room, TextView time, CircleProgressBar circleProgressBar) {
        this.lectureNumber = lectureNumber;
        this.subject = subject;
        this.subjectTeacher = subjectTeacher;
        this.room = room;
        this.time = time;
        this.circleProgressBar = circleProgressBar;
    }
    void setPeriodInformation(String lectureNumber, String subject, String subjectTeacher, String room, String time){
        this.lectureNumber.setText(lectureNumber);
        this.subject.setText(subject);
        this.subjectTeacher.setText(subjectTeacher);
        this.room.setText(room);
        this.time.setText(time);
    }

    void setCircleProgressBar(int maxTime, int elapsedTime){
        circleProgressBar.setMax(maxTime);
        circleProgressBar.setProgress(elapsedTime);
    }
}
