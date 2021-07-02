package com.cleanup.todoc.injection;

import android.content.Context;

import com.cleanup.todoc.database.SaveTaskManagerDataBase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static TaskDataRepository provideTaskDataSource(Context context) {
        SaveTaskManagerDataBase database = SaveTaskManagerDataBase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }

    public static ProjectDataRepository provideProjectDataSource(Context context) {
        SaveTaskManagerDataBase database = SaveTaskManagerDataBase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    public static Executor provideExecutor() { return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository     dataSourceTask = provideTaskDataSource(context);
        ProjectDataRepository  dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
