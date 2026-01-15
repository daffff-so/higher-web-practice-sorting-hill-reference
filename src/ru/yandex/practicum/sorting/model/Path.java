package ru.yandex.practicum.sorting.model;

public class Path {
    private final int number;
    private boolean prepared;
    private Train train;

    public Path(int number) { this.number = number; }

    public int number() { return number; }
    public boolean prepared() { return prepared; }
    public Train train() { return train; }

    public boolean isFree() { return !prepared && train == null; }
    public boolean isPreparedButEmpty() { return prepared && train == null; }

    public void prepare() { prepared = true; }
    public void assignTrain(Train t) { this.train = t; this.prepared = true; }
    public void release() { this.train = null; this.prepared = false; }
}
