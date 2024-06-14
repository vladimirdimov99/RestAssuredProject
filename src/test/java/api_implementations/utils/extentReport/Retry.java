package api_implementations.utils.extentReport;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private int count = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        int maxRetry = 3;

        if (!iTestResult.isSuccess()) {
            if (count <= maxRetry) {
                count++;
                iTestResult.setStatus(iTestResult.FAILURE);
                return true;
            }
        }else{
            iTestResult.setStatus(iTestResult.SUCCESS);
        }
        return false;
    }
}
