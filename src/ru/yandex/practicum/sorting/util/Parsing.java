package ru.yandex.practicum.sorting.util;

import ru.yandex.practicum.sorting.model.*;

public class Parsing {

    public static Wagon parseWagon(String wagonInfo) {
        if (wagonInfo == null) return new Wagon("00000000", WagonType.UNKNOWN);
        String[] parts = wagonInfo.split("/");
        String number = parts[0].trim();
        String typeCode = parts[parts.length - 1].trim();
        return new Wagon(number, WagonType.fromCode(typeCode));
    }

    public static Locomotive parseLocomotive(String loco) {
        if (loco == null || !loco.contains("-")) return new Locomotive("UNKNOWN-0", 0);
        String[] parts = loco.split("-");
        int cap = 0;
        try { cap = Integer.parseInt(parts[parts.length - 1]); } catch (Exception ignored) {}
        return new Locomotive(loco, cap);
    }
}

