package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

/** the different CRUD actions */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    /** Create a new task */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTask(Task task);

    /** Refresh the table*/
    @Update
    int updateTask(Task task);

    /** Delete a task*/
    @Query("DELETE FROM Task WHERE id = :taskId")
    int deleteTask(long taskId);
}
