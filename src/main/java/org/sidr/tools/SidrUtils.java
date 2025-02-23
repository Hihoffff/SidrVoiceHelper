package org.sidr.tools;




import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;
import org.sidr.Sidr;


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
    @Nullable
    public static String getStringFromJsonWithPH(Sidr sidr, String JSON, String key) {
        try {
            JsonElement jsonElement = JsonParser.parseString(JSON);
            if (!jsonElement.isJsonObject()) {
                return null; // The root element is not a JSON object
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String[] keys;
            if (key.contains(sidr.getPropertiesManager().getJsonRootPlaceholder())) {
                keys = key.split(sidr.getPropertiesManager().getJsonRootPlaceholder());
            } else {
                // If no placeholder, treat the entire key as a single path
                keys = new String[]{key};
            }

            if (keys.length == 0) {
                return null;
            }

            // Traverse the JSON structure
            for (int i = 0; i < keys.length - 1; i++) {
                String curKey = keys[i];
                if (!jsonObject.has(curKey)) {
                    return null; // Key not found
                }
                JsonElement element = jsonObject.get(curKey);
                if (!element.isJsonObject()) {
                    return null; // Expected a JSON object but found a primitive
                }
                jsonObject = element.getAsJsonObject();
            }

            // Get the final value
            String finalKey = keys[keys.length - 1];
            if (!jsonObject.has(finalKey)) {
                return null; // Final key not found
            }
            JsonElement finalElement = jsonObject.get(finalKey);
            if (finalElement.isJsonPrimitive()) {
                return finalElement.getAsString(); // Return the primitive value as a string
            } else {
                return null; // The final value is not a primitive
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle any parsing errors
        }
    }
}
