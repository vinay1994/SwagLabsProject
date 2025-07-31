package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.swing.text.Highlighter;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SeleniumUtils {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);
    private static WebDriverWait webDriverWait;
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int POLLING_TIME = 5;

    //**************************************** Navigation  **********************************************/

    /**
     * Used to launch the url
     *
     * @param webDriver : WebDriver Instance
     * @param url       : url as of type string
     */
    public static void openUrl(WebDriver webDriver, String url) {
        try {
            if (url == null) {
                logger.info("Url is null.. " + null);
                throw new Exception("url is null.. " + null);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to launch application due to " + null + " is not valid url.");
        }
        webDriver.get(url);
        SeleniumWait.waitForPageLoad(webDriver);
        maximizeWindow(webDriver, "maximize");
    }

    /**
     * Method used to redirect to specific url
     *
     * @param webDriver : WebDriver instance
     * @param url       : String type as url
     */
    public void redirectToPage(WebDriver webDriver, String url) {
        try {
            if (url != null) {
                webDriver.navigate().to(url);
                SeleniumWait.waitForPageLoad(webDriver);
            }
        } catch (Exception e) {
            logger.error("Unable to redirect to the page because of Url is not valid : " + url);
            throw new RuntimeException("Unable to redirect to the page because of Url is not valid : " + url);
        }
    }

    /**
     * Method to refresh the web page using java scripts
     *
     * @param webDriver : WebDriver Instance
     */
    public static void pageRefresh(WebDriver webDriver) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("history.go(0)");
    }

    /**
     * Method to refresh the web page using navigation method
     *
     * @param webDriver : WebDriver Instance
     */
    public static void pageRefreshUsingNavigation(WebDriver webDriver) {
        webDriver.navigate().refresh();
    }

    /**
     * Method to refresh the web page using navigation method
     *
     * @param webDriver : WebDriver Instance
     */
    public static void pageRefreshUsingNavigationTo(WebDriver webDriver) {
        webDriver.navigate().to(webDriver.getCurrentUrl());
    }

    /**
     * Method to refresh the web page using get method
     *
     * @param webDriver : WebDriver Instance
     */
    public static void pageRefreshUsingGetMethod(WebDriver webDriver) {
        webDriver.get(webDriver.getCurrentUrl());
    }

    /**
     * Method to refresh the web page using sendKeys method
     *
     * @param webDriver  : WebDriver Instance
     * @param webElement : WebElement
     */
    public static void pageRefreshUsingWebElement(WebDriver webDriver, WebElement webElement) {
        sendKeys(webDriver, webElement, Keys.F5);
    }

    public static void clearLocalStorage(WebDriver webDriver) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("localStorage.clear();");
        pageRefresh(webDriver);
    }

    /**
     * Method used to get the current page url
     *
     * @return : String as page url
     */
    public String getCurrentWebPageUrl(WebDriver webDriver) {
        logger.info("Get the Current Url : " + webDriver.getCurrentUrl());
        return webDriver.getCurrentUrl();
    }

    /**
     * Method used to get the page title of the current web page
     *
     * @return : String as title of the webpage
     */
    public String getCurrentWebPageTitle(WebDriver webDriver) {
        logger.info("Get the Page Title : " + webDriver.getTitle());
        return webDriver.getTitle();
    }

    public static String doGetTitleWithFraction(WebDriver webDriver, String titleFraction, int timeOutsInSeconds) {
        if (SeleniumWait.waitForTitleContains(webDriver, titleFraction, timeOutsInSeconds)) {
            return webDriver.getTitle();
        }
        return null;
    }

    public static String doGetTitle(WebDriver webDriver, String title, int timeOutsInSeconds) {
        if (SeleniumWait.waitForTitleToBe(webDriver, title, timeOutsInSeconds)) {
            return webDriver.getTitle();
        }
        return null;
    }

    public static String getWebPageSource(WebDriver webDriver) {
        return webDriver.getPageSource();
    }

    public static boolean isValuePresentOnPage(WebDriver webDriver, String value) {
        return getWebPageSource(webDriver).contains(value);
    }

    /**
     * This method is used to check either element select or not.
     *
     * @param webElement : WebElement type
     * @return : boolean value either true or false
     */
    public static boolean isElementSelected(WebDriver webDriver, WebElement webElement) {
        boolean isSelected = false;
        try {
            isSelected = (webElement.isDisplayed()) && (webElement.isSelected());
            highLightElement(webDriver, webElement);
            logger.info("Element is Selected : " + webElement + " status " + isSelected);
        } catch (NoSuchElementException ele) {
            logger.error("Element is not Selected : " + webElement + " status " + isSelected);
        }

        return isSelected;
    }

    /**
     * This method is used to check either element displayed or not.
     *
     * @param webElement : WebElement type
     * @return : boolean value either true or false
     */
    public static boolean isElementDisplayed(WebDriver webDriver, WebElement webElement) {
        boolean isDisplayed = false;
        try {
            isDisplayed = webElement.isDisplayed();
            highLightElement(webDriver, webElement);
            logger.info("Element is displayed : " + webElement + " status " + isDisplayed);
        } catch (NoSuchElementException ele) {
            logger.error("Element is not displayed : " + webElement + " status " + isDisplayed);
        }

        return isDisplayed;
    }

    /**
     * This method is used to check either element enabled or not.
     *
     * @param webElement : WebElement type
     * @return : boolean value either true or false
     */
    public static boolean isElementEnabled(WebElement webElement) {
        boolean isEnabled = false;
        try {
            isEnabled = webElement.isDisplayed() && webElement.isEnabled();
            logger.info("Element is enabled : " + webElement + " status " + isEnabled);
        } catch (ElementNotInteractableException ele) {
            logger.error("Element is not enabled : " + webElement + " status " + isEnabled);
        }

        return isEnabled;
    }

    /**
     * Used to verify either element is present.
     *
     * @param webElement : WebElement
     * @param webDriver  : WebDriver instance
     * @return : boolean value either true or false
     */
    public static boolean verifyElementIsPresent(WebDriver webDriver, WebElement webElement) {
        boolean elementVisibility = false;
        try {
            elementVisibility = isElementDisplayed(webDriver, webElement);
            logger.info("Element {%s} is present-->" + webElement);
        } catch (NoSuchElementException exception) {
            logger.info("Element {%s} is not present : ", webElement);
        }

        return elementVisibility;
    }

    /**
     * Used to verify either element is not present
     *
     * @param webElement : WebElement
     * @param webDriver  : WebDriver instance
     * @return : boolean value either true or false
     */
    public static boolean verifyElementIsNotPresent(WebDriver webDriver, WebElement webElement) {
        boolean status = false;
        try {
            if (!isElementDisplayed(webDriver, webElement)) {
                status = true;
                logger.info("Element is present : " + webElement + " status : " + true);
            }
        } catch (NoSuchElementException exception) {
            logger.error("Element is not present. " + exception.getMessage() + " status : " + status);
        } catch (NullPointerException exception) {
            logger.error("NullPointerException " + exception.getMessage());
        }

        return status;
    }

    public static void waitForElementToBeStale(WebDriver webDriver, WebElement webElement, int timeOutInSeconds) {
        logger.info("Waiting for element to be clickable :: " + webElement);
        logger.info("Waiting for element to be clickable :: " + webElement);
        long startTime = System.currentTimeMillis();
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeOutInSeconds), Duration.ofMillis(POLLING_TIME));
        webDriverWait.until(ExpectedConditions.stalenessOf(webElement));
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logger.info("Finished waiting for locator to be clickable after " + totalTime + " milliseconds.");
    }

    /**
     * Used to get the element status
     *
     * @param webDriver : WebDriver Instance
     * @param by        : Location type as By strategy
     * @return : boolean value based on the condition
     */
    public static boolean IsLocatorPresent(WebDriver webDriver, By by) {
        boolean isPresent = false;
        logger.info("Finding if element is present :: " + by.toString());
        try {
            if (findElements(webDriver, by).size() > 0) {
                isPresent = true;
                logger.info("Is element present :: " + isPresent);
            } else
                logger.info("Is element present :: " + isPresent);
        } catch (NoSuchElementException e) {
            logger.error("Element is not present " + by + " status " + isPresent);
        }

        return isPresent;
    }

    /**************************************** Click Operation ******************************************/

    public static void clickElementWhenReady(WebDriver webDriver, By locator, int timeOutInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public static void clickElementWhenReady(WebDriver webDriver, By locator, int timeOutInSeconds, int pollingEveryInMilliSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds, pollingEveryInMilliSeconds);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public static void clickElementWhenReady(WebDriver webDriver, WebElement webElement, int timeOutInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
    }

    public static void clickElementWhenReady(WebDriver webDriver, WebElement webElement, int timeOutInSeconds, int pollingEveryInMilliSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds, pollingEveryInMilliSeconds);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
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
        logger.info("Waiting for Element to be clickable :: " + webElement);
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutInSeconds, POLLING_TIME);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        logger.info("Waited for locator to be clickable: " + webElement);
    }

    /**
     * Used to click on the WebElement within specified timeout
     *
     * @param webDriver  : WebDriver Instance
     * @param webElement : WebElement value
     * @return : WebElement
     */
    public static WebElement elementToBeClickable(WebDriver webDriver, WebElement webElement) {
        // return (new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))).until(ExpectedConditions.elementToBeClickable(webElement));
        webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        ExpectedCondition<Boolean> elementIsClickable = arg0 -> {
            try {
                webElement.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        };
        webDriverWait.until(elementIsClickable);
        return webElement;
    }

    /**
     * Used to get the status of webElement either clickable or not
     *
     * @param webElement : webElement
     * @return : boolean value
     */
    public static boolean isElementClickable(WebElement webElement) {
        try {
            return webElement.isDisplayed() && webElement.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Used to click on the WebElement
     *
     * @param webElement : WebElement
     * @param webDriver  : WebDriver instance
     */
    public void click(WebDriver webDriver, final WebElement webElement) {
        try {
            webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT));
            elementToBeClickable(webDriver, webElement);
            logger.info("Element for click -> " + webElement.toString());
            highLightElement(webDriver, webElement);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
            webElement.click();
            logger.info("Element clicked -> " + webElement);
        } catch (Exception exception) {
            logger.error("Exception -> " + exception.getMessage());
            Assert.fail();
        }
    }

    public static void clickElement(WebDriver webDriver, WebElement webElement) {
        logger.info("Before performing the click operation check either element is displayed & enabled");
        if (isElementEnabled(webElement)) {
            scrollTillElementWithJS(webDriver, webElement);
            highLightElement(webDriver, webElement);
            webElement.click();
            logger.info("Click operation performed successfully : on " + webElement);
        } else {
            logger.error("Click operation is not performed : on " + webElement);
            throw new NoSuchElementException("Element " + webElement.getText() + " is not enable to performed the operation");
        }
    }

    public static void submitElement(WebDriver webDriver, WebElement webElement) {
        if (isElementEnabled(webElement)) {
            highLightElement(webDriver, webElement);
            webElement.submit();
            logger.info("Click operation performed successfully : on " + webElement);
        } else {
            logger.error("Click operation is not performed : on " + webElement);
            throw new NoSuchElementException("Element " + webElement.getText() + " is not enable to performed the operation");
        }
    }

    public static void clickElementByJS(WebDriver webDriver, WebElement webElement) {
        logger.info("Trying to click on the WebElement by JS");
        highLightElement(webDriver, webElement);
        JavascriptExecutor executor = SeleniumWait.getJavaScriptInstance(webDriver);
        logger.info("Clicking Locator by using JScript " + webElement);
        executor.executeScript("arguments[0].click();", webElement);
        logger.info("Element is clicked by JS :: " + webElement.toString());
    }

    public static void doubleClickByJs(WebDriver webdriver,WebElement webElement){
        JavascriptExecutor executor = SeleniumWait.getJavaScriptInstance(webdriver);
        executor.executeScript("arguments[0].click();", webElement);
       // executor.executeScript("arguments[0].click();", webElement);
    }
    public static void clickUsingStreamOp(List<WebElement> webElementList, String textElement) {
        if (webElementList.size() > 0)
            webElementList.stream().filter(res -> res.getText().equalsIgnoreCase(textElement)).findFirst().get().click();

        else
            System.out.println("Please enter the valid web-element list.. " + webElementList);
    }

    public static String getCssValue(WebDriver webDriver, WebElement webElement, String cssType) {
        return isElementDisplayed(webDriver, webElement) ? webElement.getCssValue(cssType) : null;
    }

    public static String getText(WebDriver webDriver, WebElement webElement) {
        String text = "";
        if (isElementDisplayed(webDriver, webElement))
            text = webElement.getText();
        logger.info("WebElement Text is : " + text);

        return text;
    }


    /**
     * Used to get the text & compare with the expected text
     *
     * @param webDrive   : WebDriver instance
     * @param webElement : WebElement
     * @param expected   : String value for matching
     */
    public static void getTextAndCompare(WebDriver webDrive, WebElement webElement, String expected) {
        try {
            String actual = getText(webDrive, webElement);
            logger.info("===========>>> " + actual);
            if (actual.contains(expected)) {
                logger.info("Actual text: " + actual + " contains expected: " + expected);
            } else {
                logger.error(
                        "Failed Step_Actual text " + actual + " not contains expected " + expected);
                Assert.fail();
            }
        } catch (Exception exception) {
            logger.error("Failed Step_getTextAndCompare with by and expected ---> " + exception.getMessage());
            Assert.fail();
        }
    }

    public static void clearEnterText(WebDriver webDriver, WebElement webElement, String inputText) {
        SeleniumWait.waitForExpectedElement(webDriver, webElement).clear();
        SeleniumWait.waitForExpectedElement(webDriver, webElement).sendKeys(inputText);
        highLightElement(webDriver, webElement);
    }

    public static void clearTextBox(WebDriver webDriver, WebElement webElement) {
        SeleniumWait.waitForExpectedElement(webDriver, webElement).clear();
        highLightElement(webDriver, webElement);
    }

    public static void clearValueUsingJS(WebDriver webDriver, String strClass) {
        JavascriptExecutor javascriptExecutor = SeleniumWait.getJavaScriptInstance(webDriver);
        javascriptExecutor.executeScript("document.getElementsByClassName('" + strClass + "')[1].innerTex='';");
    }

    public static void clearTextBoxValueUsingJS(WebDriver webDriver, Options options, String byValue) {
        String jsQuery = "";
        if (options != null && byValue != null) {

            switch (options) {
                case ID -> {
                    jsQuery = "return document.getElementById('" + byValue + "').value='';";
                    SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text-box field value is being cleared .. ");
                }
                case CLASSNAME -> {
                    jsQuery = "return document.getElementsByClassName('" + byValue + "')[0].value='';";
                    SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text-box field value is being cleared .. ");
                }
                case TAGNAME -> {
                    jsQuery = "document.getElementsByTagName('" + byValue + "')[0].value='';";
                    SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text-box field value is being cleared .. ");
                }
                case NAME -> {
                    jsQuery = "document.getElementsByName('" + byValue + "')[0].value='';";
                    SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text-box field value is being cleared .. ");
                }
                default ->
                        throw new IllegalArgumentException("Unable to clear the text-box value because of " + options + " and " + byValue);
            }
        } else
            logger.error("Operation could not be performed due to " + options + " and " + byValue);
    }

    /**
     * Used to enter the text in the text-box area
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement value
     * @param value      : String value need to enter
     */
    public static void sendKeys(WebDriver webDriver, WebElement webElement, String value) {
        try {
            if (isElementDisplayed(webDriver, webElement)) {
                logger.info("Typing input text :: " + value);
                //webElement.clear();
                webElement.sendKeys(value);
            }
        } catch (ElementNotInteractableException e) {
            logger.error("Unable to type input text because of element is not displayed : " + webElement);
        }
    }

    /**
     * Used to enter the text in the text-box area
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement value
     * @param key        : Key value
     */
    public static void sendKeys(WebDriver webDriver, WebElement webElement, Keys key) {
        try {
            if (isElementDisplayed(webDriver, webElement)) {
                logger.info("Sending keyboard command :: " + key);
                webElement.sendKeys(key);
            }
        } catch (ElementNotInteractableException e) {
            logger.error("Unable to type input text because element is not displayed : " + webElement);
        }
    }

    /**
     * Used to enter the text in the text-box area using JavaScript Executor
     *
     * @param webDriver  : WebDriver instance
     * @param webElement : WebElement value
     * @param text       : Value to be entered in the text-box
     */
    public static void sendKeysElementByJS(WebDriver webDriver, WebElement webElement, String text) {
        logger.info("Typing text by JS :: " + text);
        JavascriptExecutor javascriptExecutor = SeleniumWait.getJavaScriptInstance(webDriver);
        javascriptExecutor.executeScript("arguments[0].value=arguments[1];", webElement, text);
        logger.info(text + " value is entered by JS for WebElement :: " + webElement.toString());
    }

    public static WebElement findElement(WebDriver webDriver, final By findBy) {
        logger.info("Waiting for element :: " + findBy.toString());
        long startTime = System.currentTimeMillis();
        WebElement webElement = null;
        try {
            webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
            webElement = webDriverWait.until((driver) -> {
                logger.info("Trying to find element " + findBy, true);
                return driver.findElement(findBy);
            });

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            logger.info("Finished finding element after waiting for " + totalTime + " milliseconds.");
        } catch (Exception e) {
            logger.debug("Error while waiting for element ::" + findBy);
            try {
                throw new Exception("Error while waiting for element ::" + findBy);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return webElement;
    }

    public static List<WebElement> findElements(WebDriver webDriver, By findBy) {
        logger.info("Waiting for element :: " + findBy.toString());
        long startTime = System.currentTimeMillis();
        List<WebElement> webElementList = null;
        try {
            webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
            webElementList = webDriverWait.until((driver) -> {
                logger.info("Trying to find elements list " + findBy, true);
                return driver.findElements(findBy);
            });

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            logger.info("Finished finding elements list after waiting for " + totalTime + " milliseconds.");
        } catch (Exception e) {
            logger.debug("Error while waiting for element ::" + findBy);
            try {
                throw new Exception("Error while waiting for element ::" + findBy);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return webElementList;
    }

    public static WebElement findElement(WebDriver webDriver, WebElement parentElement, final By findBy) {
        logger.info("Waiting for element :: " + findBy.toString());
        long startTime = System.currentTimeMillis();
        WebElement webElement = null;
        try {
            webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
            webElement = webDriverWait.until((driver) -> {
                logger.info("Trying to find element " + parentElement.toString(), true);
                return parentElement.findElement(findBy);
            });

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            logger.info("Finished finding element after waiting for " + totalTime + " milliseconds.");
        } catch (Exception e) {
            logger.debug("Error while waiting for element ::" + findBy);
            new Exception("Error while waiting for element ::" + findBy);
        }
        return webElement;
    }

    public static List<WebElement> findElements(WebDriver webDriver, WebElement parentElement, By findBy) {
        logger.info("Waiting for element :: " + findBy.toString());
        long startTime = System.currentTimeMillis();
        List<WebElement> webElementList = null;
        try {
            webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(POLLING_TIME));
            webElementList = webDriverWait.until((driver) -> {
                logger.info("Trying to find elements list " + findBy, true);
                return parentElement.findElements(findBy);
            });

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            logger.info("Finished finding elements list after waiting for " + totalTime + " milliseconds.");
        } catch (Exception e) {
            logger.debug("Error while waiting for element ::" + findBy);
            new Exception("Error while waiting for element ::" + findBy);
        }

        return webElementList;
    }


    /**************************************** Action operation *****************************************/
    public static void clickElementByAction(WebDriver webDriver, WebElement webElement) {
        logger.info("Clicking element by Action :: " + webElement.toString());
        Actions actions = new Actions(webDriver);
        actions.click(webElement).build().perform();
        logger.info("Element is clicked :: " + webElement);
    }

    public static void sendKeysAndEnterByActionsClass(WebDriver driver, String value) {
        Actions action = new Actions(driver);
        action.sendKeys(value).sendKeys(Keys.ENTER).build().perform();
    }

    public static void moveToElementByActionsClass(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
    }

    public static void clickByActionsClass(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.click(webElement).build().perform();
    }

    public static void clickAndHoldByActionsClass(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.clickAndHold(webElement).build().perform();
    }

    public static void contextClickByActionsClass(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.contextClick(webElement).build().perform();
    }

    public static void doubleClickByActionsClass(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.doubleClick(webElement).build().perform();
    }

    public static void enterValueInTextFieldByActionClass(WebDriver driver, WebElement webElement, String value) {
        Actions action = new Actions(driver);
        action.sendKeys(webElement, value).perform();
        action.sendKeys(Keys.ENTER).perform();
    }

    public static void sendKeysByActionsClass(WebDriver driver, String value) {
        Actions action = new Actions(driver);
        action.sendKeys(value).build().perform();
    }

    public static void performKeyboardEnterActionClass(WebDriver driver) {
        Actions action = new Actions(driver);
        SeleniumWait.pauseExecution(1);
        action.sendKeys(Keys.ENTER).perform();
    }

    public static void performEnterFromKeyboardInActionsClass(WebDriver driver) {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ENTER).build().perform();
    }

    public static void performTabFromKeyboardInActionsClass(WebDriver driver) {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.TAB).build().perform();
    }

    public static void dragAndDropByActionsClass(WebDriver driver, WebElement sourceElement, WebElement targetElement) {
        Actions action = new Actions(driver);
        action.dragAndDrop(sourceElement, targetElement).perform();
    }

    public static String removeZeroFromNumberAfterDecimal(String number) {
        Double decimal = Double.parseDouble(number);
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(decimal);
    }

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) {
        TakesScreenshot scrShot;
        File srcFile;
        File destFile;
        try {
            scrShot = ((TakesScreenshot) webdriver);
            srcFile = scrShot.getScreenshotAs(OutputType.FILE);
            destFile = new File(fileWithPath);
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<WebElement> getAllOptionOfSelectDropDown(WebDriver driver, WebElement elementDD) {
        SeleniumWait.pauseExecution(3);
        if (!(elementDD.isDisplayed() && elementDD.isEnabled())) {
            SeleniumUtils.scrollTillElementWithJS(driver, elementDD);
        }
        SeleniumWait.pauseExecution(2);
        Select s = new Select(elementDD);
        SeleniumWait.pauseExecution(2);
        return s.getOptions();
    }

    public static void scrollDownByPixelWithJS(WebDriver webDriver, int distance) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("window.scrollBy(0, " + distance + ")");
    }

    public static void scrollBottomToTopWithJS(WebDriver webDriver) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("window.scrollTo(document.body.scrollHeight, 0);");
    }

    public static void scrollDownToBottomWithJS(WebDriver webDriver) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollTillElementWithJS(WebDriver webDriver, WebElement webElement) {
        JavascriptExecutor js = SeleniumWait.getJavaScriptInstance(webDriver);
        js.executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    //**************************************** Alert Operation ******************************************/

    /**
     * Method for wait till alert is present with default timeout value
     *
     * @param webDriver : WebDriver instance
     * @return : return Alert instance
     */
    public static Alert getAlertInstance(WebDriver webDriver) {
        WebDriverWait webDriverWait;
        Alert alert = null;
        try {
            webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver);
            alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
        } catch (NoAlertPresentException e) {
            e.getMessage();
        }

        return alert;
    }

    /**
     * Method for wait till alert is present
     *
     * @param webDriver         : WebDriver instance
     * @param timeOutsInSeconds : integer value in seconds
     * @return : return Alert instance
     */
    public static Alert getAlertInstance(WebDriver webDriver, int timeOutsInSeconds) {
        Alert alert = null;
        try {
            webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
            alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
        } catch (NoAlertPresentException e) {
            e.getMessage();
        }

        return alert;
    }

    public static Alert waitForAlert(WebDriver webDriver) {
        Alert alert = null;
        int i = 0;
        while (i++ < 5) {
            try {
                alert = webDriver.switchTo().alert();
                break;
            } catch (UnhandledAlertException e) {
                SeleniumWait.pauseExecution(1);
                continue;
            }
        }

        return alert;
    }

    /**
     * Used to accept the alert
     *
     * @param webDriver : WebDriver instance
     */
    public static void acceptAlert(WebDriver webDriver) {
        try {
            Alert alert = getAlertInstance(webDriver);
            if (alert != null)
                alert.accept();
        } catch (NoAlertPresentException e) {
            e.getMessage();
        }
    }

    /**
     * Used to dismiss the alert
     *
     * @param webDriver : WebDriver instance
     */
    public static void dismissAlert(WebDriver webDriver) {
        Alert alert;
        try {
            alert = getAlertInstance(webDriver);
            if (alert != null)
                alert.dismiss();
        } catch (NoAlertPresentException e) {
            e.getMessage();
        }
    }

    /**
     * Used to get the text from the alert
     *
     * @param webDriver : WebDriver instance
     * @return : string value as alert text
     */
    public String getAlertText(WebDriver webDriver) {
        String alertText = "";
        try {
            Alert alert = getAlertInstance(webDriver);
            if (alert != null) {
                alertText = alert.getText();
                logger.info("Alert message value is :--> " + alertText);
            }
        } catch (NoAlertPresentException e) {
            logger.error("Unable to get the Alert message :--> " + e.getMessage());
            e.getMessage();
        }

        return alertText;
    }

    /**
     * Used to high-light the element border with yellow color & background with yellow color
     *
     * @param driver     : WebDriver instance
     * @param webElement : element you want to highlight
     */
    public static void highLightElement(WebDriver driver, WebElement webElement) {
        if (GlobalVar.highlight)
            SeleniumWait.getJavaScriptInstance(driver).executeScript("arguments[0].setAttribute('style', 'background: gray; border: 2px solid red;');", webElement);
    }

    //**************************************** DropDown Operation ***************************************/
    public static void selectFromList(WebDriver webDriver, By by, String sValueToBeSelected) {
        sValueToBeSelected = sValueToBeSelected.toUpperCase();
        List<WebElement> elements = findElements(webDriver, by);
        String text = "";
        boolean flag = false;
        for (WebElement e : elements) {
            text = e.getText().toUpperCase();
            logger.info("Found :: " + text + " . Selecting value :: " + sValueToBeSelected, true);
            if (text.equalsIgnoreCase(sValueToBeSelected)) {
                logger.info("Match Found !!! Clicking value :: " + sValueToBeSelected, true);
                clickElement(webDriver, e);
                flag = true;
                break;
            }
        }
        if (!flag) {
            logger.error("No match found in the list for value-> " + sValueToBeSelected);
            try {
                throw new Exception("No match found in the list for value->" + sValueToBeSelected);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void selectValueFromDropDownUsingValue(WebDriver webDriver, By by, String valueToSelect) throws Exception {
        try {
            Select select = new Select(findElement(webDriver, by));
            if (select.getOptions().stream().map(WebElement::getText).anyMatch(t -> t.equals(valueToSelect))) {
                logger.info("Select Value from Dropdown using value :: " + valueToSelect + " with locator :: " + by, true);
                select.selectByValue(valueToSelect);
            }
        } catch (Exception e) {
            logger.error("Selecting Value :: " + valueToSelect + " from locator :: " + findElement(webDriver, by), false);
            throw new IllegalArgumentException("Unable to select value from DropDown due to " + valueToSelect);
        }
    }

    public static void selectValueFromDropDownUsingValue(WebElement webElement, String valueToSelect) {
        try {
            Select select = new Select(webElement);
            if (select.getOptions().stream().map(WebElement::getText).anyMatch(t -> t.equals(valueToSelect))) {
                logger.info("Selecting Value from dropdown using value :: " + valueToSelect + " with locator :: " + webElement.getText(), true);
                select.selectByValue(valueToSelect);
                System.out.println("Vinay kumar yadav ");
            }
        } catch (Exception e) {
            logger.error("Value could not be selected due to :: " + valueToSelect + " from locator :: " + webElement.getText(), false);
            throw new IllegalArgumentException("Unable to select value from DropDown due to " + valueToSelect);
        }
    }

    public static void selectValueFromDropDownUsingVisibleText(WebElement webElement, String valueToSelect) {
        try {
            Select select = new Select(webElement);
            logger.info("Selecting Value from dropdown using visible text:: " + valueToSelect + " from locator :: " + webElement.getText(), true);
            select.selectByVisibleText(valueToSelect);
        } catch (Exception e) {
            logger.error("Value could not be selected due to :: " + valueToSelect + " from locator :: " + webElement.getText(), false);
            throw new IllegalArgumentException("Unable to select value from DropDown due to " + valueToSelect);
        }
    }

    public static void selectValueFromDropDownUsingIndex(WebElement webElement, int indexValue) {
        try {
            Select select = new Select(webElement);
            select.selectByIndex(indexValue);
            //logger.info("Selecting Value from dropdown using index :: " + indexValue + " with locator :: " + webElement.getText(), true);
        } catch (Exception e) {
            logger.error("Value could not be selected due to :: " + indexValue + " with locator :: " + webElement.getText(), false);
            throw new IllegalArgumentException("Unable to select value from DropDown due to " + indexValue);
        }
    }

    public static String getSelectedValueFromDropdown(WebElement webElement) {
        String selectedValue = "";
        try {
            Select select = new Select(webElement);
            if (select.getFirstSelectedOption().getText().length() > 0) {
                selectedValue = select.getFirstSelectedOption().getText();
                logger.info("Selected value text is " + selectedValue + " for element " + webElement);
            }
        } catch (Exception e) {
            logger.error("Unable to get the text value from select dropdown " + selectedValue + " for element " + webElement.toString());
        }

        return selectedValue;
    }

    public static List<String> getAllSelectedValueFromDropdown(WebElement webElement) {
        List<String> allSelectedValue = new ArrayList<>();
        try {
            Select select = new Select(webElement);
            if (select.getAllSelectedOptions().size() >= 1) {
                allSelectedValue = select.getAllSelectedOptions().stream().map(te -> te.getText()).collect(Collectors.toList());
                logger.info("All Selected value text are as list " + allSelectedValue + " for element " + webElement);
            }
        } catch (Exception e) {
            logger.error("Unable to get the text value from select dropdown " + allSelectedValue + " for element " + webElement.toString());
        }

        return allSelectedValue;
    }

    //************************************************** Get Text **************************************************
    public List<String> getElementsTextList(WebDriver webDriver, By locator) {
        return findElements(webDriver, locator).stream().map(WebElement::getText).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<String> getElementsTextList(WebDriver webDriver, List<WebElement> webElementList) {
        return webElementList.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public static String getValueByJS(WebDriver webDriver, WebElement webElement) {
        String querySelector = "return arguments[0].text;";
        return (String) SeleniumWait.executeJS(webDriver, webElement, querySelector);
    }

    public static String getInnerTextUsingJS(WebDriver webDriver, WebElement webElement) {
        String querySelector = "return arguments[0].value;";
        return (String) SeleniumWait.executeJS(webDriver, webElement, querySelector);
    }

    public static String getTextValueUsingJS(WebDriver webDriver, Options options, String byValue) {
        String textBoxValue = "";
        String jsQuery = "";
        if (options != null && byValue != null) {
            switch (options) {
                case ID -> {
                    jsQuery = "return document.getElementById('" + byValue + "').value;";
                    textBoxValue = (String) SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text field value is " + textBoxValue);
                }
                case CLASSNAME -> {
                    jsQuery = "return document.getElementsByClassName('" + byValue + "')[0].value;";
                    textBoxValue = (String) SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text field value is " + textBoxValue);
                }
                case TAGNAME -> {
                    jsQuery = "document.getElementsByTagName('" + byValue + "')[0].value;";
                    textBoxValue = (String) SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text field value is " + textBoxValue);
                }
                case NAME -> {
                    jsQuery = "document.getElementsByName('" + byValue + "')[0].value;";
                    textBoxValue = (String) SeleniumWait.executeJS(webDriver, jsQuery);
                    logger.info("Text field value is " + textBoxValue);
                }
                default ->
                        throw new IllegalArgumentException("Unable to fetch the text box value because of " + options + " and " + byValue);
            }
        } else
            logger.error("Operation could not be performed due to " + options + " and " + byValue);

        return textBoxValue;
    }

    public static String getInnerTextUsingJS(WebDriver webDriver, WebElement webElement, Options locatorType) {
        String innerText = null;
        String querySelector;
        String elementValue = getAttributeValue(webDriver, webElement, locatorType.name().toLowerCase());

        switch (locatorType) {
            case ID:
                querySelector = "return document.getElementById('" + elementValue + "').innerText;";
                innerText = (String) SeleniumWait.executeJS(webDriver, querySelector);
                break;

            case CLASSNAME:
                // innerText = (String) javascriptExecutor.executeScript("return document.getElementsByClassName('" + elementValue + "')[0].innerText;");
                break;

            case NAME:
                //innerText = (String) javascriptExecutor.executeScript("return document.getElementsByName('" + elementValue + "')[0].innerText;");
                break;

            case TAGNAME:
                //innerText = (String) javascriptExecutor.executeScript("return document.getElementsByTagName('" + elementValue + "')[0].innerText;");
                break;

            default:
                throw new IllegalArgumentException("Unable to get the text because of ");
        }

        return innerText;
    }

    public static String getTextUsingJS(WebDriver webDriver, String cssOrXpathExpression, String type) {
        String innerText = "";
        String script = null;
        JavascriptExecutor javascriptExecutor = SeleniumWait.getJavaScriptInstance(webDriver);

        if (type.equalsIgnoreCase("css"))
            script = "var element = document.querySelector('" + cssOrXpathExpression + "');"
                    + "return element.textContent;";

        else if (type.equalsIgnoreCase("xpath"))
            script = "var element = document.evaluate('" + cssOrXpathExpression + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
                    + "return element.textContent;";

        innerText = (String) javascriptExecutor.executeScript(script);
        return innerText;
    }

    public static void setInnerText(WebDriver webDriver, WebElement webElement, String type, String value) {
        JavascriptExecutor javascriptExecutor = SeleniumWait.getJavaScriptInstance(webDriver);
        String elementValue = getAttributeValue(webDriver, webElement, type);

        if (type.equalsIgnoreCase("id"))
            javascriptExecutor.executeScript("document.getElementById('" + elementValue + "').innerText='" + value + "';");

        else if (type.equalsIgnoreCase("class"))
            javascriptExecutor.executeScript("document.getElementsByClassName('" + elementValue + "')[0].innerText='" + value + "';");

        else if (type.equalsIgnoreCase("name"))
            javascriptExecutor.executeScript("document.getElementsByName('" + elementValue + "')[0].innerText='" + value + "';");

        else if (type.equalsIgnoreCase("tagname"))
            javascriptExecutor.executeScript("document.getElementsByTagName('" + elementValue + "')[0].innerText='" + value + "';");
    }

    public static String getAttributeValue(WebDriver webDriver, final WebElement webElement, String value) {
        String strText = "";
        try {
            logger.info("Element for getAttribute -> " + webElement.toString());
            highLightElement(webDriver, webElement);
            strText = SeleniumWait.waitForExpectedElement(webDriver, webElement).getAttribute(value);
        } catch (Exception exception) {
            logger.info("Exception -> " + exception.getMessage());
            Assert.fail();
        }
        return strText;
    }

    public static String selectCalendarDate(String date, List<WebElement> webElementList) {
        String dayValue = date.substring(0, date.indexOf("/"));
        if (dayValue.startsWith("0"))
            dayValue = dayValue.replace("0", "");

        for (WebElement element : webElementList) {
            if (element.getText().equalsIgnoreCase(dayValue)) {
                element.click();
                break;
            }
        }

        return date;
    }

    /**************************************** Frame Related Operation ***********************************/
    public static void waitForFrameByNameOrId(WebDriver webDriver, String nameOrID, int timeOutsInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrID));
    }

    public static void waitForFrameByIndex(WebDriver webDriver, int frameIndex, int timeOutsInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
    }

    public static void waitForFrameByLocator(WebDriver webDriver, By frameLocator, int timeOutsInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
    }

    public static void waitForFrameByElement(WebDriver webDriver, WebElement webElement, int timeOutsInSeconds) {
        webDriverWait = SeleniumWait.getWebDriverWaitInstance(webDriver, timeOutsInSeconds);
        webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(webElement));
    }

    //**************************************** Window Handles Related OP ********************************/

    /**
     * Used to get the current window name
     *
     * @param webDriver : WebDriver Instance
     * @return : window name as string
     */
    public static String getWindowHandle(WebDriver webDriver) {
        String windowHandleName = null;
        try {
            SeleniumWait.waitForPageLoad(webDriver);
            windowHandleName = webDriver.getWindowHandle();
            logger.info("Current Window Handle Name is : " + windowHandleName);
        } catch (NoSuchWindowException e) {
            logger.error("Could not found window handle...");
            throw new NoSuchWindowException("Could not found window handles... " + windowHandleName);
        }

        return windowHandleName;
    }

    /**
     * Used to get the window counts
     *
     * @param webDriver : WebDriver Instance
     * @return : List of type string
     */
    public static List<String> getWindowHandles(WebDriver webDriver) {
        List<String> windowHandles = null;
        try {
            SeleniumWait.waitForPageLoad(webDriver);
            windowHandles = new ArrayList<>(webDriver.getWindowHandles());
            logger.info("Window Handles count : " + windowHandles.size());
        } catch (NoSuchWindowException e) {
            logger.error("Could not found window handles...");
            throw new NoSuchWindowException("Could not found window handles... " + windowHandles.size());
        }

        return windowHandles;
    }

    public static void switchTo(WebDriver webDriver, String windowNameOrWindowHandler) {
        webDriver.switchTo().window(windowNameOrWindowHandler);
        SeleniumWait.pauseExecution(2);
    }

    public static void switchTo(WebDriver webDriver, int windowIndex) {
        webDriver.switchTo().window(getWindowHandles(webDriver).get(windowIndex));
        SeleniumWait.pauseExecution(2);
    }


    /**
     * Used to switch to particular window based on the index
     *
     * @param webDriver          : WebDriver Instance
     * @param windowHandleNumber : Integer value of window number
     */
    public static void switchToWindow(WebDriver webDriver, int windowHandleNumber) {
        SeleniumWait.waitForPageLoad(webDriver);
        if (windowHandleNumber >= 0)
            switchTo(webDriver, windowHandleNumber);
        SeleniumWait.pauseExecution(2);
    }

    /**
     * Used to switch to particular window based on the index
     *
     * @param webDriver        : WebDriver Instance
     * @param windowHandleName : String value of window name
     */
    public static void switchToWindow(WebDriver webDriver, String windowHandleName) {
        SeleniumWait.waitForPageLoad(webDriver);
        if (windowHandleName != null)
            switchTo(webDriver, windowHandleName);
        SeleniumWait.pauseExecution(2);
    }

    public static void switchToParentMainWindow(WebDriver webDriver) {
        webDriver.switchTo().window(webDriver.getWindowHandle());
        SeleniumWait.pauseExecution(2);
    }

    public static void openNewWindow(WebDriver webDriver) {
        webDriver.switchTo().newWindow(WindowType.WINDOW);
        SeleniumWait.pauseExecution(1);
    }

    public static void openNewTabWindow(WebDriver webDriver) {
        webDriver.switchTo().newWindow(WindowType.TAB);
        SeleniumWait.pauseExecution(1);
    }

    /**
     * Used to maximize/minimize the window based upon the windowOpType value
     *
     * @param webDriver    : WebDriver Instance
     * @param windowOpType : windowOpType
     */
    public static void maximizeWindow(WebDriver webDriver, String windowOpType) {
        logger.info("Maximize the current window ");
        switch (windowOpType) {
            case "maximize" -> webDriver.manage().window().maximize();
            case "full" -> webDriver.manage().window().fullscreen();
            default ->
                    throw new IllegalArgumentException("Unable to find the implementation for BrowserOperation " + windowOpType);
        }
    }

    /**
     * Used to close/terminate window based upon the options value
     *
     * @param webDriver : WebDriver Instance
     * @param options   : options value as Enum
     */
    public static void terminateWindow(WebDriver webDriver, Options options) {
        if (options != null) {
            switch (options) {
                case CLOSE -> {
                    webDriver.close();
                    logger.info("Current Window close successfully " + options.name());
                }
                case QUIT -> {
                    webDriver.quit();
                    logger.info("Current driver instance assign to null " + options.name());
                }
                default -> {
                    logger.error("Unable to find the implementation for operation " + options.name());
                    throw new IllegalArgumentException("Unable to find the implementation for operation " + options.name());
                }
            }
        } else
            logger.error("Unable to find the implementation for operation " + options.name());
    }

    /**
     * Used to perform the browser navigation operation
     *
     * @param webDriver    : WebDriver instance
     * @param navigationOp : either forward or backward
     */
    public static void performBackwardUpwardOperation(WebDriver webDriver, String navigationOp) {
        switch (navigationOp) {
            case "forward" -> webDriver.navigate().forward();
            case "backward" -> webDriver.navigate().back();
            default ->
                    throw new IllegalArgumentException("Could not find the appropriate operation based on the " + navigationOp);
        }
    }

    public static boolean validateBrokenLinks(List<WebElement> anchorWebElementList) {
        int brokenLinksCount = 0;
        boolean isBroken = false;
        int responseCode;
        HttpURLConnection httpURLConnection;
        List<String> hrefList;

        try {
            hrefList = anchorWebElementList.stream().parallel().map(ele -> ele.getAttribute("href")).collect(Collectors.toList());
            for (String s : hrefList) {
                if (s == null || s.isEmpty()) {
                    System.out.println("Empty URL or an Un-configured URL------>" + s);
                    continue;
                }
                httpURLConnection = (HttpURLConnection) new URL(s).openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();

                if (responseCode >= 400 || responseCode >= 300) {
                    isBroken = true;
                    brokenLinksCount = brokenLinksCount + 1;
                    System.out.println(s + " is a broken link......");
                } else
                    System.out.println(s + " is a valid link");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("OverAll total broken links count are ======> " + brokenLinksCount);

        return isBroken;
    }


    public static List<WebElement> getListOfWebElements(WebDriver driver, String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        return elements;
    }

    public boolean checkElementExistenceInList(WebDriver driver, List<WebElement> elements, String value) {
        boolean isAvailable = false;
        isAvailable = GenericFunction.checkElementExistenceInList(driver, elements, value);
        return isAvailable;
    }

    public static void scrollDropDownListAndSelectParticularTextUsingJS(WebDriver driver, List<WebElement> options, String desiredText) {
        boolean optionFound = false;
        // Loop through all options to check if the desired text is visible and scroll if not
        for (int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);
            SeleniumWait.pauseExecution(1); // Pause for better UX or loading time
            // Scroll the current option into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            // Check if the option's text matches the desired text
            if (option.getText().trim().equals(desiredText)) {
                // Option found, click to select it
                option.click();
                optionFound = true;
                break;
            }
            // Scroll one last time if it's the last iteration and option still not found
            if (i == options.size() - 1 && !optionFound) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            }
        }
        // Optionally, throw an exception or log if the option was not found after scrolling
        if (!optionFound) {
            throw new NoSuchElementException("Desired option '" + desiredText + "' was not found in the dropdown.");
        }
    }
}