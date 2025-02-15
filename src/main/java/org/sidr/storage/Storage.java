package org.sidr.storage;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class Storage {
    private final String FILENAME;
    private final Yaml yaml = new Yaml();
    private HashMap<String, Object> objects = new HashMap<>();
    public Storage(String path, String filename) {
        if(!filename.endsWith(".yml")) {
            filename += ".yml";
        }
        this.FILENAME = (path+"\\"+filename).replace("\\", File.separator);

        Path pathToFile = Paths.get(FILENAME);
        Path parentDir = pathToFile.getParent();

        try {
            // Создаем директории, если их нет
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);  // createDirectories() создаст все необходимые папки
                //System.out.println("Директории успешно созданы: " + parentDir.toAbsolutePath());
            }

            // Проверяем наличие самого файла, если его нет, создаем
            if (Files.notExists(pathToFile)) {
                Files.createFile(pathToFile);
                //System.out.println("Файл успешно создан: " + pathToFile.toAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла или директории: " + e.getMessage());
        }

        try (InputStream input = new FileInputStream(FILENAME)) {
            objects = yaml.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(objects == null){
            objects = new HashMap<>();
        }
    }

    public void save(){
        try (FileWriter writer = new FileWriter(FILENAME)) {
            yaml.dump(objects, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HashMap<String,Object> getObjects(){
        return this.objects;
    }

}
