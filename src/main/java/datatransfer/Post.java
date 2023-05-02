package datatransfer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datadto.WorkbookDtoResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import processors.FileProcessor;

import java.io.IOException;
import java.net.URISyntaxException;

public class Post {

    public static void sendData(WorkbookDtoResponse workbookDtoResponse, String submissionUrl) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(workbookDtoResponse);
        FileProcessor. writeDataToFile("response", prettyJson);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(submissionUrl);
        request.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8"));
        request.setHeader(new BasicHeader(HttpHeaders.ACCEPT,"application/json"));
        request.setEntity(new StringEntity(prettyJson));
        HttpResponse response = httpClient.execute(request);

        int responseStatus = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        if (responseStatus < 300) {
            FileProcessor.writeDataToFile("statusOK", responseBody);
            System.out.println("Connection successful! HTTP status code " + responseStatus);
        } else {
            FileProcessor.writeDataToFile("statusBAD", responseBody);
            System.out.println("Connection failed! HTTP status code " + responseStatus);
            Runtime.getRuntime().exit(0);
        }
    }
}
