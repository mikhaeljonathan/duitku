package com.example.duitku.firebase;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class FirebaseJobService extends JobService {
    //panggil ini dari firebaseScheduler
    private Boolean jobCancelled = false;

    public void backupToFirebase(JobParameters params) {
        FirebaseWriter writer = new FirebaseWriter(getApplicationContext());

        if (jobCancelled) return;

        writer.writeAll();
        jobFinished(params, false);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        backupToFirebase(params);
        return true; // true to keep device awake
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true; // true to tell the system to reschedule
    }
}