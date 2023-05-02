package dataobjects;

import java.util.ArrayList;
import java.util.List;

public class SheetCalculated {
    private String id;
    List<Object> data = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
    public void addColumn(List<Object> column) {
        this.data.add(column);
    }
}
