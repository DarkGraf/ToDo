package com.tsv.todo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NewItemFragment.OnNewItemAddedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final int MENU_IS_DONE = 0;
    private final int MENU_DELETE = 2;

    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoItems = new ArrayList<>();
        aa = new ToDoItemAdapter(this, R.layout.todolist_item, todoItems);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment)fm.findFragmentById(R.id.ToDoListFragment);
        toDoListFragment.setListAdapter(aa);

        getLoaderManager().initLoader(0, null, this);
        registerForContextMenu(toDoListFragment.getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Выбор действия");
        menu.add(Menu.NONE, MENU_IS_DONE, Menu.NONE, "Выполнить");
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        ToDoItem item = aa.getItem(info.position);

        switch (menuItem.getItemId())
        {
            case MENU_IS_DONE:
                setToDoItemIsDone(item);
                return true;
            case MENU_DELETE:
                deleteToDoItem(item);
                return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    private void setToDoItemIsDone(final ToDoItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("ToDo")
                .setMessage("Задание \"" + item.getTask() + "\" выполено?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(ToDoContentProvider.CONTENT_URI, item.getId());
                        ContentValues values = new ContentValues();
                        values.put(ToDoContentProvider.KEY_IS_DONE, 1);
                        ContentResolver cr = getContentResolver();
                        cr.update(uri, values, null, null);
                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteToDoItem(final ToDoItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("ToDo")
                .setMessage("Задание \"" + item.getTask() + "\" удалить?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(ToDoContentProvider.CONTENT_URI, item.getId());
                        ContentResolver cr = getContentResolver();
                        cr.delete(uri, null, null);
                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // region NewItemFragment.OnNewItemAddedListener

    @Override
    public void onNewItemAdded(ToDoItem newItem) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, newItem.getTask());
        values.put(ToDoContentProvider.KEY_DESCRIPTION, newItem.getDescription());
        values.put(ToDoContentProvider.KEY_CREATED_DATE, newItem.getCreated().getTime());
        values.put(ToDoContentProvider.KEY_SHALL_BE_MADE_DATE, newItem.getShallBeMade().getTime());
        values.put(ToDoContentProvider.KEY_CATEGORY, newItem.getCategory().ordinal());
        values.put(ToDoContentProvider.KEY_IS_DONE, newItem.getIsDone() ? 1 : 0);
        cr.insert(ToDoContentProvider.CONTENT_URI, values);
        getLoaderManager().restartLoader(0, null, this);
    }

    // endregion

    // region LoaderManager.LoaderCallbacks

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this, ToDoContentProvider.CONTENT_URI,
                null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyIdIndex = cursor.getColumnIndex(ToDoContentProvider.KEY_ID);
        int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);
        int keyDescriptionIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_DESCRIPTION);
        int keyCreatedIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_CREATED_DATE);
        int keyShallBeMadeIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_SHALL_BE_MADE_DATE);
        int keyCategoryIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_CATEGORY);
        int keyIsDoneIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_IS_DONE);

        todoItems.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(keyIdIndex);
            String task = cursor.getString(keyTaskIndex);
            String description = cursor.getString(keyDescriptionIndex);
            Date created = new Date(cursor.getLong(keyCreatedIndex));
            Date shallBeMade = new Date(cursor.getLong(keyShallBeMadeIndex));
            ToDoItemCategory category = ToDoItemCategory.values()[cursor.getInt(keyCategoryIndex)];
            boolean isDone = cursor.getInt(keyIsDoneIndex) == 1;

            ToDoItem newItem = new ToDoItem(task, description, created, shallBeMade, category, isDone, id);
            todoItems.add(newItem);
        }

        aa.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // endregion
}
