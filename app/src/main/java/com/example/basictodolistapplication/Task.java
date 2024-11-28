package com.example.basictodolistapplication;

import androidx.annotation.NonNull;

public class Task {
    private int id;
    private String name;
    private boolean isCompleted;
    private String description;
    private String deadline; // You can use Date or long type
    private int duration; // For example, duration in minutes

    public Task(String name, boolean isCompleted) {
        this.name = name;
        this.isCompleted = isCompleted;
    }

    public Task(int id, String name, boolean isCompleted) {
        this(id, name, isCompleted, null, null, 0);
    }

    public Task(int id, String name, boolean isCompleted, String description, String deadline, int duration) {
        this.id = id;
        this.name = name;
        this.isCompleted = isCompleted;
        this.description = description;
        this.deadline = deadline;
        this.duration = duration;
    }

    public Task(String taskName, boolean b, String description, String deadline, int duration) {
    }

    // Getters and setters for all attributes
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NonNull
    @Override
    public String toString() {
        return name
                + (description != null && !description.isEmpty() ? "\nDescription: " + description : "")
                + (deadline != null && !deadline.isEmpty() ? "\nDeadline: " + deadline : "")
                + (duration > 0 ? "\nDuration: " + duration + " minutes" : "");
    }
}