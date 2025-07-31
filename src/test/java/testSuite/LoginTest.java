package testSuite;

import driver.BaseClass;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.Options;
import utils.SeleniumUtils;
import utils.WebDriverSessionManager;

import java.awt.*;


@Epic("Login Page")
@Tag("Login")
public class LoginTest extends BaseClass {
    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    LoginPage login;


    @BeforeMethod(alwaysRun = true)
    public void initializeDriver() {
        logger.info("Initializing the web-driver instance based on the things from the config file");
        startDriver();
        System.out.println("Driver session is --" + WebDriverSessionManager.getDriver());
        openBrowserWithBaseURL(WebDriverSessionManager.getDriver());
        login = new LoginPage(WebDriverSessionManager.getDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        SeleniumUtils.terminateWindow(WebDriverSessionManager.getDriver(), Options.QUIT);
    }

    @Test(description = "Verify the  user logged in successfully")
    public void validateUserLoginSuccessfully() throws InterruptedException, AWTException {
        logger.info("Verifying the user logged-In successfully");
        login.login(getValueFromProperties("adminUserName"), getValueFromProperties("adminPassword"));
        login.validateLoginSuccessMessage();
        logger.info("Verified the user logged-In successfully");
    }


    @Test(description = "Verify the add to card")
    public void validateAddToCard() throws InterruptedException, AWTException {
        logger.info("Verifying the  add to card ");
        login.login(getValueFromProperties("adminUserName"), getValueFromProperties("adminPassword"));
        login.clickOnAddToCardBackPack();
        login.clickOnAddToCardBoltTshirt();
        login.clickOnAddToCarD();
        login.validateBackPackProduct("Sauce Labs Backpack");
        login.validateTShirtProduct("Sauce Labs Bolt T-Shirt");
        login.validateQuantity();
        logger.info("Verified the add to card flow");
    }

    @Test(description = "Verify the checkout flow")
    public void validateCheckOutProcess() throws InterruptedException, AWTException {
        logger.info("Verifying the  checkout flow");
        login.login(getValueFromProperties("adminUserName"), getValueFromProperties("adminPassword"));
        login.clickOnAddToCardBackPack();
        login.clickOnAddToCardBoltTshirt();
        login.clickOnAddToCarD();
        login.clickOnCheckoutBtn();
        login.enterFirstName("John");
        login.enterLastName("Doe");
        login.enterZipCode("12345");
        login.clickOnContinue();
        login.validateSubTotalDisplay();
        login.validateSubTaxDisplay();
        login.validateTotal();
        login.clickOnFinish();
        login.validateSuccMsg("Thank you for your order!");
        logger.info("Verified the checkout flow");
    }



    @Test(description = "Verify the logout flow")
    public void validateLogoutFlow() throws InterruptedException, AWTException {
        logger.info("Verifying the  Logout flow ");
        login.login(getValueFromProperties("adminUserName"), getValueFromProperties("adminPassword"));
        login.clickOnMenuBtn();
        login.clickOnLogoutBtn();
        login.validateLoginBtn();
        logger.info("Verified the logout flow");
    }
}

