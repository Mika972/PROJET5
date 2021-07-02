package com.cleanup.todoc.todolist;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<List<Project>> currentProject;

    public TaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataSource = taskDataRepository;
        this.projectDataSource = projectDataRepository;
        this.executor = executor;
    }

    public void init() {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject();
    }

    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<List<Project>> getProject() { return this.currentProject; }

    // -------------
    // FOR TASK
    // -------------

    public LiveData<List<Task>> getTasks() { return taskDataSource.getTasks(); }

    public void createTask(Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> {
            taskDataSource.deleteTask(taskId);
        });
    }

}
