package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LoggerUtil {

    public static void log(String message) {
        try (FileWriter fw = new FileWriter("logs.txt", true)) {

            fw.write(LocalDateTime.now() + " - " + message + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}