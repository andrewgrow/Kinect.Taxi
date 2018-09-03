package pro.kinect.taxi.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import pro.kinect.taxi.rest.RestManager;

public class TaxiJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        RestManager.getAllAutoCoordinates(null);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
