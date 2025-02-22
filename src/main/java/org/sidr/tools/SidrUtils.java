package org.sidr.tools;




import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;



import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SidrUtils {
    public static List<String> getFileNames(String directoryPath) {
        List<String> fileNames = new ArrayList<>();

        // Создаем объект Path для указанной директории
        Path dir = Paths.get(directoryPath);

        // Проверяем, существует ли директория
        if (!Files.exists(dir)) {
            System.out.println("Директория не существует: " + directoryPath);
            return fileNames;
        }

        // Используем DirectoryStream для получения списка файлов
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    fileNames.add(entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении директории: " + e.getMessage());
        }
        return fileNames;
    }

    @Nullable
    public static String getStringFromJson(String JSON, String key) {
        JsonObject jsonObject = JsonParser.parseString(JSON).getAsJsonObject();
        return jsonObject.get(key).getAsString();
    }
}
