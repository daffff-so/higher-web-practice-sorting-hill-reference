package ru.yandex.practicum.sorting.core;

import ru.yandex.practicum.sorting.model.*;
import ru.yandex.practicum.sorting.util.Parsing;

import java.util.*;

public class StationService {

    private final int numberOfPaths;

    private final Map<Integer, Path> paths = new HashMap<>();
    private final Map<String, Train> trains = new HashMap<>();
    private final Deque<Wagon> ringBuffer = new ArrayDeque<>();
    private long trainIndex = 0;
    private boolean shiftActive = false;

    private final EnumMap<WagonType, Integer> skippedByType = new EnumMap<>(WagonType.class);
    private int sentTrains = 0;
    private final EnumMap<TrainType, Integer> sentByType = new EnumMap<>(TrainType.class);
    private int maxRingSize = 0;

    public StationService(int numberOfPaths) {
        this.numberOfPaths = numberOfPaths;
        for (int i = 1; i <= numberOfPaths; i++) paths.put(i, new Path(i));
        resetMetrics();
    }

    public void startShift() {
        shiftActive = true;
        trainIndex = 0;
        trains.clear();
        ringBuffer.clear();
        for (Path p : paths.values()) p.release();
        resetMetrics();
    }

    public void endShift() {
        shiftActive = false;
        for (Wagon w : ringBuffer) {
            skippedByType.put(w.type(), skippedByType.get(w.type()) + 1);
        }
    }

    public Integer preparePath() {
        if (!shiftActive) return null;
        for (int p = 1; p <= numberOfPaths; p++) {
            Path path = paths.get(p);
            if (path != null && path.isFree()) {
                path.prepare();
                return p;
            }
        }
        return null;
    }

    public Map<Integer, String> allocatePathForTrain() {
        if (!shiftActive) return Collections.emptyMap();

        for (int p = 1; p <= numberOfPaths; p++) {
            Path path = paths.get(p);
            if (path != null && path.isPreparedButEmpty()) {
                String id4 = String.format("%04d", ++trainIndex);
                Train train = new Train(id4);
                trains.put(id4, train);
                path.assignTrain(train);
                return Collections.singletonMap(p, id4);
            }
        }
        return Collections.emptyMap();
    }

    public String handleLocomotive(String locoRaw) {
        if (!shiftActive) return null;

        Locomotive loco = Parsing.parseLocomotive(locoRaw);

        for (int p = 1; p <= numberOfPaths; p++) {
            Path path = paths.get(p);
            if (path == null || path.train() == null) continue;
            Train train = path.train();
            if (!train.hasLocomotive() && train.wagons().isEmpty()) {
                train.attachLocomotive(loco);
                drainRingIntoTrains();
                return train.fullNumber();
            }
        }
        return null;
    }

    public String handleWagon(String wagonRaw) {
        Wagon wagon = Parsing.parseWagon(wagonRaw);

        if (!shiftActive) {
            ringBuffer.addLast(wagon);
            maxRingSize = Math.max(maxRingSize, ringBuffer.size());
            return null;
        }

        Train candidate = findTrainForWagon(wagon);
        if (candidate == null) {
            ringBuffer.addLast(wagon);
            maxRingSize = Math.max(maxRingSize, ringBuffer.size());
            return null;
        }

        candidate.addWagon(wagon);
        drainRingIntoTrains();
        return candidate.fullNumber();
    }

    public String sendTrain() {
        if (!shiftActive) return null;

        boolean noMoreIncoming = ringBuffer.isEmpty();

        for (int p = 1; p <= numberOfPaths; p++) {
            Path path = paths.get(p);
            if (path == null || path.train() == null) continue;

            Train train = path.train();
            if (train.isReadyToSend(noMoreIncoming)) {
                String full = train.fullNumber();
                sentTrains++;
                TrainType t = train.type() == TrainType.UNKNOWN ? TrainType.P : train.type();
                sentByType.put(t, sentByType.get(t) + 1);

                trains.remove(train.id4());
                path.release();
                return full;
            }
        }
        return null;
    }

    public int sentTrains() { return sentTrains; }
    public int maxRingSize() { return maxRingSize; }
    public Map<WagonType, Integer> skippedByType() { return Collections.unmodifiableMap(skippedByType); }
    public Map<TrainType, Integer> sentByType() { return Collections.unmodifiableMap(sentByType); }

    private void resetMetrics() {
        skippedByType.clear();
        for (WagonType wt : WagonType.values()) skippedByType.put(wt, 0);
        sentByType.clear();
        for (TrainType tt : TrainType.values()) sentByType.put(tt, 0);
        sentTrains = 0;
        maxRingSize = 0;
    }

    private Train findTrainForWagon(Wagon wagon) {
        for (int p = 1; p <= numberOfPaths; p++) {
            Path path = paths.get(p);
            if (path == null || path.train() == null) continue;
            Train train = path.train();
            if (train.matchesWagon(wagon)) return train;
        }
        return null;
    }

    private void drainRingIntoTrains() {
        int safety = ringBuffer.size();
        while (!ringBuffer.isEmpty() && safety-- > 0) {
            Wagon w = ringBuffer.pollFirst();
            if (w == null) break;
            Train t = findTrainForWagon(w);
            if (t == null) {
                ringBuffer.addLast(w);
                continue;
            }
            t.addWagon(w);
        }
    }
}
