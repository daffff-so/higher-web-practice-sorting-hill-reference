package ru.yandex.practicum.sorting.core;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sorting.model.TrainType;
import ru.yandex.practicum.sorting.model.WagonType;

import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest {

    @Test
    void wagonGoesToRingIfNoLocomotive() {
        StationService s = new StationService(2);
        s.startShift();

        s.preparePath();
        s.allocatePathForTrain();

        assertNull(s.handleWagon("00000001/Г"));
        assertTrue(s.maxRingSize() >= 1);
    }

    @Test
    void locomotiveThenWagonSetsTrainType() {
        StationService s = new StationService(2);
        s.startShift();

        s.preparePath();
        s.allocatePathForTrain();

        assertNotNull(s.handleLocomotive("ЭВЛ-16"));

        String train = s.handleWagon("00000001/О");
        assertNotNull(train);
        assertTrue(train.endsWith("О"));
    }

    @Test
    void passengerWagonDoesNotGoIntoCargoTrain() {
        StationService s = new StationService(1);
        s.startShift();

        s.preparePath();
        s.allocatePathForTrain();
        s.handleLocomotive("ЭВЛ-16");

        String cargoTrain = s.handleWagon("00000001/Г");
        assertNotNull(cargoTrain);
        assertTrue(cargoTrain.endsWith("Г"));

        assertNull(s.handleWagon("00000002/Л"));
    }

    @Test
    void sendTrainWhenFull() {
        StationService s = new StationService(1);
        s.startShift();

        s.preparePath();
        s.allocatePathForTrain();
        s.handleLocomotive("ЭВЛ-16");

        for (int i = 1; i <= 16; i++) {
            assertNotNull(s.handleWagon(String.format("%08d/Г", i)));
        }

        String sent = s.sendTrain();
        assertNotNull(sent);

        assertEquals(1, s.sentTrains());
        assertEquals(1, s.sentByType().getOrDefault(TrainType.G, 0));
    }

    @Test
    void endShiftCountsSkippedWagonsFromRing() {
        StationService s = new StationService(1);
        s.startShift();

        s.handleWagon("00000001/О");

        s.endShift();

        assertTrue(s.skippedByType().getOrDefault(WagonType.O, 0) >= 1);
    }
}

