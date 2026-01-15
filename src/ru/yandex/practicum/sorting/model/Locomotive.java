package ru.yandex.practicum.sorting.model;

public class Locomotive {
    private final String model;
    private final int capacity;

    public Locomotive(String model, int capacity) {
        this.model = model;
        this.capacity = capacity;
    }

    public String model() { return model; }
    public int capacity() { return capacity; }
}
