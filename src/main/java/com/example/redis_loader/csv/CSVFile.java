package com.example.redis_loader.csv;

import com.opencsv.CSVReader;
import lombok.Getter;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CSVFile {
    private final String[] header;
    @Getter private final Iterator<String[]> lines;
    private final static int COLUMN_ONE = 0;

    public CSVFile(CSVReader csvReader) {
        this.lines = csvReader.iterator();
        this.header = lines.next();
    }

    public ActionsPerCSVFileBuilder forEachLine(ActionPerLine actionPerLine) {
        return new ActionsPerCSVFileBuilder(this, actionPerLine);
    }

    public String headerColumnAt(int index) {
        return this.header[index];
    }

    public String headerValueAtFirstColumn() {
        return this.header[COLUMN_ONE];
    }


}
