package com.assignment.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;

public class FileProcessor{

	@Value("${output.folder}")
    private String outFolder;

    @Value("${processed.folder}")
    private String processedFolder;

    @Value("${error.folder}")
    private String errorFolder;

    public String transform(String payload) throws MessagingException {
        try {
            String[] lines = payload.split("\n");
            int sum = 0;
            for (String line : lines) {
                try {
                    sum += Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    // move file to error folder if non-numeric line is encountered
                	System.out.println("Ignoring non-numeric line: " + line);
                	moveFileToErrorFolder(payload);
                }
            }
            // move file to out folder if file is valid
            moveFileToOutFolder(payload);
            return String.valueOf(sum);
        } catch (Exception e) {
            // handle general errors
            System.out.println("Error processing file: " + e.getMessage());
            throw new MessagingException("Error processing file", e);
        } finally {
            // move file to processed folder after processing
            moveFileToProcessedFolder(payload);
        }
    }
    
    private void moveFileToOutFolder(String payload) {
        String fileName = getOriginalFileName(payload);
        File file = new File(fileName);
        Path source = file.toPath();
        Path target = Paths.get(outFolder, file.getName());
        try {
            Files.move(source, target);
        } catch (IOException e) {
            System.out.println("Error moving file to out folder: " + e.getMessage());
        }
    }

    private void moveFileToErrorFolder(String payload) {
        String fileName = getOriginalFileName(payload);
        File file = new File(fileName);
        Path source = file.toPath();
        Path target = Paths.get(errorFolder, file.getName());
        try {
            Files.move(source, target);
        } catch (IOException e) {
            System.out.println("Error moving file to error folder: " + e.getMessage());
        }
    }

    private void moveFileToProcessedFolder(String payload) {
        String fileName = getOriginalFileName(payload);
        File file = new File(outFolder + "/" + fileName);
        Path source = file.toPath();
        Path target = Paths.get(processedFolder, file.getName());
        try {
            Files.move(source, target);
        } catch (IOException e) {
            System.out.println("Error moving file to processed folder: " + e.getMessage());
        }
    }

    private String getOriginalFileName(String payload) {
        // implement logic to extract the original file name from the payload
        // for example, if the payload is the file content, and the file name is the first line
        String[] lines = payload.split("\n");
        return lines[0];
    }
}
