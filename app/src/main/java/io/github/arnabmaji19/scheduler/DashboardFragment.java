package io.github.arnabmaji19.scheduler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinuscxj.progressbar.CircleProgressBar;


public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        CircleProgressBar circleProgressBar = view.findViewById(R.id.progress_circular);
        circleProgressBar.setMax(100);
        circleProgressBar.setProgress(90);
        return view;
    }
}
