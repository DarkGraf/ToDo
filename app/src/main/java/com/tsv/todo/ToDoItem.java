package com.tsv.todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoItem {

    private int id;
    private String task;
    private String description;
    private Date created;
    private Date shallBeMade;
    private ToDoItemCategory category;
    private boolean isDone;

    // region Аксессоры

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return  description;
    }

    public Date getCreated() {
        return created;
    }

    public Date getShallBeMade() {
        return shallBeMade;
    }

    public ToDoItemCategory getCategory() {
        return category;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public int getId() {
        return id;
    }

    // endregion

    // region Конструкторы

    public ToDoItem(String task) {
        this(task, new Date(System.currentTimeMillis()));
    }

    public ToDoItem(String task, Date created) {
        this(task, "", created, getDefaultShallBeMade(created), ToDoItemCategory.Common, false, 0);
    }

    public ToDoItem(String task, String description, Date created, Date shallBeMade, ToDoItemCategory category, boolean isDone, int id) {
        this.task = task;
        this.description = description;
        this.created = created;
        this.shallBeMade = shallBeMade;
        this.category = category;
        this.isDone = isDone;
        this.id = id;
    }

    // endregion

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(created);
        return task + " (" + dateString + ") " + (isDone ? "done" : "");
    }

    private static Date getDefaultShallBeMade(Date created) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(created);
        calendar.add(Calendar.DATE, 3);
        return calendar.getTime();
    }
}
