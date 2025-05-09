package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TestSummaryReportGenerator
 */
public class TestSummaryReportGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    public void testGenerateSummaryReport() throws IOException {
        // Create temporary directories
        Path jsonDir = tempDir.resolve("json");
        Path reportDir = tempDir.resolve("report");
        Path outputFile = tempDir.resolve("test-summary.html");
        
        Files.createDirectories(jsonDir);
        Files.createDirectories(reportDir);
        
        // Create a sample Cucumber JSON report
        String sampleJson = createSampleCucumberJson();
        Path jsonFile = jsonDir.resolve("cucumber.json");
        try (FileWriter writer = new FileWriter(jsonFile.toFile())) {
            writer.write(sampleJson);
        }
        
        // Create a sample detailed report HTML file
        Path detailedReportFile = reportDir.resolve("overview-features.html");
        try (FileWriter writer = new FileWriter(detailedReportFile.toFile())) {
            writer.write("<html><body>Detailed Report</body></html>");
        }
        
        // Generate the summary report
        TestSummaryReportGenerator.generateSummaryReport(
            jsonDir.toString(),
            reportDir.toString(),
            outputFile.toString()
        );
        
        // Verify the summary report was created
        assertTrue(Files.exists(outputFile), "Summary report file should be created");
        
        // Read the content of the generated file
        String content = Files.readString(outputFile);
        
        // Verify the content contains expected elements
        assertTrue(content.contains("Test Execution Summary"), "Report should have a title");
        assertTrue(content.contains("Total Tests"), "Report should show total tests");
        assertTrue(content.contains("Passed"), "Report should show passed tests");
        assertTrue(content.contains("Failed"), "Report should show failed tests");
        assertTrue(content.contains("overview-features.html"), "Report should link to detailed report");
    }
    
    /**
     * Creates a sample Cucumber JSON report with one passing and one failing scenario
     */
    private String createSampleCucumberJson() {
        return "[\n" +
               "  {\n" +
               "    \"line\": 1,\n" +
               "    \"elements\": [\n" +
               "      {\n" +
               "        \"start_timestamp\": \"2023-01-01T12:00:00.000Z\",\n" +
               "        \"line\": 3,\n" +
               "        \"name\": \"Passing scenario\",\n" +
               "        \"description\": \"\",\n" +
               "        \"id\": \"feature-name;passing-scenario\",\n" +
               "        \"type\": \"scenario\",\n" +
               "        \"keyword\": \"Scenario\",\n" +
               "        \"steps\": [\n" +
               "          {\n" +
               "            \"result\": {\n" +
               "              \"duration\": 1000000,\n" +
               "              \"status\": \"passed\"\n" +
               "            },\n" +
               "            \"line\": 4,\n" +
               "            \"name\": \"a passing step\",\n" +
               "            \"match\": {\n" +
               "              \"location\": \"StepDefinitions.passingStep()\"\n" +
               "            },\n" +
               "            \"keyword\": \"Given \"\n" +
               "          }\n" +
               "        ]\n" +
               "      },\n" +
               "      {\n" +
               "        \"start_timestamp\": \"2023-01-01T12:01:00.000Z\",\n" +
               "        \"line\": 6,\n" +
               "        \"name\": \"Failing scenario\",\n" +
               "        \"description\": \"\",\n" +
               "        \"id\": \"feature-name;failing-scenario\",\n" +
               "        \"type\": \"scenario\",\n" +
               "        \"keyword\": \"Scenario\",\n" +
               "        \"steps\": [\n" +
               "          {\n" +
               "            \"result\": {\n" +
               "              \"duration\": 1000000,\n" +
               "              \"status\": \"failed\",\n" +
               "              \"error_message\": \"Test failed\"\n" +
               "            },\n" +
               "            \"line\": 7,\n" +
               "            \"name\": \"a failing step\",\n" +
               "            \"match\": {\n" +
               "              \"location\": \"StepDefinitions.failingStep()\"\n" +
               "            },\n" +
               "            \"keyword\": \"Given \"\n" +
               "          }\n" +
               "        ]\n" +
               "      }\n" +
               "    ],\n" +
               "    \"name\": \"Feature name\",\n" +
               "    \"description\": \"\",\n" +
               "    \"id\": \"feature-name\",\n" +
               "    \"keyword\": \"Feature\",\n" +
               "    \"uri\": \"file:features/test.feature\",\n" +
               "    \"tags\": []\n" +
               "  }\n" +
               "]";
    }
}