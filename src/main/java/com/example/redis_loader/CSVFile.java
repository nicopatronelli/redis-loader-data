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

    public ActionsPerCSVFileBuilder forEachLine(BiConsumer<Integer, CSVLine> actionPerLine) {
        return new ActionsPerCSVFileBuilder(this, actionPerLine);
    }

    public String headerColumnAt(int index) {
        return this.header[index];
    }

}
