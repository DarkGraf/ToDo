package com.tsv.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ToDoItemParcelable implements Parcelable {

    public static final String TAG_PARCELABLE_NAME = "ToDoItemParcelable";

    private ToDoItem toDoItem;

    public ToDoItem getToDoItem() {
        return toDoItem;
    }

    public ToDoItemParcelable(ToDoItem toDoItem) {
        this.toDoItem = toDoItem;
    }

    // region Стандартная реализация

    protected ToDoItemParcelable(Parcel in) {
        int id = in.readInt();
        String task = in.readString();
        String description = in.readString();
        Date created = new Date(in.readLong());
        Date shallBeMade = new Date(in.readLong());
        ToDoItemCategory category = ToDoItemCategory.values()[in.readInt()];
        boolean isDone = in.readInt() == 1;

        toDoItem = new ToDoItem(task, description, created, shallBeMade, category, isDone, id);
    }

    public static final Creator<ToDoItemParcelable> CREATOR = new Creator<ToDoItemParcelable>() {
        @Override
        public ToDoItemParcelable createFromParcel(Parcel in) {
            return new ToDoItemParcelable(in);
        }

        @Override
        public ToDoItemParcelable[] newArray(int size) {
            return new ToDoItemParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(toDoItem.getId());
        dest.writeString(toDoItem.getTask());
        dest.writeString(toDoItem.getDescription());
        dest.writeLong(toDoItem.getCreated().getTime());
        dest.writeLong(toDoItem.getShallBeMade().getTime());
        dest.writeInt(toDoItem.getCategory().ordinal());
        dest.writeInt(toDoItem.getIsDone() ? 1 : 0);
    }

    // endregion
}
