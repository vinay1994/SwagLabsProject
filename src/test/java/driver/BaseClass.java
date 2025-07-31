package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;
import utils.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class BaseClass {

    private static final String browser = GlobalVar.browserName;
    protected static WebDriver webDriver;
    public LoadProperty loadProperty;

    private static final Logger logger = LogManager.getLogger(BaseClass.class);

    @BeforeSuite(alwaysRun = true)
    public void initialization() {
        loadProperty = new LoadProperty();
        setBaseURLAndEnvironment();
    }

    public void startDriver() {
        String gridUrl = System.getenv("SELENIUM_GRID_URL");
        String executionMode = System.getenv("executionMode");
        GlobalVar.executionMode = (executionMode != null) ? executionMode.toLowerCase() : "local";
        logger.info("Execution mode is -----> " + GlobalVar.executionMode);

        ChromeOptions chromeOptions = getChromeOptionManager();
        if (GlobalVar.executionMode.equals("local")) {
            if ("chrome".equalsIgnoreCase(browser)) {
                webDriver = new ChromeDriver(chromeOptions);
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        } else if (GlobalVar.executionMode.equals("server")) {
            try {
                logger.info("Using Selenium Grid at URL: {}", gridUrl);
                if (gridUrl == null || gridUrl.isEmpty()) {
                    throw new IllegalArgumentException("SELENIUM_GRID_URL environment variable is not set.");
                }
                if ("chrome".equalsIgnoreCase(browser)) {
                    webDriver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
                } else {
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Selenium Grid URL: " + gridUrl, e);
            }
        } else {
            throw new IllegalArgumentException("Invalid execution mode: " + GlobalVar.executionMode);
        }

        logger.info("WebDriver Status Is -----> {}", webDriver);
        WebDriverSessionManager.setWebDriver(webDriver);
        WebDriverSessionManager.getDriver().manage().deleteAllCookies();
        WebDriverSessionManager.getDriver().manage().window().maximize();
        setupBrowserTimeouts(WebDriverSessionManager.getDriver());
    }


    public ChromeOptions getChromeOptionManager() {
        logger.info("Initializing the chrome browser instance ....");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2); // Pass the argument 1 to allow and 2 to block
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", GenericFunction.createFolderAndGetFolderPath(ConstantsData.DOWNLOAD_FILE_NAME));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--guest");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--ignore-ssl-errors=yes");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-save-password-bubble");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-features=PasswordManager,PasswordLeakDetection");
        if (GlobalVar.headless) {
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--headless=new");
        }
        if (GlobalVar.executionMode.equalsIgnoreCase("server")) {
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            //chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.setExperimentalOption("prefs", prefs);
        return chromeOptions;
    }

    private static void setupBrowserTimeouts (WebDriver driver) {


        driver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(50)); // Set page load timeout first

        driver.manage()
                .timeouts()
                .scriptTimeout(Duration.ofSeconds(200)); // Set script timeout next

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(20)); // Set implicit wait last

    }

    public void setBaseURLAndEnvironment() {
        System.out.println("Setting up base URL and all--->> ");
        if (!(System.getenv("environment") == null))
            GlobalVar.currentEnvironment = System.getenv("environment").toLowerCase();

        else if (System.getProperty("env.PATH") != null)
            GlobalVar.currentEnvironment = System.getProperty("env.PATH").toLowerCase();

        else {
            logger.info("This is the default value if the environment variable value is not specified ----> " + GlobalVar.currentEnvironment);
        }

        System.out.println("Curent Environemnt is --->>>>>"+GlobalVar.currentEnvironment);
        GlobalVar.propertyPath = GlobalVar.workingDirectory + "/src/test/resources/config/" + GlobalVar.currentEnvironment + ".properties";
        GlobalVar.baseURL = loadProperty.getValueFromPropertyFile(GlobalVar.propertyPath, "BASE_URL");
    }

    public String getValueFromProperties(String Key) {
        if (!(Key.isEmpty())) {
            return new LoadProperty().getValueFromPropertyFile(GlobalVar.propertyPath, Key);
        } else return "";
    }

    public void openBrowserWithBaseURL(WebDriver driver) {
        SeleniumUtils.openUrl(driver, new LoadProperty().getValueFromProperties("BASE_URL"));
    }

}