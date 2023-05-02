package datadto;

import dataobjects.SheetCalculated;

import java.util.ArrayList;
import java.util.List;

public class WorkbookDtoResponse {
    private final String email = "kestux@gmail.com";
    private List<SheetCalculated> results = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public List<SheetCalculated> getResults() {
        return results;
    }

    public void setResults(List<SheetCalculated> results) {
        this.results = results;
    }

    public void addSheet(SheetCalculated sheet) {
        results.add(sheet);
    }

    @Override
    public String toString() {
        return "WorkbookDtoResponse{" +
                "email='" + email + '\'' +
                ", results=" + results +
                '}';
    }
}
