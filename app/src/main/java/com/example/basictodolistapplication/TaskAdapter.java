package com.example.basictodolistapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
        TextView textViewDeadline = convertView.findViewById(R.id.textViewDeadline);
        TextView textViewDuration = convertView.findViewById(R.id.textViewDuration);

        textViewName.setText(task.getName());
        textViewDescription.setText("Description: " + task.getDescription());
        textViewDeadline.setText("Deadline: " + task.getDeadline());
        textViewDuration.setText("Duration: " + task.getDuration() + " minutes");

        return convertView;
    }
}