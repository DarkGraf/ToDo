package com.tsv.todo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.Date;

public class ToDoItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText taskView;
    EditText descriptionView;
    EditText createdView;
    EditText shallBeMadeView;
    Spinner categoryView;
    CheckBox isDoneView;

    DateWatcher createdMask;
    DateWatcher shallBeMadeMask;

    ToDoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);

        Intent intent = getIntent();
        ToDoItemParcelable itemParcelable = intent.getParcelableExtra(ToDoItemParcelable.TAG_PARCELABLE_NAME);
        item = itemParcelable.getToDoItem();

        taskView = findViewById(R.id.task);
        descriptionView = findViewById(R.id.description);
        createdView = findViewById(R.id.created);
        shallBeMadeView = findViewById(R.id.shallBeMade);
        categoryView = findViewById(R.id.category);
        isDoneView = findViewById(R.id.isDone);

        Button saveButton = findViewById(R.id.save);

        taskView.setText(item.getTask());
        descriptionView.setText(item.getDescription());
        createdView.setText(DateViewFormatter.format(item.getCreated()));
        shallBeMadeView.setText(DateViewFormatter.format(item.getShallBeMade()));
        categoryView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ToDoItemCategory.values()));
        isDoneView.setChecked(item.getIsDone());

        saveButton.setOnClickListener(this);

        createdMask = new DateWatcher(createdView);
        shallBeMadeMask = new DateWatcher(shallBeMadeView);
    }

    @Override
    public void onClick(View v) {
        String task = taskView.getText().toString();
        String description = descriptionView.getText().toString();
        Date created = parse(createdView.getText().toString());
        Date shallBeMade = parse(shallBeMadeView.getText().toString());
        int categoryInt = categoryView.getSelectedItemPosition();
        boolean isDone = isDoneView.isChecked();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, task);
        values.put(ToDoContentProvider.KEY_DESCRIPTION, description);
        values.put(ToDoContentProvider.KEY_CREATED_DATE, created.getTime());
        values.put(ToDoContentProvider.KEY_SHALL_BE_MADE_DATE, shallBeMade.getTime());
        values.put(ToDoContentProvider.KEY_CATEGORY, categoryInt);
        values.put(ToDoContentProvider.KEY_IS_DONE, isDone ? 1 : 0);

        Uri uri = ContentUris.withAppendedId(ToDoContentProvider.CONTENT_URI, item.getId());
        cr.update(uri, values, null, null);

        setResult(RESULT_OK);
        finish();
    }

    private Date parse(String text) {
        Date date;
        try {
            date = DateViewFormatter.parse(text);
        } catch (ParseException e) {
            date = new Date(System.currentTimeMillis());
            e.printStackTrace();
        }
        return date;
    }
}
