package ru.yandex.practicum.sorting.model;

public enum TrainType {
    P("П"), G("Г"), L("Л"), O("О"), UNKNOWN("");

    private final String code;
    TrainType(String code) { this.code = code; }
    public String code() { return code; }

    public static TrainType fromWagonType(WagonType w) {
        return switch (w) {
            case G -> G;
            case L -> L;
            case O -> O;
            case P -> P;
            default -> UNKNOWN;
        };
    }
}

