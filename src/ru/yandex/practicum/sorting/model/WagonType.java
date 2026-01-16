package ru.yandex.practicum.sorting.model;

public enum WagonType {
    P("П"), G("Г"), L("Л"), O("О"), UNKNOWN("?");

    private final String code;
    WagonType(String code) { this.code = code; }
    public String code() { return code; }

    public static WagonType fromCode(String s) {
        if (s == null) {
            throw new ru.yandex.practicum.sorting.util.ParsingException(
                    "Wagon type is null. Expected one of: П, Г, Л, О"
            );
        }

        return switch (s.trim()) {
            case "П" -> P;
            case "Г" -> G;
            case "Л" -> L;
            case "О" -> O;
            default -> throw new ru.yandex.practicum.sorting.util.ParsingException(
                    "Invalid wagon type: '" + s + "'. Expected one of: П, Г, Л, О"
            );
        };
    }
}

