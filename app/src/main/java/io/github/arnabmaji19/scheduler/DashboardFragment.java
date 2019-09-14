package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class DashboardFragment extends Fragment {

    private final ScheduleDataModel scheduleDataModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Object[] fields = {view.findViewById(R.id.lectureNumberTextView),
                view.findViewById(R.id.subjectTextView),
                view.findViewById(R.id.subjectTeacherTextView),
                view.findViewById(R.id.classRoomTextView),
                view.findViewById(R.id.circular_progress),
                view.findViewById(R.id.timeTextView)};
        Object[] nextFields = {view.findViewById(R.id.nextLectureNumberTextView),
                view.findViewById(R.id.nextSubjectTextView),
                view.findViewById(R.id.nextSubjectTeacherTextView),
                view.findViewById(R.id.nextClassRoomTextView),
                view.findViewById(R.id.nextCircular_progress),
                view.findViewById(R.id.nextTimeTextView)};
        scheduleDataModel.updateTextViewFields(fields,true);
        scheduleDataModel.updateTextViewFields(nextFields,false);
        return view;
    }

    public DashboardFragment(ScheduleDataModel scheduleDataModel){
        this.scheduleDataModel = scheduleDataModel;
    }
}
