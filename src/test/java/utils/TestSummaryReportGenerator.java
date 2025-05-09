package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to generate a summary HTML report of test execution results.
 * This report shows total tests run, passed tests, and failed tests with links to detailed reports.
 */
public class TestSummaryReportGenerator {

    /**
     * Generates a summary HTML report from Cucumber JSON files.
     *
     * @param jsonDir Directory containing the JSON report files
     * @param detailedReportDir Directory containing the detailed HTML reports
     * @param outputFile Path where the summary HTML report will be saved
     */
    public static void generateSummaryReport(String jsonDir, String detailedReportDir, String outputFile) {
        try {
            List<String> jsonFiles = findJsonFiles(jsonDir);
            
            if (jsonFiles.isEmpty()) {
                System.out.println("No JSON report files found in " + jsonDir);
                return;
            }

            int totalScenarios = 0;
            int passedScenarios = 0;
            int failedScenarios = 0;

            // Process each JSON file to collect statistics
            for (String jsonFile : jsonFiles) {
                try (FileReader reader = new FileReader(jsonFile)) {
                    JsonArray features = JsonParser.parseReader(reader).getAsJsonArray();
                    
                    for (JsonElement featureElement : features) {
                        JsonObject feature = featureElement.getAsJsonObject();
                        JsonArray elements = feature.getAsJsonArray("elements");
                        
                        for (JsonElement elementElement : elements) {
                            JsonObject element = elementElement.getAsJsonObject();
                            
                            // Only count scenarios (not backgrounds)
                            if ("scenario".equals(element.get("type").getAsString())) {
                                totalScenarios++;
                                
                                boolean scenarioPassed = true;
                                JsonArray steps = element.getAsJsonArray("steps");
                                
                                for (JsonElement stepElement : steps) {
                                    JsonObject step = stepElement.getAsJsonObject();
                                    JsonObject result = step.getAsJsonObject("result");
                                    
                                    if (result != null && result.has("status")) {
                                        String status = result.get("status").getAsString();
                                        if (!"passed".equals(status)) {
                                            scenarioPassed = false;
                                            break;
                                        }
                                    }
                                }
                                
                                if (scenarioPassed) {
                                    passedScenarios++;
                                } else {
                                    failedScenarios++;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing JSON file " + jsonFile + ": " + e.getMessage());
                }
            }

            // Generate HTML report
            generateHtmlReport(totalScenarios, passedScenarios, failedScenarios, detailedReportDir, outputFile);
            
            System.out.println("Summary report generated at: " + outputFile);
            
        } catch (Exception e) {
            System.err.println("Error generating summary report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates the HTML summary report with test statistics and links to detailed reports.
     */
    private static void generateHtmlReport(int totalScenarios, int passedScenarios, int failedScenarios, 
                                          String detailedReportDir, String outputFile) throws IOException {
        // Find the index.html file in the detailed report directory
        String detailedReportLink = findDetailedReportIndexFile(detailedReportDir);
        
        // Calculate pass percentage
        double passPercentage = totalScenarios > 0 ? 
            (double) passedScenarios / totalScenarios * 100 : 0;
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>Test Execution Summary</title>\n")
            .append("    <style>\n")
            .append("        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }\n")
            .append("        .container { max-width: 800px; margin: 0 auto; }\n")
            .append("        h1 { color: #2c3e50; }\n")
            .append("        .summary-card { background-color: #f8f9fa; border-radius: 8px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n")
            .append("        .stat-container { display: flex; flex-wrap: wrap; gap: 20px; margin-top: 20px; }\n")
            .append("        .stat-box { flex: 1; min-width: 150px; padding: 15px; border-radius: 8px; color: white; text-align: center; }\n")
            .append("        .total { background-color: #3498db; }\n")
            .append("        .passed { background-color: #2ecc71; }\n")
            .append("        .failed { background-color: #e74c3c; }\n")
            .append("        .stat-number { font-size: 36px; font-weight: bold; margin: 10px 0; }\n")
            .append("        .stat-label { font-size: 16px; }\n")
            .append("        .progress-bar { height: 20px; background-color: #ecf0f1; border-radius: 10px; margin: 20px 0; overflow: hidden; }\n")
            .append("        .progress { height: 100%; background-color: #2ecc71; width: ").append(String.format("%.1f", passPercentage)).append("%; }\n")
            .append("        .percentage { text-align: center; font-weight: bold; margin-top: 5px; }\n")
            .append("        .links { margin-top: 30px; }\n")
            .append("        .report-link { display: inline-block; background-color: #3498db; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; margin-right: 10px; }\n")
            .append("        .report-link:hover { background-color: #2980b9; }\n")
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <div class=\"container\">\n")
            .append("        <h1>Test Execution Summary</h1>\n")
            .append("        <div class=\"summary-card\">\n")
            .append("            <div class=\"stat-container\">\n")
            .append("                <div class=\"stat-box total\">\n")
            .append("                    <div class=\"stat-number\">").append(totalScenarios).append("</div>\n")
            .append("                    <div class=\"stat-label\">Total Tests</div>\n")
            .append("                </div>\n")
            .append("                <div class=\"stat-box passed\">\n")
            .append("                    <div class=\"stat-number\">").append(passedScenarios).append("</div>\n")
            .append("                    <div class=\"stat-label\">Passed</div>\n")
            .append("                </div>\n")
            .append("                <div class=\"stat-box failed\">\n")
            .append("                    <div class=\"stat-number\">").append(failedScenarios).append("</div>\n")
            .append("                    <div class=\"stat-label\">Failed</div>\n")
            .append("                </div>\n")
            .append("            </div>\n")
            .append("            <div class=\"progress-bar\">\n")
            .append("                <div class=\"progress\"></div>\n")
            .append("            </div>\n")
            .append("            <div class=\"percentage\">").append(String.format("%.1f", passPercentage)).append("% Passed</div>\n")
            .append("            <div class=\"links\">\n");
        
        if (detailedReportLink != null) {
            html.append("                <a href=\"").append(detailedReportLink).append("\" class=\"report-link\">View Detailed Report</a>\n");
        }
        
        html.append("                <a href=\"cucumber-reports.html\" class=\"report-link\">View Basic Report</a>\n")
            .append("            </div>\n")
            .append("        </div>\n")
            .append("    </div>\n")
            .append("</body>\n")
            .append("</html>");

        // Create parent directories if they don't exist
        File file = new File(outputFile);
        file.getParentFile().mkdirs();
        
        // Write the HTML to file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html.toString());
        }
    }

    /**
     * Finds the index.html file in the detailed report directory.
     */
    private static String findDetailedReportIndexFile(String detailedReportDir) {
        File dir = new File(detailedReportDir);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        
        File indexFile = new File(dir, "overview-features.html");
        if (indexFile.exists()) {
            return "overview-features.html";
        }
        
        // Look for any HTML file if overview-features.html doesn't exist
        File[] htmlFiles = dir.listFiles((d, name) -> name.endsWith(".html"));
        if (htmlFiles != null && htmlFiles.length > 0) {
            return htmlFiles[0].getName();
        }
        
        return null;
    }

    /**
     * Finds all JSON files in the specified directory.
     */
    private static List<String> findJsonFiles(String directoryPath) {
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return new ArrayList<>();
        }
        
        List<String> jsonFiles = new ArrayList<>();
        findJsonFilesRecursively(directory, jsonFiles);
        
        return jsonFiles;
    }
    
    /**
     * Recursively finds all JSON files in the directory and its subdirectories.
     */
    private static void findJsonFilesRecursively(File directory, List<String> jsonFiles) {
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    jsonFiles.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    findJsonFilesRecursively(file, jsonFiles);
                }
            }
        }
    }
}