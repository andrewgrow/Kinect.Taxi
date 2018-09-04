package pro.kinect.taxi.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import pro.kinect.taxi.App;
import pro.kinect.taxi.db.AppPrefs;
import pro.kinect.taxi.rest.RestManager;

public class TaxiJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        AppPrefs appPrefs = App.getInstance().getAppPrefs();
        long nowTime = System.currentTimeMillis();
        long lastTime = appPrefs.getLastTimeGettingAuto();
        long delay = App.jobPeriod / 2;
        if (lastTime + delay < nowTime) {
            appPrefs.setLastTimeGettingAuto(nowTime);
            RestManager.getAllAutoCoordinates(null);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
