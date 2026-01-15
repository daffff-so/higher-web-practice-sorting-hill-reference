package ru.yandex.practicum.sorting.model;

public class Wagon {
    private final String number;
    private final WagonType type;

    public Wagon(String number, WagonType type) {
        this.number = number;
        this.type = type;
    }

    public String number() { return number; }
    public WagonType type() { return type; }

    public String rawInfo() {
        return number + "/" + type.code();
    }
}

