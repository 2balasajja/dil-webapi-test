package utils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to generate detailed Cucumber reports using the Masterthought Cucumber Reporting library.
 */
public class CucumberReportGenerator {

    /**
     * Generates detailed Cucumber reports from JSON files.
     * 
     * @param jsonDir Directory containing the JSON report files
     * @param outputDir Directory where the generated reports will be saved
     * @param projectName Name of the project to display in the report
     */
    public static void generateDetailedReport(String jsonDir, String outputDir, String projectName) {
        File reportOutputDirectory = new File(outputDir);
        List<String> jsonFiles = findJsonFiles(jsonDir);
        
        if (jsonFiles.isEmpty()) {
            System.out.println("No JSON report files found in " + jsonDir);
            return;
        }

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        
        // Optional customization
        configuration.setSortingMethod(SortingMethod.NATURAL);
        configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
        configuration.setTrendsStatsFile(new File("target/cucumber-trends.json"));
        
        // Add build information
        configuration.setBuildNumber(System.getProperty("build.number", "1"));
        
        // Add system information
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("Java Version", System.getProperty("java.version"));
        configuration.addClassifications("Environment", System.getProperty("environment", "default"));

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
        
        System.out.println("Detailed Cucumber reports generated at: " + outputDir);
    }

    /**
     * Finds all JSON files in the specified directory.
     * 
     * @param directoryPath Path to the directory containing JSON files
     * @return List of paths to JSON files
     */
    private static List<String> findJsonFiles(String directoryPath) {
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return Collections.emptyList();
        }
        
        List<String> jsonFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    jsonFiles.add(file.getAbsolutePath());
                }
            }
        }
        
        return jsonFiles;
    }
}