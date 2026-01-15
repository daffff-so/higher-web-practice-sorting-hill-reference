package ru.yandex.practicum.sorting.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Train {
    private final String id4;
    private TrainType type = TrainType.UNKNOWN;
    private Locomotive locomotive;
    private final Deque<Wagon> wagons = new ArrayDeque<>();

    public Train(String id4) { this.id4 = id4; }

    public String id4() { return id4; }
    public TrainType type() { return type; }
    public Locomotive locomotive() { return locomotive; }
    public Deque<Wagon> wagons() { return wagons; }

    public String fullNumber() {
        TrainType printable = (type == TrainType.UNKNOWN) ? TrainType.P : type;
        return id4 + printable.code();
    }

    public boolean hasLocomotive() { return locomotive != null; }

    public void attachLocomotive(Locomotive loco) {
        this.locomotive = loco;
    }

    public boolean canAcceptMoreWagons() {
        return locomotive != null && wagons.size() < locomotive.capacity();
    }

    public void addWagon(Wagon wagon) {
        if (type == TrainType.UNKNOWN && wagon.type() != WagonType.P) {
            type = TrainType.fromWagonType(wagon.type());
        }
        wagons.addLast(wagon);
    }

    public boolean matchesWagon(Wagon wagon) {
        if (!hasLocomotive() || !canAcceptMoreWagons()) return false;

        WagonType wt = wagon.type();
        if (type == TrainType.L) return wt == WagonType.L;
        if (type == TrainType.G) return wt == WagonType.G || wt == WagonType.P;
        if (type == TrainType.O) return wt == WagonType.O || wt == WagonType.P;

        if (wt == WagonType.L) return wagons.isEmpty();
        return true;
    }

    public boolean isReadyToSend(boolean noMoreIncomingWagons) {
        if (!hasLocomotive()) return false;
        if (wagons.isEmpty()) return false;
        if (wagons.size() == locomotive.capacity()) return true;
        return noMoreIncomingWagons;
    }
}
