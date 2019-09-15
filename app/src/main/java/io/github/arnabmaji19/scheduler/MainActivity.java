package io.github.arnabmaji19.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SCHEDULER_MAIN_ACTIVITY";
    private Fragment currentFragment;
    private Fragment dashboardFragment;
    private ScheduleDataModel scheduleDataModel;
    private static final String AVAILABLE_SCHEDULES_URL = "https://github.com/arnabmaji19/Cloud/raw/master/Scheduler/Schedules/available_schedules.txt";
    private String[] availableSchedules;
    private Snackbar snackbar;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        scheduleDataModel = new ScheduleDataModel(this,findViewById(R.id.main_layout));
        if(sharedPreferences.getString("selected_schedule",null) == null){ //in case user is opening app for first time, open choose dialog and select schedule
            if(isInternetConnectionAvailable()){
                availableSchedules = null;
                showAvailableSchedules();
            } else { //In case internet connection not available for first time, show error dialog and close app.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Internet Connection");
                builder.setCancelable(false);
                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        } else { //If already exists, read from internal storage
            updateAndShowSchedule(false);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.bottom_dashboard:
                        selectedFragment = dashboardFragment;
                        break;
                    case R.id.bottom_schedule:
                        selectedFragment = new ScheduleFragment(MainActivity.this, scheduleDataModel.getFullScheduleJson());
                        break;
                    case R.id.bottom_support:
                        selectedFragment = new SupportFragment(MainActivity.this);
                }
                if(selectedFragment != null){
                    currentFragment = selectedFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_frame_layout,selectedFragment).commit();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public void setDashboardFragment(){
        bottomNavigationView.setSelectedItemId(R.id.bottom_dashboard);
        currentFragment = dashboardFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame_layout, currentFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sync_schedule:
                if(isInternetConnectionAvailable()){
                    scheduleDataModel.syncSchedule();
                } else {
                    Toast.makeText(MainActivity.this,R.string.internet_connection_error,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.change_schedule:
                if(isInternetConnectionAvailable()){
                    availableSchedules = null;
                    showAvailableSchedules();
                } else {
                    Toast.makeText(MainActivity.this,R.string.internet_connection_error,Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private void showChangeScheduleDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final String[] availableSchedules = this.availableSchedules;
        if(availableSchedules != null){
            builder.setTitle("Select your Schedule");
            builder.setItems(availableSchedules, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sharedPreferences.edit().putString("selected_schedule",availableSchedules[i]).apply();
                    updateAndShowSchedule(true);
                }
            });
        }
        builder.show();
    }

    private void showAvailableSchedules(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        snackbar = Snackbar.make(findViewById(R.id.main_layout),R.string.syncing_schedule_text,Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(AVAILABLE_SCHEDULES_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "onFailure: failed to get available schedules");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String[] strings = responseString.trim().split("\\s+");
                availableSchedules = new String[strings.length-2];
                for(int i=1;i<strings.length-1;i++){
                    availableSchedules[i-1] = strings[i].trim();
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                snackbar.dismiss();
                showChangeScheduleDialog();
            }
        });
    }
    private boolean isInternetConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void updateAndShowSchedule(boolean isFirstTime){
        scheduleDataModel = new ScheduleDataModel(MainActivity.this,findViewById(R.id.main_layout));
        if(isFirstTime) {
            scheduleDataModel.syncSchedule();
        } else {
            scheduleDataModel.readFromInternalStorage();
        }
        dashboardFragment = new DashboardFragment(scheduleDataModel);
        setDashboardFragment();
    }
}
