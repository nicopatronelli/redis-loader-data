package com.example.redis_loader;

import com.opencsv.CSVReader;
import lombok.Getter;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class CSVFile {
    private final String[] header;
    @Getter private final Iterator<String[]> lines;

    public CSVFile(CSVReader csvReader) {
        this.lines = csvReader.iterator();
        this.header = lines.next();
    }

    public String[] header() {
        return this.header;
    }

    public Clazz forEachLine(BiConsumer<Integer, CSVLine> actionPerLine) {
        return new Clazz(this, actionPerLine);
    }

    public String headerColumnAt(int index) {
        return this.header[index];
    }

}
