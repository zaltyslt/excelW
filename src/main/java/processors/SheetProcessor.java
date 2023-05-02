package processors;

import dataobjects.CellObject;
import dataobjects.SheetObject;

public class SheetProcessor {
    private SheetObject sheet;

    public SheetProcessor(SheetObject sheet) {
        this.sheet = sheet;
    }

    public void calculateSheet() {
        int count = sheet.getCells().size();
        int processed = 0;
        while (processed < count) {
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    processed = 0;
                }
                CellObject cellToProcess = sheet.getCells().get(i);
                if (!cellToProcess.isProcessed()) {
                    //process cell
                    CellObject processedCell;
                    if (cellToProcess.getType().equals("reference")) {
                        processedCell = processReference(cellToProcess);
                    } else {
                        //process formula
                        FormulaProcessor formulaToProcess = new FormulaProcessor(this.sheet);
                        processedCell = formulaToProcess.formulaAnalyzer(cellToProcess.getFormulaValue());
                        copyCellData(cellToProcess, processedCell);
                    }
                }
                processed++;
            }
        }
    }

    private static void copyCellData(CellObject target, CellObject source) {
        target.setProcessed(true);
        target.setValue(source.getValue());
        target.setType(source.getType());
        target.setIntValue(source.getIntValue());
        target.setBooleanValue(source.getBooleanValue());
        target.setFloatValue(source.getFloatValue());
        target.setReferenceValue(null);
    }

    private CellObject processReference(CellObject cell) {
        var referredCell = cell.getReferenceValue();
        if (sheet.checkCellByIndex(referredCell)) {
            if (sheet.getCellByIndex(referredCell).getType().equals("reference")) {
                processReference(sheet.getCellByIndex(referredCell));
            }
            copyCellData(cell, sheet.getCellByIndex(referredCell));

        } else {
            cell.setType("error");
            cell.setValue("ERROR");
            cell.setProcessed(true);
            return cell;
        }
        return cell;
    }


}
