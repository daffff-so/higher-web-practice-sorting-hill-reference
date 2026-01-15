package ru.yandex.practicum.sorting.impl;

import ru.yandex.practicum.SortingHill;
import ru.yandex.practicum.sorting.SortingHandler;
import ru.yandex.practicum.sorting.core.StationService;
import ru.yandex.practicum.sorting.model.TrainType;
import ru.yandex.practicum.sorting.model.WagonType;

import java.util.Collections;
import java.util.Map;

public class SortingReporterImpl implements SortingHandler {

    private StationService service;

    @Override
    public void init(SortingHill sortingHill) {
        this.service = new StationService(sortingHill.getNumberOfPaths());
    }

    @Override
    public String handleWagon(String wagonInfo) {
        try {
            return service.handleWagon(wagonInfo);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String handleLocomotive(String locomotive) {
        try {
            return service.handleLocomotive(locomotive);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer preparePath() {
        try {
            return service.preparePath();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<Integer, String> allocatePathForTrain() {
        try {
            return service.allocatePathForTrain();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    @Override
    public String sendTrain() {
        try {
            return service.sendTrain();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void startShift() {
        try {
            service.startShift();
        } catch (Exception ignored) {}
    }

    @Override
    public void endShift() {
        try {
            service.endShift();
            printReport();
        } catch (Exception ignored) {}
    }

    private void printReport() {
        System.out.println("=== ОТЧЁТ ЗА СМЕНУ ===");
        System.out.println("Отправлено поездов: " + service.sentTrains());

        System.out.println("Отправлено по типам:");
        for (TrainType t : service.sentByType().keySet()) {
            if (t == TrainType.UNKNOWN) continue;
            System.out.printf("  %s: %d%n", t.code(), service.sentByType().get(t));
        }

        System.out.println("Макс. заполнение буферного пути: " + service.maxRingSize());

        System.out.println("Пропущенные вагоны (остались в буфере к концу смены):");
        for (WagonType wt : service.skippedByType().keySet()) {
            if (wt == WagonType.UNKNOWN) continue;
            System.out.printf("  %s: %d%n", wt.code(), service.skippedByType().get(wt));
        }
        System.out.println("======================");
    }
}