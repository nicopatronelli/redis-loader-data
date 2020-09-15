package com.example.redis_loader;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CSVLine {
    String[] line;

    public void setLine(String[] line) {
        this.line = line;
    }

    public String valueAt(int index){
        return line[index];
    }

    public int length() {
        return line.length;
    }
}
