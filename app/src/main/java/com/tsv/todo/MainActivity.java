package com.tsv.todo;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NewItemFragment.OnNewItemAddedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment)fm.findFragmentById(R.id.ToDoListFragment);

        todoItems = new ArrayList<>();
        aa = new ToDoItemAdapter(this, R.layout.todolist_item, todoItems);

        toDoListFragment.setListAdapter(aa);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onNewItemAdded(String newItem) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, newItem);
        cr.insert(ToDoContentProvider.CONTENT_URI, values);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this, ToDoContentProvider.CONTENT_URI,
                null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);

        todoItems.clear();
        while (cursor.moveToNext()) {
            ToDoItem newItem = new ToDoItem(cursor.getString(keyTaskIndex));
            todoItems.add(newItem);
        }

        aa.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
