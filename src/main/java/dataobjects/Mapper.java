package dataobjects;

import datadto.SheetDto;
import datadto.WorkbookDtoResponse;
import processors.TypeProcessor;

import java.util.*;

public class Mapper {

    public static WorkBookObject toWorkbookObject(List<SheetDto> sheetDtoList) {
        WorkBookObject workBook = new WorkBookObject();
        for (SheetDto sheetDto : sheetDtoList) {
            SheetObject sheet = new SheetObject();
            sheet.setId(sheetDto.getId());
            if (sheetDto.getData().size() > 0) {

                for (int i = 0; i < sheetDto.getData().size(); i++) {
                    char letter = 'A';
                    var tempColumn = sheetDto.getData().get(i);
                    for (int j = 0; j < tempColumn.size(); j++) {
                        var cell = tempColumn.get(j);
                        CellObject tempCell = new CellObject(cell);
                        TypeProcessor.fillCellValues(tempCell);
                        tempCell.setIndex(String.valueOf(letter) + (i + 1));
                        sheet.setCellByIndex(tempCell.getIndex(), tempCell);
                        letter++;
                    }
                }
            }
            workBook.addSheet(sheet);
        }
        return workBook;
    }

    public static WorkbookDtoResponse toWorkbookDtoResponse(List<SheetObject> sheets) {
        WorkbookDtoResponse workbookDtoResponse = new WorkbookDtoResponse();
        for (SheetObject sheet : sheets) {
            SheetCalculated sheetCalculated = new SheetCalculated();
            sheetCalculated.setId(sheet.getId());

            if (sheet.getCells() != null) {
                String columnName = "";
                List<CellObject> cells = sheet.getCells();
                Map<String, List<CellObject>> cellsInColumns = new HashMap<>();

                for (CellObject cell : cells) {
                    String index = cell.getIndex().substring(1);
                    if (!cellsInColumns.containsKey(index)) {
                        cellsInColumns.put(index, new ArrayList<CellObject>());
                    }
                    cellsInColumns.get(index).add(cell);
                }
                columnToValues(sheetCalculated, cellsInColumns);
            }
            workbookDtoResponse.addSheet(sheetCalculated);
        }
        return workbookDtoResponse;
    }

    private static void columnToValues(SheetCalculated sheetCalculated, Map<String, List<CellObject>> cellsByColumns) {

        for (int i = 1; i <= cellsByColumns.size(); i++) {
            List<Object> columnValues = new ArrayList<>();
            var columnCells = cellsByColumns.get(String.valueOf(i));

            for (CellObject cell : columnCells) {
                if (cell.getType().equals("error") || cell.getType().equals("string")) {
                    columnValues.add(cell.getValue());
                } else if (cell.getType().equals("boolean")) {
                    columnValues.add(cell.getBooleanValue());
                } else if (cell.getType().equals("integer")) {
                    columnValues.add(cell.getIntValue());
                } else if (cell.getType().equals("float")) {
                    columnValues.add(cell.getFloatValue());
                }
            }
            sheetCalculated.addColumn(columnValues);
        }
    }
}
