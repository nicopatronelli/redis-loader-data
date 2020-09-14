package com.example.redis_loader;

import java.util.function.BiConsumer;

public class Clazz {

    private BiConsumer<Integer, CSVLine> actionPerValue;
    private Action action;
    private boolean executeActionPerValue;

    public Clazz(CSVFile csvFile, BiConsumer<Integer, CSVLine> actionPerLine) {
        this.executeActionPerValue = false;
        this.action = () -> {
            String[] line;
            CSVLine csvLine = new CSVLine(new String[]{});
            while (csvFile.getLines().hasNext()) {
                line = csvFile.getLines().next();
                csvLine.setLine(line);
                this.forEachValue(csvLine);
                actionPerLine.accept(0, csvLine);
            }
        };
    }

    public Clazz andForEachValueDo(BiConsumer<Integer, CSVLine> actionPerValue) {
        this.actionPerValue = actionPerValue;
        this.executeActionPerValue = true;
        return this;
    }

    public void execute() {
        this.action.execute();
    }

    private void forEachValue(CSVLine csvLine) {
        if (executeActionPerValue) {
            for (int i = 1; i < csvLine.length(); i++)
                this.actionPerValue.accept(i, csvLine);
        }
    }
}
