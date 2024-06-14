package api_implementations.utils.extentReport;

import com.aventstack.extentreports.model.Log;
import org.apache.logging.log4j.LogManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

public class TestListener implements ITestListener {

    private static final Logger Log = (Logger) LogManager.getLogger(TestListener.class);

    private static String getTestDescriptionName(ITestResult testResult) {
        return testResult.getMethod().getDescription();
    }

    @Override
    public void onStart(ITestContext testContext) {

    }

    @Override
    public void onFinish(ITestContext testContext) {
        ExtentTestManager.extentReports.flush();
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        Log.info("Starting with test: " + getTestDescriptionName(testResult));
    }
}
