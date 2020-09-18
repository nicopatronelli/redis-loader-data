package com.example.redis_loader.csv;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CSVLine {
    private String[] line;
    private final static int COLUMN_ONE = 0;
    private final static int COLUMN_TWO = 1;

    public void setLine(String[] line) {
        this.line = line;
    }

    public String valueAt(int index){
        return line[index];
    }

    public String valueAtFirstColumn() {
        return line[COLUMN_ONE];
    }

    public String valueAtSecondColumn() {
        return line[COLUMN_TWO];
    }

    public int length() {
        return line.length;
    }
}
