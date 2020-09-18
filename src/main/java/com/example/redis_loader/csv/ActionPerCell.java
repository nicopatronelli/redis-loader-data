package com.example.redis_loader.csv;

@FunctionalInterface
public interface ActionPerCell {
    void executeAt(int index, CSVLine csvLine);
}
