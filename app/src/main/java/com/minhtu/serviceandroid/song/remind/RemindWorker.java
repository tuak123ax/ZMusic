package com.minhtu.serviceandroid.song.remind;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RemindWorker extends Worker {
    Context context;
    public RemindWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent(context, RemindService.class);
        context.startService(intent);
        return Result.success();
    }
}
