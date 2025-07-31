package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public class SeleniumWait {

    public static final Logger logger = LoggerFactory.getLogger(SeleniumWait.class);

    public static WebDriverWait webDriverWait;
    public static final int DEFAULT_TIMEOUT = 60;
    public static final int POLLING_TIME = 5;

    public static JavascriptExecutor getJavaScriptInstance(WebDriver webDriver) {
        return (JavascriptExecutor) webDriver;
    }

    public static Object executeJS(WebDriver webDriver, String query) {
        JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
        return javascriptExecutor.executeScript(query);
    }

    public static Object executeJS(WebDriver webDriver, WebElement webElement, String query) {
        JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
        return javascriptExecutor.executeScript(query, webElement);
    }

    public static WebDriverWait getWebDriverWaitInstance(WebDriver webDriver) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebDriverWait getWebDriverWaitInstance(WebDriver webDriver, int timeOutsInSeconds) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(timeOutsInSeconds));
    }

    public static WebDriverWait getWebDriverWaitInstance(WebDriver webDriver, int timeOutsInSeconds, int pollingEveryInMilliseconds) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(timeOutsInSeconds), Duration.ofMillis(pollingEveryInMilliseconds));
    }

    public static boolean waitForURLToContain(WebDriver webDriver, String urlFraction) {
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return webDriverWait.until(ExpectedConditions.urlContains(urlFraction));
    }

    public static boolean waitForURLToContain(WebDriver webDriver, String urlFraction, int timeOutsInSeconds) {
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeOutsInSeconds));
        return webDriverWait.until(ExpectedConditions.urlContains(urlFraction));
    }

    public static boolean waitForURLToBe(WebDriver webDriver, String url, int timeOutsInSeconds) {
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeOutsInSeconds));
        return webDriverWait.until(ExpectedConditions.urlToBe(url));
    }

    /**
     * Method for wait till title is present
     *
     * @param webDriver         : WebDriver instance
     * @param titleFraction     : String title
     * @param timeOutsInSeconds : integer value in seconds
     */
    public static boolean waitForTitleContains(WebDriver webDriver, String titleFraction, int timeOutsInSeconds) {
        webDriverWait = getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        return webDriverWait.until(ExpectedConditions.titleContains(titleFraction));
    }

    /**
     * Method for wait till title is present
     *
     * @param webDriver         : WebDriver instance
     * @param title             : String title
     * @param timeOutsInSeconds : integer value in seconds
     */
    public static boolean waitForTitleToBe(WebDriver webDriver, String title, int timeOutsInSeconds) {
        webDriverWait = getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        return webDriverWait.until(ExpectedConditions.titleIs(title));
    }

    /**
     * Used to stop the current execution for some amount of time
     *
     * @param timeoutsInSeconds : no. of seconds, you want to stop the execution.
     */
    public static void pauseExecution(int timeoutsInSeconds) {
        try {
            Thread.sleep(timeoutsInSeconds * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to stop the current execution for some amount of time (Provide the value with l otherwise it will treat as timeoutsInSeconds)
     *
     * @param timeoutsInMilliseconds : no. of milliseconds, you want to stop the execution.
     */
    public static void pauseExecution(long timeoutsInMilliseconds) {
        try {
            Thread.sleep(timeoutsInMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************************************    Waits    **********************************************/
    public static ExpectedCondition<WebElement> visibilityOfElementLocated(final WebElement webElement) {
        return driver -> {
            pauseExecution(30L);
            return webElement.isDisplayed() ? webElement : null;
        };
    }

    public static WebElement waitForExpectedElement(WebDriver webDriver, final WebElement webElement) {
        logger.info("Waiting for expected element");
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return webDriverWait.until(visibilityOfElementLocated(webElement));
    }

    public static WebElement retryingElement(WebDriver webDriver, WebElement webElement) {
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        ExpectedCondition<Boolean> elementDisplayed = arg0 -> webElement.isDisplayed();
        webDriverWait.until(elementDisplayed);

        return webElement;
    }

    public WebElement retryingElement(WebElement webElement, int timeOut) {
        int attempts = 0;
        while (attempts < timeOut) {
            try {
                webElement.isDisplayed();
                break;
            } catch (NoSuchElementException e) {
                logger.warn("element is not found in attempt: " + attempts + " : " + webElement);
                pauseExecution(500L);
            }
            attempts++;
        }

        if (webElement == null) {
            try {
                throw new Exception("ELEMENT NOT FOUND EXCEPTION");
            } catch (Exception e) {
                logger.error("Element is not found in attempt : " + attempts + " timeOut : " + timeOut + " with the interval of : " + 500 + "ms : " + webElement);
            }
        }
        return webElement;
    }

    public static WebElement retryingElement(WebElement webElement, int timeOut, long pollingTime) {
        int attempts = 0;
        while (attempts < timeOut) {
            try {
                webElement.isDisplayed();
                break;
            } catch (NoSuchElementException e) {
                logger.error("Element is not found in attempt: " + attempts + " timeOut : " + timeOut + " with the interval of : " + pollingTime + " : " + webElement);
                pauseExecution(pollingTime);
            }

            attempts++;
        }
        if (webElement == null) {
            try {
                throw new Exception("ELEMENT NOT FOUND EXCEPTION");
            } catch (Exception e) {
                logger.error("element is not found exception....tried for :" + timeOut + " with the interval of : " + pollingTime + "ms");
            }
        }
        return webElement;
    }

    /**
     * Used to wait for WebElement using Fluent wait
     *
     * @param webDriver         : WebDriver Instance
     * @param webElement        : WebElement
     * @param timeOutsInSeconds : time in seconds
     * @param pollingTime       : polling every in milliseconds
     * @return : WebElement Instance
     */
    public static WebElement waitForElementUsingFluentWait(WebDriver webDriver, WebElement webElement, int timeOutsInSeconds, int pollingTime) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofSeconds(timeOutsInSeconds))
                .pollingEvery(Duration.ofMillis(pollingTime))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Used to wait for WebElement using WebDriver wait
     *
     * @param webDriver         : WebDriver Instance
     * @param webElement        : WebElement
     * @param timeOutsInSeconds : time in seconds
     * @param pollingTime       : polling every in milliseconds
     * @return : WebElement Instance
     */
    public static WebElement waitForElementUsingWebDriverWait(WebDriver webDriver, WebElement webElement, int timeOutsInSeconds, int pollingTime) {
        webDriverWait = getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        webDriverWait.pollingEvery(Duration.ofMillis(pollingTime))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
    }

    //**************************************** Page Load Condition **************************************/

    /**
     * Used to wait for page to be loaded within the default time.
     *
     * @param webDriver : WebDriver Instance
     */
    public static void waitForJQueryLoad(WebDriver webDriver) {
        try {
            JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
            ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) javascriptExecutor
                    .executeScript("return jQuery.active") == 0);

            boolean jqueryReady = (Boolean) javascriptExecutor.executeScript("return jQuery.active==0");

            if (!jqueryReady) {
                webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
                webDriverWait.until(jQueryLoad);
            }
        } catch (WebDriverException ignored) {
            logger.error("WebDriver Exception :--> " + ignored.getMessage());
        }
    }

    /**
     * Wait for element to be loaded
     *
     * @return true if page loads successful otherwise false
     */
    public static boolean waitForPageLoaded(WebDriver webDriver) {
        int count = 0;
        JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
        Boolean jquery = (Boolean) executeJS(webDriver, "return window.jQuery != undefined");
        if (jquery) {
            while (!(Boolean) executeJS(webDriver, "return jQuery.active == 0")) {
                pauseExecution(1);
                if (count > 5)
                    break;
                count++;
            }
        }
        return false;
    }

    public static void waitUntilJQueryReady(WebDriver webDriver) {
        JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
        Boolean jQueryDefined = (Boolean) executeJS(webDriver, "return typeof jQuery != 'undefined'");
        if (jQueryDefined) {
            pauseExecution(20L);
            waitForJQueryLoad(webDriver);
            pauseExecution(20L);
        }
    }

    /**
     * Used to wait for page to be loaded within the default time.
     *
     * @param webDriver : WebDriver Instance
     */
    public static void waitUntilJSReady(WebDriver webDriver) {
        try {
            JavascriptExecutor javascriptExecutor = getJavaScriptInstance(webDriver);
            ExpectedCondition<Boolean> jsLoad = driver -> javascriptExecutor
                    .executeScript("return document.readyState").toString().equals("complete");

            boolean jsReady = executeJS(webDriver, "return document.readyState").toString().equals("complete");
            if (!jsReady) {
                webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
                webDriverWait.until(jsLoad);
            }
        } catch (WebDriverException ignored) {
            logger.error("WebDriver Exception :--> " + ignored.getStackTrace());
        }
    }

    /**
     * Used to wait for page to be loaded within the specified time
     *
     * @param webDriver : WebDriver Instance
     */
    public static boolean waitForPageLoad(WebDriver webDriver) {
        boolean jsReady = false;
        try {
            jsReady = getPageLoadCondition(webDriver).equalsIgnoreCase("complete");
            if (jsReady)
                logger.info("Page is fully loaded...");

            else {
                for (int i = 0; i <= 20; i++) {
                    pauseExecution(500L);
                    jsReady = getPageLoadCondition(webDriver).equalsIgnoreCase("complete");
                    logger.info("Current page loading status : " + jsReady);
                    if (jsReady)
                        break;
                }
            }
        } catch (WebDriverException ignored) {
            logger.error("WebDriver Exception :--> " + ignored.getStackTrace());
        }

        return jsReady;
    }

    /**
     * Used to get page load strategy
     *
     * @param webDriver : WebDriver Instance
     * @return : String value as return type
     */
    public static String getPageLoadCondition(WebDriver webDriver) {
        return executeJS(webDriver, "return document.readyState").toString();
    }

    /**
     * Used to get page load strategy
     *
     * @param webDriver : WebDriver Instance
     * @param query     : String type as query
     * @return : String value as return type(completed, interactive & other states)
     */
    public static String getPageLoadCondition(WebDriver webDriver, String query) {
        return executeJS(webDriver, query).toString();
    }

    public static boolean waitForElementToBeDisplayed(WebDriver webDriver, WebElement webElement) {
        webDriverWait = getWebDriverWaitInstance(webDriver);
        ExpectedCondition<Boolean> elementDisplayed = arg0 -> {
            return webElement.isDisplayed();
        };
        return webDriverWait.until(elementDisplayed);
    }

    /**
     * Method for wait till element visibility of element
     *
     * @param webDriver     : WebDriver instance
     * @param webElement    : WebElement value
     * @param timeInSeconds : integer value in seconds
     */
    public static void waitForElementToBeVisible(WebDriver webDriver, WebElement webElement, int timeInSeconds) {
        logger.info("Waiting for Element to be visible :: " + webElement);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeInSeconds));
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        logger.info("WebElement visible successfully : " + webElement, true);
    }

    /**
     * Method for wait till element visibility of element
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement value
     */
    public static void waitUntilVisibilityOfElement(WebDriver webDriver, WebElement webElement) {
        logger.info("Waiting for Element to be visible :: " + webElement);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        logger.info("WebElement visible successfully : " + webElement, true);
    }

    public static void waitUntilVisibilityOfElementWithText(WebDriver webDriver, WebElement webElement) {
        logger.info("Waiting for Element to be visible and contain text :: " + webElement);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));

        // Wait until the element is visible and has non-empty text
        webDriverWait.until(driver -> {
            boolean isVisible = webElement.isDisplayed();
            String text = webElement.getText();
            logger.info("Element visibility: " + isVisible + ", Text: " + text);
            return isVisible && text != null && !text.trim().isEmpty();
        });

        logger.info("WebElement visible with text successfully: " + webElement.getText());
    }

    public static void waitUntilVisibilityOfElement(WebDriver webDriver, WebElement webElement, int time) {
        logger.info("Waiting for element to be visible :: " + webElement);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(time));
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        logger.info("Web element visible successfully : " + webElement, true);
    }

    /**
     * Used for wait for element to be invisible
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement
     * @return : boolean value based on the condition
     */
    public static boolean waitForElementToBeVisible(WebDriver webDriver, WebElement webElement) {
        boolean elementStatus;
        logger.info("Waiting for Element to be visible :: " + webElement, true);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
        elementStatus = webDriverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed();
        logger.info("Waited for locator to be visible: " + webElement, true);

        return elementStatus;
    }

    /**
     * Method for wait till element invisibility of element
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement value
     * @return : boolean value based on the condition
     */
    public static boolean waitForElementToBeInvisible(WebDriver webDriver, WebElement webElement) {
        logger.info("Waiting for Element to be Invisible :: " + webElement);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
        boolean elementStatus = webDriverWait.until(ExpectedConditions.invisibilityOf(webElement));
        logger.info("Waited for locator to be Invisible: " + webElement);

        return elementStatus;
    }

    /**
     * Used for wait for element to be invisible
     *
     * @param webDriver        : WebDriver instance
     * @param webElement       : WebElement
     * @param timeOutInSeconds : value of timeOutsInSeconds in seconds as integer value
     */
    public static void waitForElementToBeInvisible(WebDriver webDriver, WebElement webElement, int timeOutInSeconds) {
        logger.info("Waiting for Element to be Invisible :: " + webElement);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeOutInSeconds), Duration.ofMillis(POLLING_TIME));
        webDriverWait.until(ExpectedConditions.invisibilityOf(webElement));
        logger.info("Waited for locator to be Invisible: " + webElement);
    }

    public static void waitForElementToBeClickable(WebDriver webDriver, WebElement webElement) {
        logger.info("Waiting for element to be clickable :: " + webElement);
        long startTime = System.currentTimeMillis();
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logger.info("Finished waiting for locator to be clickable after " + totalTime + " milliseconds.");
    }

    public static void waitForElementToBeClickable(WebDriver webDriver, WebElement webElement, int timeOutInSeconds) {
        logger.info("Waiting for Element to be clickable --->> " + webElement);
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds, POLLING_TIME);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        logger.info("Waited for locator to be clickable --->> " + webElement);
    }
}
