package com.example.redis_loader.csv;

public class ActionsPerCSVFileBuilder {

    private ActionPerValue actionPerValue;
    private ActionPerCompleteCSVFile actionPerCompleteCSVFile;
    private boolean executeActionPerCell;

    public ActionsPerCSVFileBuilder(CSVFile csvFile, ActionPerLine actionPerLine) {
        this.executeActionPerCell = false;
        this.actionPerCompleteCSVFile = () -> {
            String[] line;
            CSVLine csvLine = new CSVLine(new String[]{});
            while (csvFile.getLines().hasNext()) {
                line = csvFile.getLines().next();
                csvLine.setLine(line);
                this.forEachValueIn(csvLine);
                actionPerLine.executeWith(csvLine);
            }
        };
    }

    public ActionsPerCSVFileBuilder andForEachCell(ActionPerValue actionPerValue) {
        this.actionPerValue = actionPerValue;
        this.executeActionPerCell = true;
        return this;
    }

    public void execute() {
        this.actionPerCompleteCSVFile.execute();
    }

    private void forEachValueIn(CSVLine csvLine) {
        if (executeActionPerCell) {
            for (int column = 1; column < csvLine.length(); column++)
                this.actionPerValue.executeAt(column, csvLine);
        }
    }
}
