package com.wilczewski.partsshopadmin.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUpload {

    public static void saveFile(String dir, String fileName, MultipartFile multipartFile) throws IOException {

        Path upladPath = Paths.get(dir);

        if (!Files.exists(upladPath)){
            Files.createDirectories(upladPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = upladPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex){
            throw new IOException("Nie można zapisać pliku: " + fileName, ex);
        }
    }

    public static void cleanDir(String dir){
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        System.out.println("Nie można usunąć pliku " +file);
                    }
                }
            });
        } catch (IOException ex){
            System.out.println("Brak listy " + dirPath);
        }
    }

    public static void removeDir(String dir){
        cleanDir(dir);

        try {
            Files.delete(Paths.get(dir));
        } catch (IOException ex) {
            System.out.println("Nie można usunąć katalogu!");
        }
    }

}
