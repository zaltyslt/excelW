package datadto;

import java.util.List;

public class WorkbookDtoRequest {
    private String submissionUrl;
    private List<SheetDto> sheets;

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }

    public List<SheetDto> getSheets() {
        return sheets;
    }

    public void setSheets(List<SheetDto> sheets) {
        this.sheets = sheets;
    }

    @Override
    public String toString() {
        return "WorkbookDtoRequest{" +
                "submissionUrl='" + submissionUrl + '\'' +
                ", sheets=" + sheets.toString() +
                '}';
    }
}
