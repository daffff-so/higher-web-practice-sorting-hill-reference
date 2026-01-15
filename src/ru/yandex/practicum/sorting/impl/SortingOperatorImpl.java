package ru.yandex.practicum.sorting.impl;

import ru.yandex.practicum.SortingHill;
import ru.yandex.practicum.sorting.SortingHandler;
import ru.yandex.practicum.sorting.core.StationService;

import java.util.Collections;
import java.util.Map;

public class SortingOperatorImpl implements SortingHandler {

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
            service.preparePath();
            service.allocatePathForTrain();
        } catch (Exception ignored) {}
    }

    @Override
    public void endShift() {
        try {
            service.endShift();
        } catch (Exception ignored) {}
    }
}
