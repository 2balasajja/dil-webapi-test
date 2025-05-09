package runners;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import utils.CucumberReportGenerator;
import utils.TestSummaryReportGenerator;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "stepDefinitions",
        plugin = {
            "pretty", 
            "html:target/cucumber-reports.html",
            "json:target/cucumber-reports/cucumber.json"
        },
        publish = true
        // Tags will be provided via command line or default to @test
)
public class TestRunner {
    
    @AfterClass
    public static void generateDetailedReport() {
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
    }
}

