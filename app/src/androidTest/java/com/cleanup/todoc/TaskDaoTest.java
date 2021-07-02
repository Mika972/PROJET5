package com.cleanup.todoc;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.SaveTaskManagerDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {
    // FOR DATA
    private SaveTaskManagerDataBase database;
    // The timestamp when the task has been created
    private long creationTimestamp;

    // DATA SET FOR TEST
    private static long PROJECT_ID  = 1L;
    private static long PROJECT_ID2 = 2L;
    private static long PROJECT_ID3 = 3L;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Ecarlate", 0xe5F010);
    private static Task NEW_TASK_RANDOMLY = new Task(1, PROJECT_ID, "Un tour en Bugatti", System.currentTimeMillis()/1000);
    private static Task NEW_TASK_TREK = new Task(2, PROJECT_ID, "Une superbe randonnée", System.currentTimeMillis()/1000);
    private static Task NEW_TASK_FINN = new Task(3, PROJECT_ID, "FN-2187", System.currentTimeMillis()/1000);
    private static Task NEW_TASK_POE  = new Task(4, PROJECT_ID, "BB-8", System.currentTimeMillis()/1000);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                SaveTaskManagerDataBase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetProjects() throws InterruptedException {
        // BEFORE : Adding a new project
        this.database.projectDao().createProject(PROJECT_DEMO);

        // TEST
        List<Project> project = LiveDataTestUtil.getValue(this.database.projectDao().getProject());
        assertTrue(project.get(0).getName().equals(PROJECT_DEMO.getName()) && project.get(0).getId() == PROJECT_ID);
    }


    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks( ));
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
        // BEFORE : Adding demo tasks

        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_RANDOMLY);
        this.database.taskDao().insertTask(NEW_TASK_TREK);
        this.database.taskDao().insertTask(NEW_TASK_FINN);
        this.database.taskDao().insertTask(NEW_TASK_POE);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.size() == 4);
    }

    @Test
    public void insertAndUpdateTask() throws InterruptedException {
        // BEFORE : Adding demo project & demo task Next, update item added & re-save it
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_RANDOMLY);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().updateTask(taskAdded);
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // BEFORE : Adding demo task. Next, get the task added and delete it
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK_RANDOMLY);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getId());


        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }


}