# Sorting Hill — Railway Sorting Station Simulator (Java)

This project implements a service that processes “human-style” text commands from a railway sorting station and models how trains are formed and dispatched.  
Events may arrive in a random order, so the service is designed to be resilient and always return a safe, correct response.

## What the service does

During a work shift the station can:

- Start and end a shift
- Prepare main paths (tracks) for trains
- Allocate a new train to a prepared path and assign it a sequential number (`0001`, `0002`, …)
- Accept locomotives and wagons
- Distribute wagons to trains by train number and train type
- Temporarily store wagons on a ring (buffer) path if a locomotive has not arrived yet
- Finish train formation, send trains, and free paths
- Produce shift metrics (sent trains, sent by type, max buffer fill, skipped wagons left in buffer)

Train type is defined by the wagons included:
- `Л` — passenger
- `Г` — cargo
- `О` — dangerous cargo
- `П` — empty / service type

## Input formats

The system receives commands as strings:

- `"начало работ"`
- `"окончание работ"`
- `"вагон на сортировку"`
- `"подача локомотива"`
- `"формируйте состав"`
- `"отправляйте поезд"`
- `"готовьте путь"`

Wagon format:

- `NNNNNNNN/TYPE`  
  Example: `42314281/Г`

Locomotive format:

- `MODEL-CAPACITY`  
  Example: `ЭВЛ-16` (capacity = 16 wagons)

## Project structure

- `ru.yandex.practicum.SortingHill`  
  Main simulator that generates random events and calls handlers.

- `ru.yandex.practicum.sorting.impl.SortingOperatorImpl`  
  Handler adapter. Delegates business logic to `StationService` and wraps calls in try/catch.

- `ru.yandex.practicum.sorting.core.StationService`  
  Core business logic and station state.

- `ru.yandex.practicum.sorting.impl.SortingReporterImpl`  
  Collects metrics and prints the shift report.

- `ru.yandex.practicum.sorting.model.*`  
  Domain model: `Wagon`, `Locomotive`, `Train`, `Path`, enums.

- `ru.yandex.practicum.sorting.util.Parsing`  
  Parsing helpers for wagon/locomotive formats.

- `ru.yandex.practicum.sorting.util.ParsingException`  
  Custom runtime exception for invalid input formats.

## How to run

### Run the simulator
Run the main class:

- `ru.yandex.practicum.SortingHill`

It will:
1) start a shift  
2) generate random events in random order  
3) process them until all wagons are handled  
4) end the shift and print a report

### Run tests
Tests are located in `test/` and can be run from the IDE or via Gradle/Maven (depending on the project setup).

Example (IDE):
- Right click `test` → Run All Tests

## Notes on robustness

- Parsing uses strict validation and throws `ParsingException` with clear messages.
- The operator handler catches exceptions to keep the simulator running and prevent crashes.
- The logic does not depend on a fixed order of events (locomotives may arrive before or after wagons).

---
Author: Darya Sozinova
