package com.example.basictodolistapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private EditText editTextDescription;
    private EditText editTextDeadline;
    private EditText editTextDuration;
    private Button buttonAdd;
    private ListView listViewTasks;
    private TaskDbHelper dbHelper;
    private List<Task> taskList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        editTextDuration = findViewById(R.id.editTextDuration);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        dbHelper = new TaskDbHelper(this);
        taskList = dbHelper.getAllTasks();

        adapter = new TaskAdapter(this, taskList);
        listViewTasks.setAdapter(adapter);
        registerForContextMenu(listViewTasks);

        // Deadline picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = dateFormat.format(calendar.getTime());
                editTextDeadline.setText(date);
            }
        };

        editTextDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateSetListener, year, month, day).show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editTextTask.getText().toString();
                String description = editTextDescription.getText().toString();
                String deadline = editTextDeadline.getText().toString();

                int duration = 0;
                try {
                    duration = Integer.parseInt(editTextDuration.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid duration", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed if duration is invalid
                }

                if (!taskName.isEmpty()) {
                    Task newTask = new Task(0, taskName, false, description, deadline, duration); // Use the full constructor
                    dbHelper.addTask(newTask);
                    taskList.add(newTask);
                    adapter.notifyDataSetChanged();

                    // Clear input fields
                    editTextTask.setText("");
                    editTextDescription.setText("");
                    editTextDeadline.setText("");
                    editTextDuration.setText("");
                }
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = taskList.get(position);
                selectedTask.setCompleted(!selectedTask.isCompleted());
                dbHelper.updateTask(selectedTask);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Task selectedTask = taskList.get(position);

        if (item.getItemId() == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.deleteTask(selectedTask);
                            taskList.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}