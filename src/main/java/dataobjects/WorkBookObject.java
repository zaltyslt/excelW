package dataobjects;

import java.util.ArrayList;
import java.util.List;

public class WorkBookObject {
private List<SheetObject> workbook;

    public WorkBookObject() {
        this.workbook = new ArrayList<>();
    }

    public List<SheetObject> getSheets() {
        return workbook;
    }

    public void setSheets(List<SheetObject> workbook) {
        this.workbook = workbook;
    }
    public void addSheet(SheetObject sheet){
        workbook.add(sheet);
    }
    public SheetObject getSheetByIndex(int index){
        return workbook.get(index);
    }
}
