package com.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.util.FileProcessor;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FileProcessorTest {

	@InjectMocks
    private FileProcessor fileProcessor;

    @Mock
    private File file;

    @Test
    public void testProcessFile_validFile() throws IOException {
        // Load the valid file
        File validFile = new File("src/main/resources/TestTask_002_valid-file-1.txt");
        String fileContent = new String(Files.readAllBytes(Paths.get(validFile.getAbsolutePath())));

        // Process the file
        String result = fileProcessor.transform(fileContent);

        // Verify that the file was processed correctly
        assertNotNull(result);
        assertEquals("File processed successfully", result);

        // Verify that the file was moved to the processed directory
        File processedFile = new File("processed/" + validFile.getName());
        assertTrue(processedFile.exists());

        // Clean up
        processedFile.delete();
    }

    @Test
    public void testProcessFile_invalidFile() throws IOException {
        // Load the invalid file
        File invalidFile = new File("src/main/resources/TestTask_002_invalid-file-1.txt");
        String fileContent = new String(Files.readAllBytes(Paths.get(invalidFile.getAbsolutePath())));

        // Process the file
        String result = fileProcessor.transform(fileContent);

        // Verify that the file was processed correctly
        assertNotNull(result);
        assertEquals("File contains invalid data", result);

        // Verify that the file was moved to the error directory
        File errorFile = new File("error/" + invalidFile.getName());
        assertTrue(errorFile.exists());

        // Clean up
        errorFile.delete();
    }
}