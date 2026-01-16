package ru.yandex.practicum.sorting.util;

import ru.yandex.practicum.sorting.model.*;

public class Parsing {

    public static Wagon parseWagon(String wagonInfo) {
        if (wagonInfo == null) {
            throw new ParsingException("wagonInfo is null. Expected format: 8digits/TYPE (e.g. 00000001/Г)");
        }

        String[] parts = wagonInfo.split("/");
        if (parts.length != 2) {
            throw new ParsingException("Invalid wagonInfo: '" + wagonInfo
                    + "'. Expected exactly 2 parts separated by '/': NUMBER/TYPE");
        }

        String number = parts[0].trim();
        String typeRaw = parts[1].trim();

        if (!number.matches("\\d{8}")) {
            throw new ParsingException("Invalid wagon number: '" + number + "'. Expected 8 digits");
        }

        WagonType type = WagonType.fromCode(typeRaw);
        return new Wagon(number, type);
    }

    public static Locomotive parseLocomotive(String loco) {
        if (loco == null) {
            throw new ParsingException("locomotive is null. Expected format: MODEL-CAPACITY (e.g. ЭВЛ-16)");
        }
        if (!loco.contains("-")) {
            throw new ParsingException("Invalid locomotive: '" + loco + "'. Expected '-' separator: MODEL-CAPACITY");
        }

        String[] parts = loco.split("-");
        if (parts.length != 2) {
            throw new ParsingException("Invalid locomotive: '" + loco + "'. Expected exactly 2 parts split by '-': MODEL-CAPACITY");
        }

        String model = parts[0].trim();
        String capStr = parts[1].trim();

        if (model.isEmpty()) {
            throw new ParsingException("Invalid locomotive: '" + loco + "'. Model part is empty");
        }

        final int cap;
        try {
            cap = Integer.parseInt(capStr);
        } catch (NumberFormatException e) {
            throw new ParsingException("Invalid locomotive capacity: '" + capStr + "' in '" + loco + "'. Expected integer number");
        }

        if (cap <= 0) {
            throw new ParsingException("Invalid locomotive capacity: " + cap + " in '" + loco + "'. Capacity must be > 0");
        }

        return new Locomotive(loco, cap);
    }
}

