package com.example.redis_loader.csv;

@FunctionalInterface
public interface ActionPerValue {
    void executeAt(int index, CSVLine csvLine);
}
