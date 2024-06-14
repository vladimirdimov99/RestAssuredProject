package api_implementations.utils.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    static ExtentReports extentReports = ExtentReportJUnit.createExtentSparkReports();
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>(); // <thread_id, extentTests

    public static synchronized ExtentTest onStart(String testName, String description) {
        ExtentTest test = extentReports.createTest(testName, description);
        extentTestMap.put((int) Thread.currentThread().getId(), test);

        return test;
    }

    public static synchronized ExtentTest getTest() {

        return extentTestMap.get((int) Thread.currentThread().getId());
    }
}
