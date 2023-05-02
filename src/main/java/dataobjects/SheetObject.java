package dataobjects;

import java.util.*;

public class SheetObject {
    private String id;
    private Map<String, CellObject> cells;

    public SheetObject() {
        this.cells = new LinkedHashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean checkCellByIndex(String index) {
        return this.cells.containsKey(index);
    }

    public CellObject getCellByIndex(String index) {
        return cells.get(index);
    }

    public void setCellByIndex(String index, CellObject value) {
        cells.put(index, value);
    }

    public List<CellObject> getCells() {
        return new ArrayList<>(this.cells.values());

    }

    @Override
    public String toString() {
        return "SheetObject{" +
                "id='" + id + '\'' +
                ", cells=" + cells +
                '}';
    }
}
