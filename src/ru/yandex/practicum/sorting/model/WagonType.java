package ru.yandex.practicum.sorting.model;

public enum WagonType {
    P("П"), G("Г"), L("Л"), O("О"), UNKNOWN("?");

    private final String code;
    WagonType(String code) { this.code = code; }
    public String code() { return code; }

    public static WagonType fromCode(String code) {
        for (WagonType t : values()) if (t.code.equals(code)) return t;
        return UNKNOWN;
    }
}

