package pages;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import utils.Assertions;
import utils.SeleniumUtils;
import utils.SeleniumWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.ID, using = "user-name")
    WebElement username;

    @FindBy(how = How.ID, using = "password")
    WebElement password;

    @FindBy(how = How.ID, using = "login-button")
    static WebElement loginBtn;
    @FindBy(how = How.ID, using = "add-to-cart-sauce-labs-backpack")
    static WebElement sauce_labs_backpack;
    @FindBy(how = How.ID, using = "add-to-cart-sauce-labs-bolt-t-shirt")
    static WebElement sauce_labs_bolt_t_shirt;
    @FindBy(how = How.XPATH, using = "//a[@class='shopping_cart_link']")
    static WebElement addTCard;

    @FindBy(how = How.XPATH, using = "//div[text()='Sauce Labs Backpack']")
    static WebElement Backpack;
    @FindBy(how = How.XPATH, using = "//div[text()='Sauce Labs Bolt T-Shirt']")
    static WebElement Bolt_Tshirt;
    @FindBy(how = How.XPATH, using = "//div[@class='cart_quantity']")
    static List<WebElement> quantity;

    @FindBy(how = How.ID, using = "checkout")
    static WebElement checkoutBtn;

    @FindBy(how = How.XPATH, using = "//input[@id='first-name']")
    static WebElement firstNameTxt;
    @FindBy(how = How.XPATH, using = "//input[@id='last-name']")
    static WebElement lastNameTxt;
    @FindBy(how = How.XPATH, using = "//input[@id='postal-code']")
    static WebElement zipCodeTxt;
    @FindBy(how = How.XPATH, using = "//input[@id='continue']")
    static WebElement continueBtn;

    @FindBy(how = How.XPATH, using = "//button[@id='finish']")
    static WebElement finishBtn;

    @FindBy(how = How.XPATH, using = "//div[@class='summary_subtotal_label']")
    static WebElement subTotal;
    @FindBy(how = How.XPATH, using = "//div[@class='summary_tax_label']")
    static WebElement tax;
    @FindBy(how = How.XPATH, using = "//div[@class='summary_total_label']")
    static WebElement total;
    @FindBy(how = How.XPATH, using = "//h2[@class='complete-header']")
    static WebElement successMsg;
    @FindBy(how = How.XPATH, using = "//button[@id='react-burger-menu-btn']")
    static WebElement menuBtn;
    @FindBy(how = How.XPATH, using = "//a[@id='logout_sidebar_link']")
    static WebElement logoutBtn;








    public void validateSubTotalDisplay(){
        Assertions.assertTrue(subTotal.isDisplayed());
    }
    public void validateSubTaxDisplay(){
        Assertions.assertTrue(tax.isDisplayed());
    }

    public boolean validateTotal(){
        String sub=subTotal.getText();
       double  subtotal=Double.parseDouble(sub.substring(sub.indexOf("$")+1));
        String tax=subTotal.getText();
       double  t=Double.parseDouble(tax.substring(tax.indexOf("$")+1));
        String tota = total.getText();
        double  totalAmount=Double.parseDouble(tota.substring(tota.indexOf("$")+1));
        System.out.println(subtotal +" "+t+" "+totalAmount);
        if (totalAmount==subtotal+t){
            return true;
        }
        else {
            return false;
        }

    }

    @Step("Enter the Username")
    public void setUsername(String uname) {
        SeleniumWait.waitForElementToBeClickable(driver, username, 50);
        SeleniumUtils.sendKeys(driver, username, uname);
        logger.info("Enter the Username");
    }

    @Step("Enter the Password")
    public void setPassword(String pass) {
        SeleniumUtils.sendKeys(driver, password, pass);
    }

    @Step("Submit the credential")
    public void clickOnLoginButton() {
        SeleniumWait.waitForElementToBeClickable(driver,loginBtn);
        SeleniumUtils.clickElement(driver, loginBtn);
    }

    @Step("Login into the application")
    public void login(String uName, String password) throws InterruptedException, AWTException {
        setUsername(uName);
        setPassword(password);
        clickOnLoginButton();

    }

    @Step("Login into the application")
    public void validateLoginSuccessMessage() {
        if (driver.getCurrentUrl().contains("inventory.html")) {
            Assertions.assertTrue(true);
        } else {
            Assertions.assertTrue(false, "There was error while logged in!!");
        }
    }

    public void clickOnCheckoutBtn() {
        SeleniumWait.waitForElementToBeClickable(driver,checkoutBtn);
        SeleniumUtils.clickElement(driver, checkoutBtn);
    }

    public void clickOnMenuBtn() {
        SeleniumUtils.scrollBottomToTopWithJS(driver);
        SeleniumWait.waitForElementToBeClickable(driver,menuBtn);
        SeleniumUtils.clickElement(driver, menuBtn);
    }

    public void clickOnLogoutBtn() {
        SeleniumWait.waitForElementToBeClickable(driver,logoutBtn);
        SeleniumUtils.clickElement(driver, logoutBtn);
    }
    public void clickOnAddToCardBackPack() {
        SeleniumWait.waitForElementToBeClickable(driver,sauce_labs_backpack);
        SeleniumUtils.clickElement(driver, sauce_labs_backpack);
    }
    public void clickOnAddToCardBoltTshirt() {
        SeleniumWait.waitForElementToBeClickable(driver,sauce_labs_bolt_t_shirt);
        SeleniumUtils.scrollTillElementWithJS(driver,sauce_labs_bolt_t_shirt);
        SeleniumUtils.clickElement(driver, sauce_labs_bolt_t_shirt);
    }
    public void clickOnAddToCarD() {
        SeleniumWait.waitForElementToBeClickable(driver,addTCard);
        SeleniumUtils.scrollBottomToTopWithJS(driver);
        SeleniumUtils.clickElement(driver, addTCard);
    }
    public void clickOnContinue() {
        SeleniumWait.waitForElementToBeClickable(driver,continueBtn);
        SeleniumUtils.scrollTillElementWithJS(driver,continueBtn);
        SeleniumUtils.clickElement(driver, continueBtn);
    }
    public void clickOnFinish() {
        SeleniumWait.waitForElementToBeClickable(driver,finishBtn);
        SeleniumUtils.scrollTillElementWithJS(driver,finishBtn);
        SeleniumUtils.clickElement(driver, finishBtn);
    }
    public void validateBackPackProduct(String pname) {
        Assertions.assertEquals(Backpack.getText(),pname);
    }

    public void validateLoginBtn() {
       Assertions.assertTrue(loginBtn.isDisplayed());
    }

    public void validateTShirtProduct(String pname) {
        Assertions.assertEquals(Bolt_Tshirt.getText(),pname);
    }
    public void validateSuccMsg(String msg) {
        Assertions.assertEquals(successMsg.getText(),msg);
    }

    public void validateQuantity() {

        for(int i=0; i<quantity.size(); i++) {
    if (quantity.get(i).getText().equals("1")) {
        Assertions.assertTrue(true);
    } else {
        Assertions.assertTrue(false, "Quantity is not  1");
    }
}
    }



    public void enterFirstName(String firstName) {
        firstNameTxt.clear();
        firstNameTxt.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameTxt.clear();
        lastNameTxt.sendKeys(lastName);
    }

    public void enterZipCode(String zipCode) {
        zipCodeTxt.clear();
        zipCodeTxt.sendKeys(zipCode);
    }


}