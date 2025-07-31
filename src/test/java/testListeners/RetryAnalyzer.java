package testListeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int maxTry = 2; // Total attempts = initial try + retries

    /**
     * This method decides whether a test should be retried.
     *
     * @param result The result of the test method that just ran
     * @return true if the test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (count < maxTry) {
            count++;
            System.out.println("Retrying test " + result.getName() + " (" + count + "/" + maxTry + ")");
            return true;
        }
        return false;
    }
}