package datatransfer;

import com.google.gson.Gson;
import datadto.WorkbookDtoRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import processors.FileProcessor;

import java.io.IOException;


public class Get {
    public static WorkbookDtoRequest getData() throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://www.wix.com/_serverless/hiring-task-spreadsheet-evaluator/sheets");
        HttpResponse response = httpClient.execute(request);
        int responseStatus = response.getStatusLine().getStatusCode();

        if (responseStatus < 300) {
            String responseBody = EntityUtils.toString(response.getEntity());
            FileProcessor.writeDataToFile("data", responseBody);
            WorkbookDtoRequest workbook = new WorkbookDtoRequest();
            try {
                Gson gson = new Gson();
                workbook = gson.fromJson(responseBody, WorkbookDtoRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return workbook;
        } else {
            System.out.println("Connection to server failed! HTTP status code " + responseStatus);
            Runtime.getRuntime().exit(0);
        }
        return new WorkbookDtoRequest();
    }
}
