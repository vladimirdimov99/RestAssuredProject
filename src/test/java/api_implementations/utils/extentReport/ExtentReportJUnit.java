package api_implementations.utils.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportJUnit {
    public static final ExtentReports extentReports = new ExtentReports();

    public synchronized static ExtentReports createExtentSparkReports() {
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(".\\reports\\extent-report.html");
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().setReportName("Team Automation QA");

        //Attach Spark Reporter to Extent's Reporter
        extentReports.attachReporter(extentSparkReporter);

        //Generic System Info
        extentReports.setSystemInfo("Team", "Automation QA");
        extentReports.setSystemInfo("Env", System.getProperty("env"));
        extentReports.setSystemInfo("Reporter", "Vladimir");

        return extentReports;
    }
}
