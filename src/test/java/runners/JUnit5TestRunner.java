package runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import utils.CucumberReportGenerator;
import utils.TestSummaryReportGenerator;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepDefinitions")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports.html, json:target/cucumber-reports/cucumber-junit5.json")
public class JUnit5TestRunner {
    // This class doesn't need any code - it's just a runner
    
    // Since JUnit 5 doesn't have @AfterClass, we need to use a shutdown hook to generate reports
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Generate detailed Cucumber reports
            CucumberReportGenerator.generateDetailedReport(
                "target/cucumber-reports", 
                "target/cucumber-detailed-reports", 
                "Cucumber Test Results"
            );
            
            // Generate summary report with statistics
            TestSummaryReportGenerator.generateSummaryReport(
                "target/cucumber-reports",
                "target/cucumber-detailed-reports",
                "target/test-summary.html"
            );
            
            System.out.println("\n\n==================================================");
            System.out.println("Test Summary Report: target/test-summary.html");
            System.out.println("==================================================\n");
        }));
    }
}