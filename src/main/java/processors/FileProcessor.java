package processors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileProcessor {
    public static void writeDataToFile(String type, String data) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String filename = type + "_" + LocalDateTime.now().format(formatter) + ".txt";

        File file = new File(filename);
        FileWriter writer = new FileWriter(file);

        writer.write(data);
        writer.close();

        System.out.println("File created: " + filename);
    }
}
