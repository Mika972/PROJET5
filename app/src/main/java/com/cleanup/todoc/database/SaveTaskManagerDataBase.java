package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

/** binds all classes and interfaces */
@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class SaveTaskManagerDataBase extends RoomDatabase {
    // --- Singleton ---
    private static volatile SaveTaskManagerDataBase INSTANCE;

    // --- DAO ---
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    // --- Instance ---
    public static SaveTaskManagerDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveTaskManagerDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveTaskManagerDataBase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /** Projects for the spinner */
    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1L);
                contentValues.put("name", "Projet Tartampion");
                contentValues.put("color", 0xFFEADAD1);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("id",2L);
                contentValues2.put("name","Projet Lucidia");
                contentValues2.put("color",0xFFB4CDBA);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues2);

                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("id",3L);
                contentValues3.put("name","Projet Circus");
                contentValues3.put("color",0xFFA3CED2);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues3);
            }
        };
    }

}