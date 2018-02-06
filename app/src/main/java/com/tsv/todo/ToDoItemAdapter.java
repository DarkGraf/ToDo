package com.tsv.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

// TODO: Добавить паттерн ViewHolder.
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    int resource;

    public ToDoItemAdapter(Context context, int resource, List<ToDoItem> items) {
        super(context, resource, items);

        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView;

        ToDoItem item = getItem(position);

        String task = item.getTask();
        String description = item.getDescription();
        String created = DateViewFormatter.format(item.getCreated());
        String shallBeMade = DateViewFormatter.format(item.getShallBeMade());
        String category = item.getCategory().toString();
        boolean isDone = item.getIsDone();

        if (convertView == null) {
            todoView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li =(LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, todoView, true);
        }
        else {
            todoView = (LinearLayout)convertView;
        }

        ToDoListItemView taskView = todoView.findViewById(R.id.task);
        TextView descriptionView = todoView.findViewById(R.id.description);
        TextView createdView = todoView.findViewById(R.id.created);
        TextView shallBeMadeView = todoView.findViewById(R.id.shallBeMade);
        TextView categoryView = todoView.findViewById(R.id.category);

        taskView.setText(task);
        taskView.setIsDone(isDone);
        descriptionView.setText(description);
        createdView.setText(created);
        shallBeMadeView.setText(shallBeMade);
        categoryView.setText(category);

        return todoView;
    }
}