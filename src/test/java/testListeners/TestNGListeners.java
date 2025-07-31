package testListeners;

import driver.BaseClass;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.*;
import utils.GenericFunction;
import utils.GlobalVar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

public class TestNGListeners extends BaseClass implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("==================================================");
        System.out.println("|| TEST SUITE STARTED: " + suite.getName() + " ||");
        System.out.println("==================================================");
        GenericFunction.removeFilesFromFolder("videos", ".mov");
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("==================================================");
        System.out.println("|| TEST STARTED: " + getTestMethodName(iTestResult) + " ||");
        System.out.println("==================================================");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("--------------------------------------------------");
        System.out.println("|| TEST PASSED: " + getTestMethodName(iTestResult) + " ||");
        System.out.println("--------------------------------------------------");
        try {
            Allure.addAttachment("Success Logs", getLogs());
         //   Allure.addAttachment("Screenshot", new ByteArrayInputStream(xScreenShot()));
            xScreenShot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("**************************************************");
        System.out.println("** TEST FAILED: " + getTestMethodName(iTestResult) + " **");
        System.out.println("**************************************************");

        // Retry logic
        if (iTestResult.getMethod().getRetryAnalyzer(iTestResult) == null) {
            iTestResult.getMethod().setRetryAnalyzerClass(RetryAnalyzer.class);
        }

        try {
            Allure.addAttachment("Failure Logs", getLogs());
           // Allure.addAttachment("Screenshot", new ByteArrayInputStream(xScreenShot()));
            xScreenShot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~ TEST SKIPPED: " + getTestMethodName(iTestResult) + " ~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Allure.addAttachment("Skipped Screenshot", new ByteArrayInputStream(xScreenShot()));
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("##################################################");
        System.out.println("## TEST SUITE FINISHED: " + suite.getName() + " ##");
        System.out.println("##################################################");
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("--------------------------------------------------");
        System.out.println("|| TEST CONTEXT STARTED: " + iTestContext.getName() + " ||");
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("--------------------------------------------------");
        System.out.println("|| TEST CONTEXT FINISHED: " + iTestContext.getName() + " ||");
        System.out.println("--------------------------------------------------");
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "Test Screenshot", type = "image/png")
    public byte[] xScreenShot() {
        return ((TakesScreenshot) BaseClass.webDriver).getScreenshotAs(OutputType.BYTES);
    }

    public String getLogs() {
        List<LogEntry> logEntries = BaseClass.webDriver.manage().logs().get(LogType.BROWSER).getAll();
        StringBuilder logs = new StringBuilder();
        for (LogEntry entry : logEntries) {
            System.out.println("LOG: " + new Date(entry.getTimestamp()) + " --> " + entry.getLevel() + " --> " + entry.getMessage());
            logs.append(new Date(entry.getTimestamp()) + " --> " + entry.getLevel() + " --> " + entry.getMessage());
            logs.append(System.lineSeparator());
        }
        return logs.toString();
    }

    @Attachment(value = "Success/Failure Logs", type = "text/plain")
    public byte[] xLogs() {
        return getLogs().getBytes();
    }

    @Attachment(value = "Video of {0}", type = "video")
    public byte[] xVideos(String filename) {
        File file = new File("videos" + GlobalVar.slash + filename + ".mov");
        String path = file.getAbsolutePath();
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}