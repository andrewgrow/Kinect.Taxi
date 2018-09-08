package pro.kinect.taxi.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.concurrent.TimeUnit;

import pro.kinect.taxi.App;
import pro.kinect.taxi.db.AppPrefs;
import pro.kinect.taxi.rest.RestManager;

public class TaxiJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        AppPrefs appPrefs = App.getInstance().getAppPrefs();
        long nowTime = System.currentTimeMillis();
        long lastTime = appPrefs.getLastTimeGettingAuto();
        long delay = TimeUnit.MINUTES.toMillis(1);
        // только раз в минуту можно обновить данные (чтобы не спамить сервер)
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
