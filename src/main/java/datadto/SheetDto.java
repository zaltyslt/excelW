package datadto;

import java.util.ArrayList;
import java.util.List;

public class SheetDto {
    private String id;
    private List<List<String>> data;
    public SheetDto() {
        this.data = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
    public void addData(List<String> data) {
        this.data.add(data);
    }

    @Override
    public String toString() {
        return "SheetDto{" +
                "id='" + id + '\'' +
                ", data=" + data +
                '}';
    }
}
