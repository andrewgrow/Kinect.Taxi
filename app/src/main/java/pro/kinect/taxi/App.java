package pro.kinect.taxi;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import pro.kinect.taxi.db.AppDatabase;
import pro.kinect.taxi.job.TaxiJobService;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static App instance;
    private AppDatabase database;

    private static final int jobId = 1;
    private static final long jobPeriod = 1000 * 60 * 5; // 5 minutes

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String db = "kinect_taxi.db";
        database = Room.databaseBuilder(this, AppDatabase.class, db).build();

        planeJob();
    }

    private void planeJob() {
        ComponentName jobService = new ComponentName(getApplicationContext(), TaxiJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, jobService);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        builder.setPeriodic(jobPeriod);
        builder.setPersisted(true);

        JobScheduler jobScheduler =
                (JobScheduler) getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
            Log.d(TAG, "Задача запланирована успешно!");
        } else {
            Log.e(TAG, "jobScheduler is null!");
        }

    }

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return getInstance().database;
    }
}
