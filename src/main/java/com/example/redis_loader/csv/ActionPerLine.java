package com.example.redis_loader.csv;

@FunctionalInterface
public interface ActionPerLine {
    void executeWith(CSVLine csvLine);
}
