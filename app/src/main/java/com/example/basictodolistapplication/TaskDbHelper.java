package com.example.basictodolistapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log; // Import Log for debugging

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 2; // Increment version due to schema change
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IS_COMPLETED = "isCompleted";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DEADLINE = "deadline";
    private static final String COLUMN_DURATION = "duration";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_IS_COMPLETED + " INTEGER DEFAULT 0, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DEADLINE + " TEXT, "
                + COLUMN_DURATION + " INTEGER);";
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_DURATION, task.getDuration());

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();

        if (newRowId == -1) {
            Log.e("TaskDbHelper", "Error inserting task into database");
        } else {
            Log.d("TaskDbHelper", "Task inserted with ID: " + newRowId);
        }
    }


    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        COLUMN_ID,
                        COLUMN_NAME,
                        COLUMN_IS_COMPLETED,
                        COLUMN_DESCRIPTION,
                        COLUMN_DEADLINE,
                        COLUMN_DURATION
                },
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2) == 1,
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5)
        );
        cursor.close();
        db.close();
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5)
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Log the retrieved tasks for debugging
        Log.d("TaskDbHelper", "Retrieved tasks: " + taskList.toString());

        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_DURATION, task.getDuration());
        return db.update(TABLE_NAME, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }
}