package lt.ks.app;

import datadto.SheetDto;
import datadto.WorkbookDtoRequest;
import datadto.WorkbookDtoResponse;
import dataobjects.Mapper;
import dataobjects.SheetObject;
import dataobjects.WorkBookObject;
import datatransfer.Get;
import datatransfer.Post;
import processors.SheetProcessor;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        WorkbookDtoRequest workbookDtoRequest = Get.getData();
        List<SheetDto> sheetsDto = workbookDtoRequest.getSheets();
        WorkBookObject workbookObject = Mapper.toWorkbookObject(sheetsDto);

        for (SheetObject sheet: workbookObject.getSheets()) {
            if(sheet.getCells().size() > 0){
                SheetProcessor sheetToProcess = new SheetProcessor(sheet);
                sheetToProcess.calculateSheet();
            }
        }

        List<SheetObject> sheetObjects = workbookObject.getSheets();
        WorkbookDtoResponse workbookDtoResponse = Mapper.toWorkbookDtoResponse(sheetObjects);
        Post.sendData(workbookDtoResponse, workbookDtoRequest.getSubmissionUrl());
    }
}