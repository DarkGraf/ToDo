package com.tsv.todo;

public enum ToDoItemCategory {
    Common("Общее"),
    Finance("Финансы"),
    Building("Строительство"),
    Working("Работа");

    private final String friendlyName;

    ToDoItemCategory(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
