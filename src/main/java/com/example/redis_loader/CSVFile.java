package com.example.redis_loader;

import com.opencsv.CSVReader;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class CSVFile {
    private final String[] header;
    private final Iterator<String[]> lines;

    public CSVFile(CSVReader csvReader) {
        this.lines = csvReader.iterator();
        this.header = lines.next();
    }

    public String[] header() {
        return this.header;
    }

    public void forEachLine(BiConsumer<Integer, CSVLine> actionPerLine, BiConsumer<Integer, CSVLine> actionPerValue) {
        String[] line;
        CSVLine csvLine = new CSVLine(new String[]{});
        while (lines.hasNext()) {
            line = lines.next();
            csvLine.setLine(line);
            for (int i = 1; i < csvLine.length(); i++)
                actionPerValue.accept(i, csvLine);
            actionPerLine.accept(0, csvLine);
        }
    }

    public String headerColumnAt(int index) {
        return this.header[index];
    }

}
