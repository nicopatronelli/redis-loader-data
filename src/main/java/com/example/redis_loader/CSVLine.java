package com.example.redis_loader;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CSVLine {
    String[] l;

    public void setLine(String[] l) {
        this.l = l;
    }

    public String valueAt(int index){
        return l[index];
    }

    public int length() {
        return l.length;
    }
}
