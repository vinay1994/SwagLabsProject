package utils;

import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openqa.selenium.logging.LogEntry;

import static utils.SeleniumWait.logger;

public class GenericFunction {

    /**************************************** Time Related Op ******************************************/
    public static HashMap<String, String> getCurrentDateAndTime() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String dateAndTime = new SimpleDateFormat("yyyy/MM/dd h:mm a").format(new Date());
        hashMap.put("todayDate", dateAndTime.split(" ")[0].trim().split("/")[2].trim());
        hashMap.put("month", dateAndTime.split(" ")[0].trim().split("/")[1].trim());
        hashMap.put("year", dateAndTime.split(" ")[0].trim().split("/")[0].trim());
        hashMap.put("todayTime", dateAndTime.split(" ")[1].trim());
        hashMap.put("AM/PM", dateAndTime.split(" ")[2].trim());

        return hashMap;
    }

    public static String getTodayDate(String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        Date d = null;
        try {
            d = inputFormat.parse(date);
            SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy");
            output.format(d);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return d.toString();
    }


    public static String getCurrentDate(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formatedDate = now.format(formatter);
        return formatedDate;
    }


    public static String getDateBasedOnFormat(String format) {
        String dateAndTime = null;
        if (format != null) {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date date = new Date();
            dateAndTime = dateFormat.format(date);
        }

        return dateAndTime;
    }

    public static String getFutureDate(String format, int noOfDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(noOfDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = futureDate.format(formatter);
        System.out.println("Future date: " + formattedDate);
        return formattedDate;
    }
    public static String getFormattedDateWithHours(String format,int minToAdd) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updatedTime = now.plusMinutes(minToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return updatedTime.format(formatter);
    }


    // ******************** Dynamic Data Generation By Faker API ***************************
    public static String firstName() {
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static String firstName(String localType) {
        Faker faker = new Faker(new Locale(localType));
        return faker.name().firstName();
    }

    public static String lastName() {
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static String emailAddress() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }

    public static String emailAddressWithInternet() {
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }

    public static String signatureId() {
        Faker faker = new Faker();
        return "Sig" + faker.random().hex(9).toLowerCase();
    }

    public static String avatarUrl() {
        Faker faker = new Faker();
        return faker.internet().avatar();
    }

    public static String authorId() {
        String authorId;
        Faker faker = new Faker();
        authorId = "Author" + faker.idNumber().valid().replace("-", "");

        return authorId;
    }

    public static String authorName() {
        String authorName;
        Faker faker = new Faker();
        authorName = faker.book().author();

        return authorName;
    }

    public static String bookName() {
        String bookName;
        Faker faker = new Faker();
        bookName = "Aut" + getString(faker.book().title());
        bookName = bookName.length() > 22 ? bookName.substring(0, 20) : bookName;
        return bookName.replace(" ", "");
    }

    public static String resourceName() {
        String bookName;
        Faker faker = new Faker();
        bookName = "Resource" + getString(faker.book().title());
        return bookName.replace(" ", "");
    }

    public static String metaDataName() {
        Faker faker = new Faker();
        return "MetaData" + getString(faker.book().title());
    }

    public static String description() {
        Faker faker = new Faker();
        return faker.book().genre();
    }

    public static String publisher() {
        Faker faker = new Faker();
        return faker.book().publisher();
    }

    public static String edition() {
        String[] str = {"First Edition", "Second Edition", "Third Edition", "Fourth Edition",
                "Fifth Edition", "Sixth Edition", "Seventh Edition"};
        return str[generateRandomNumber(0, str.length)];
    }

    public static String removeSpecialCharacterFromString(String inputString) {
        String str = null;
        if (inputString != null) {
            str = inputString.chars()
                    .mapToObj(c -> Character.toString((char) c))
                    .filter(s -> s.matches("[a-zA-Z0-9 ]+"))
                    .collect(Collectors.joining());

            System.out.println("Original string: " + inputString);
            System.out.println("Result string: " + str);
        }

        return str;
    }

    public static String getString(String inputString) {
        String str = null;
        if (inputString != null) {
            str = inputString.chars()
                    .mapToObj(c -> Character.toString((char) c))
                    .filter(s -> s.matches("[a-zA-Z ]+"))
                    .collect(Collectors.joining());
        }

        return str;
    }

    public static int getPageCountFromStr(String inputString) {
        int number = 0;
        List<Integer> numbersList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            numbersList.add(Integer.parseInt(matcher.group()));
        }

        if (numbersList.get(0) == numbersList.get(1))
            number = numbersList.get(0);
        else
            number = numbersList.get(1);

        return number;
    }

    public static String capitalizeFully(String string) {

        if (string == null || string.isEmpty())
            return null;

        return Arrays.stream(string.split("_")).map(str ->
                str.substring(0, 1).toUpperCase() + str.substring(1)
                        .toLowerCase()).collect(Collectors.joining(" "));
    }

    public static String capitalizeFully(String string, String splitBy) {

        if (string == null || string.isEmpty())
            return null;

        return Arrays.stream(string.split(splitBy)).map(str ->
                str.substring(0, 1).toUpperCase() + str.substring(1)
                        .toLowerCase()).collect(Collectors.joining(" "));
    }

    public static String removeAllDigitUsingStream(String str) {
        return Arrays.stream(str.split("\\s+"))
                .filter(s -> !s.matches("\\d+"))
                .collect(Collectors.joining(" "));
    }

    public static String removeAllDigitUsingChars(String str) {
        return str.chars()
                .filter(c -> !Character.isDigit(c))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().replaceAll("  ", " ").trim();
    }

    public static String removeAllDigitUsingRegex(String str) {
        return str.replaceAll("\\d", "").replaceAll("  ", " ").trim();
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static int generateRandomNumber(int maxSize) {
        int number = 0;
        Random random;
        random = new Random();
        if (maxSize > 0)
            number = random.nextInt(maxSize);

        return number;
    }

    public static int generateRandomNumber(int minSize, int maxSize) {
        Random random = new Random();
        return random.ints(minSize, maxSize)
                .findFirst()
                .getAsInt();
    }

    public static long generateRandomOf13Digit() {
        Random random = new Random();
        long randomNumber = random.nextLong() % 10000000000000L;
        if (randomNumber < 0) {
            randomNumber *= -1;
        }

        return randomNumber;
    }

    public static String generateRandomAlphaNumeric(int length) {
        Random random = new Random();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        return random.ints(length, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static String generateRandomString(int length) {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        return random.ints(length, 0, alphabet.length())
                .mapToObj(alphabet::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static String currentTimeInUTCFormat() {
        LocalDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC);
        // Create a DateTimeFormatter object for the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return dateTime.format(formatter);
    }

    public static void uploadFile(WebDriver driver, WebElement element, String fileName) {
        if (fileName != null && SeleniumUtils.isElementDisplayed(driver, element)) {
            LocalFileDetector localFileDetector = new LocalFileDetector();
            ((RemoteWebDriver) driver).setFileDetector(localFileDetector);
            File file = new File(GlobalVar.workingDirectory + fileName);
            element.sendKeys(file.getAbsolutePath());
        }
    }

    public static boolean checkDigitInStr(String str) {
        return str.matches(".*\\d.*");
    }

    public static String getColorCode(WebDriver webDriver, WebElement webElement, String cssTypeValue) {
        Color color = Color.fromString(SeleniumUtils.getCssValue(webDriver, webElement, cssTypeValue));

        return color.asHex();
    }

    public static void getFileName() {
        File file = new File(GlobalVar.workingDirectory + GlobalVar.slash + "testData");

        String fileName = Arrays.stream(Objects.requireNonNull(file.listFiles())).filter(fil -> fil.getName().contains(".png")).findFirst().get().getName();
        System.out.println(fileName);
    }

    public static String getFileName(String folderName, String fileType) {
        String fileName;
        int randomNumber = generateRandomNumber(100, 1000);

        File file = new File(GlobalVar.workingDirectory + GlobalVar.slash + folderName);
        fileName = Arrays.stream(Objects.requireNonNull(file.listFiles())).filter(fi -> fi.getName().contains(fileType)).findFirst().get().getName();

        File olderFile = new File(GlobalVar.workingDirectory + GlobalVar.slash + folderName + GlobalVar.slash + fileName);

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1)
            fileName = fileName.substring(dotIndex + 1);
        fileName = fileName + randomNumber + "." + fileType;

        File newFile = new File(GlobalVar.workingDirectory + GlobalVar.slash + folderName + GlobalVar.slash + fileName);

        if (olderFile.renameTo(newFile)) ;

        return fileName;
    }

    public static void removeFilesFromFolder(String folderName, String fileExtension) {
        File file = new File(GlobalVar.workingDirectory + GlobalVar.slash + folderName + GlobalVar.slash);
        if (file.exists()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                if (f.getName().contains(fileExtension))
                    f.delete();
            }
        }
    }


    public static String createFolderAndGetFolderPath(String folderName) {
        File file = new File(GlobalVar.workingDirectory + GlobalVar.slash + folderName + GlobalVar.slash);
        if (!file.exists()) {
            file.mkdir();
        }
        return GlobalVar.workingDirectory + GlobalVar.slash + folderName;
    }

    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }


//    public static void uploadFileByRobot(WebDriver driver, WebElement element, String filePath) throws Exception {
//        //    SeleniumUtils.clickElementByJS(driver, element);
////            SeleniumWait.waitForElementToBeClickable(driver,element);
//        element.click();
//        File folder = new File(GlobalVar.workingDirectory + GlobalVar.slash + ConstantsData.DOWNLOAD_FILE_NAME + GlobalVar.slash + filePath);
//        String absolutePath = folder.getAbsolutePath();
//        System.out.println(absolutePath);
//        Robot robot = new Robot();
//        StringSelection selection = new StringSelection(absolutePath);
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(selection, null);
//        robot.delay(2000);
//        robot.keyPress(KeyEvent.VK_CONTROL);
//        robot.keyPress(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_CONTROL);
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.keyRelease(KeyEvent.VK_ENTER);
//        SeleniumWait.pauseExecution(5);
////        //    SeleniumUtils.scrollTillElementWithJS(driver, uploadSaveBtn);
////            SeleniumWait.pauseExecution(10);
////            SeleniumUtils.clickElementByJS(driver, uploadSaveBtn);
////            SeleniumWait.pauseExecution(30);
//    }


//        public static void uploadFileByRobot(WebDriver driver, WebElement element, String fileName) throws Exception {
//            // Ensure the trigger element is ready
//            SeleniumWait.waitUntilVisibilityOfElement(driver, element);
//            // SeleniumUtils.clickElementByJS(driver, element); // Optional, not needed for sendKeys()
//
//            // Construct the file path
//            String filePath = GlobalVar.workingDirectory + GlobalVar.slash + ConstantsData.DOWNLOAD_FILE_NAME + GlobalVar.slash + fileName;
//            System.out.println("Uploading file from: " + filePath);
//
//            // Find the file input within the element
//            WebElement uploadFile;
//            try {
//                uploadFile = element.findElement(By.id("fileElem"));
//                System.out.println("Found file input: " + uploadFile.getAttribute("id"));
//            } catch (NoSuchElementException e) {
//                System.out.println("fileElem not found within element, falling back to generic search...");
//                uploadFile = waitUntilElementLocated(driver, By.cssSelector("input[type='file']"), 10);
//                System.out.println("Found fallback file input: " + uploadFile.getAttribute("id"));
//            }
//
//            // Ensure the input is interactable and trigger onchange
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible';", uploadFile);
//            uploadFile.sendKeys(filePath);
//            System.out.println("File path sent to input");
//
//            // Trigger onchange event manually
//            js.executeScript("arguments[0].dispatchEvent(new Event('change'));", uploadFile);
//            System.out.println("Triggered onchange event");
//
//            // Fallback: Submit the form if fileSelect() doesn’t handle it
//            try {
//                WebElement uploadForm = driver.findElement(By.id("uploadForm"));
//               // uploadForm.submit();
//                System.out.println("Form submitted explicitly");
//            } catch (NoSuchElementException e) {
//                System.out.println("No uploadForm found, assuming fileSelect() handles submission");
//            }
//    }

//    public static void uploadFileByRobot(WebDriver driver, WebElement element, String fileName) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//
//        try {
//            // 1. File validation
//            Path filePath = Paths.get(GlobalVar.workingDirectory, ConstantsData.DOWNLOAD_FILE_NAME, fileName);
//            System.out.println("Absolute file path: " + filePath.toAbsolutePath());
//
//            if (!Files.exists(filePath)) {
//                throw new RuntimeException("File not found: " + filePath);
//            }
//            if (Files.size(filePath) == 0) {
//                throw new RuntimeException("Empty file: " + filePath);
//            }
//
//            // 2. Element interaction
//            wait.until(ExpectedConditions.visibilityOf(element));
//            captureState(driver, "pre-upload");
//
//            // 3. File input handling
//            WebElement fileInput = wait.until(d -> {
//                try {
//                    return element.findElement(By.cssSelector("input[type='file']"));
//                } catch (NoSuchElementException e) {
//                    return driver.findElement(By.cssSelector("input[type='file']"));
//                }
//            });
//
//            // 4. JavaScript preparation
//            ((JavascriptExecutor) driver).executeScript(
//                    "arguments[0].style.cssText = 'display:block !important; visibility:visible !important; height:auto !important;';",
//                    fileInput
//            );
//
//            // 5. File submission
//            fileInput.sendKeys(filePath.toString());
//            System.out.println("File input value after sendKeys: " + fileInput.getAttribute("value"));
//
//            // 6. Wait for client-side validation
//            wait.until(d -> !fileInput.getAttribute("value").isEmpty());
//
//            // 7. Controlled submission
//            try {
//                SeleniumWait.pauseExecution(5);
//                ((JavascriptExecutor) driver).executeScript(
//                        "arguments[0].closest('form').dispatchEvent(new Event('submit', {bubbles: true}));",
//                        fileInput
//                );
//                ((JavascriptExecutor) driver).executeScript(
//                        "arguments[0].closest('form').dispatchEvent(new Event('submit', {bubbles: true}));",
//                        fileInput
//                );
//                SeleniumWait.pauseExecution(5);
//            } catch (JavascriptException e) {
//                //  fileInput.submit();
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Helper methods
//    private static void captureState(WebDriver driver, String prefix) {
//        // Capture screenshot
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        screenshot.renameTo(new File(prefix + "-" + System.currentTimeMillis() + ".png"));
//
//        // Capture page source
//        try (FileWriter writer = new FileWriter(prefix + "-page-source.html")) {
//            writer.write(driver.getPageSource());
//        } catch (IOException ignored) {}
//    }
//
//    private static void analyzeNetworkRequests(WebDriver driver) {
//        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);
//        logs.getAll().stream()
//                .map(LogEntry::getMessage)
//                .filter(msg -> msg.contains("Network.requestWillBeSent") || msg.contains("Network.responseReceived"))
//                .forEach(msg -> System.out.println("NETWORK: " + msg));
//    }
//
//    private static void analyzePageContent(WebDriver driver) {
//        // Check for hidden error messages
//        List<WebElement> hiddenErrors = driver.findElements(By.cssSelector(
//                "input[type='hidden'][name*='error'], .error[style*='hidden']"
//        ));
//        hiddenErrors.forEach(error ->
//                System.out.println("HIDDEN ERROR: " + error.getAttribute("value"))
//        );
//
//        // Check HTTP status
//        String pageSource = driver.getPageSource();
//        if (pageSource.contains("HTTP Status 4") || pageSource.contains("HTTP Status 5")) {
//            System.out.println("SERVER ERROR DETECTED IN PAGE SOURCE");
//        }
//    }



    public static void uploadFileWithoutDialog(WebDriver driver, WebElement element, String fileName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // 1. File validation
            Path filePath = Paths.get(GlobalVar.workingDirectory, ConstantsData.DOWNLOAD_FILE_NAME, fileName);
            System.out.println("Absolute file path: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + filePath);
            }
            if (Files.size(filePath) == 0) {
                throw new RuntimeException("Empty file: " + filePath);
            }

            // 2. Wait for parent element visibility
            wait.until(ExpectedConditions.visibilityOf(element));
            // 3. Locate file input
            WebElement fileInput = wait.until(d -> {
                try {
                    return element.findElement(By.cssSelector("input[type='file']"));

                } catch (NoSuchElementException e) {
                     return driver.findElement(By.cssSelector("input[type='file']"));

                }
            });

            // 4. Make input interactable
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.cssText = 'display:block !important; visibility:visible !important;';",
                    fileInput
            );

            // 5. Perform file upload
            fileInput.sendKeys(filePath.toString());
            try {
              // Wait for standard value population
                wait.until(d -> !fileInput.getAttribute("value").isEmpty());
            } catch (TimeoutException e) {
                // Fallback: Wait for files property
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(d -> (Boolean) ((JavascriptExecutor) d)
                                .executeScript("return arguments[0].files.length > 0;", fileInput));
            }

            // Add small safety delay
            try {
                Thread.sleep(8000); // 0.5 second
            } catch (InterruptedException ignored) {}

            // Trigger events with retry logic
            ((JavascriptExecutor) driver).executeScript(
                    "var element = arguments[0];" +
                            "function triggerWithRetry(retries) {" +
                            "if(retries <= 0) return;" +
                            "if(element.files.length > 0) {" +
                            "element.dispatchEvent(new InputEvent('input', {bubbles: true}));" +
                            "element.dispatchEvent(new Event('change'));" +
                            "} else {" +
                            "setTimeout(() => triggerWithRetry(retries-1), 300);" +
                            "}" +
                            "}" +
                            "triggerWithRetry(5);", // Max 5 retries = 1.5 seconds
                    fileInput
            );


        } catch (Exception e) {
            System.out.println("File not uploaded beacuse of "+e);
        }
    }

        public static WebElement waitUntilElementLocated(WebDriver driver, By locator, int timeoutInSeconds) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        }

    public static int convertStrToInt(String stringValue) {
        int intValue = 0;
        if (stringValue != null)
            intValue = Integer.parseInt(stringValue);

        return intValue;
    }

    public static void selectElementFromDD(WebDriver driver, String elementType, WebElement elementDD, List<WebElement> elementList) {
        SeleniumWait.pauseExecution(3);
        SeleniumUtils.scrollTillElementWithJS(driver, elementDD);
        elementDD.click();
        SeleniumWait.pauseExecution(2);
        for (WebElement element : elementList) {
            if (element.getText().equals(elementType)) {
                SeleniumWait.waitUntilVisibilityOfElement(driver, element);
                SeleniumUtils.clickElement(driver, element);
                break;
            }
        }
    }

    public static void selectElementFromDDJS(WebDriver driver, String elementType, WebElement elementDD, List<WebElement> elementList) {
        SeleniumWait.pauseExecution(1);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        elementDD.click();
        SeleniumWait.pauseExecution(3);
        for (WebElement element : elementList) {
            if (element.getText().equals(elementType)) {
                js.executeScript("arguments[0].click()", element);
                SeleniumUtils.clickElement(driver, element);
                break;
            }
        }
    }

    public static void selectElementFromDDJS(WebDriver driver, int index, WebElement elementDD, List<WebElement> elementList) {
        SeleniumWait.pauseExecution(3);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        elementDD.click();
        SeleniumWait.pauseExecution(3);
        for (int i = 1; i < elementList.size(); i++) {
            if (i == index) {
                js.executeScript("arguments[0].click()", elementList.get(i));
                SeleniumUtils.clickElement(driver, elementList.get(i));
                break;
            }
        }
    }

    public static void checkElementExistence(WebDriver driver, List<WebElement> elementList, String elementType) {
        boolean isAvailable = false;
        for (WebElement element : elementList) {
            if (element.getText().equals(elementType)) {
                logger.info("Element " + elementType + " is found successfully");
                isAvailable = true;
                Assertions.assertTrue(true, "Element " + elementType + " is found in List");
                break;
            }
        }
        if (!isAvailable) {
            Assertions.assertTrue(false, "Element " + elementType + " is not found in List");
        }
    }

    public static boolean checkElementExistenceInList(WebDriver driver, List<WebElement> elementList, String elementType) {
        boolean isAvailable = false;
        for (WebElement element : elementList) {
            if (element.getText().equals(elementType)) {
                logger.info("Element " + elementType + " is found successfully");
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }

    public static String getSelectQuery(String catlogName, String schemaName, String tableName, String columnName, String ColumnValue) {
        return "Select * From " + catlogName + "." + schemaName + "." + tableName + " " + "where " + columnName + "=" + ColumnValue;
    }

    public static String getSelectQueryWithLimit(String catlogName, String schemaName, String tableName, String limitValue) {
        return "Select * From " + catlogName + "." + schemaName + "." + tableName + " " + "LIMIT " + limitValue;
    }

    public static String makeQuery(String queryType, String catlogName, String schemaName, String tableName) {
        String query = null;
        switch (queryType) {
            case "select":
                query = "select * from " + catlogName + "." + schemaName + "." + tableName;
                break;
            case "update":
                query = "";
                break;
            case "delete":
                query = "";
                break;
            default:
                throw new IllegalArgumentException("Please enter valid query type:" + queryType);
        }
        return query;
    }

    public static boolean checkValueInList(List<WebElement> elements, String value) {
        boolean isAvailable = false;
        for (WebElement element : elements) {
            if (!(element.getText().equalsIgnoreCase(value))) {
                System.out.println(element.getText());
                logger.info("Value " + value + " is wrong in the List");
                isAvailable = false;
                break;
            } else {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    public static void checkElementExistenceInTable(WebDriver driver, List<WebElement> elementList, String value) {
        boolean isAvailable = false;
        for (WebElement element : elementList) {
            if (element.getText().contains(value)) {
                logger.info("Element " + value + " is found successfully");
                isAvailable = true;
                break;
            }
        }
        if (!isAvailable) {
            Assertions.assertTrue(false, "Element " + value + " is not found in List");
        }
    }

    public static void checkElementExistenceOnPage(WebDriver driver, WebElement pageElement, String pageLbl) {
        SeleniumWait.waitForPageLoaded(driver);
        SeleniumWait.waitForElementToBeClickable(driver, pageElement, 30);
//        SeleniumWait.pauseExecution(3);
        SeleniumUtils.scrollTillElementWithJS(driver, pageElement);
        SeleniumWait.waitUntilVisibilityOfElement(driver, pageElement);
        Assertions.assertTrue(SeleniumUtils.getText(driver, pageElement).equals
                (pageLbl));
    }

    public static void selectElementFromDDByValue(WebDriver driver, String elementType, WebElement elementDD) {
        SeleniumWait.pauseExecution(3);
        SeleniumUtils.scrollTillElementWithJS(driver, elementDD);
        elementDD.click();
        SeleniumWait.pauseExecution(2);
        SeleniumUtils.sendKeysAndEnterByActionsClass(driver, elementType);
        SeleniumWait.pauseExecution(2);
    }

    public static void clickAndCheckElementExistenceOnPage(WebDriver driver, WebElement clickElement, WebElement pageElement, String pageLbl) {
        SeleniumUtils.clickElement(driver, clickElement);
        SeleniumWait.waitForPageLoaded(driver);
        SeleniumWait.waitForElementToBeClickable(driver, pageElement, 30);
        SeleniumWait.waitUntilVisibilityOfElement(driver, pageElement);
        Assertions.assertTrue(SeleniumUtils.getText(driver, pageElement).equals(pageLbl));
    }

    public static void checkElementPresenceOnPage(WebDriver driver, WebElement pageElement) {
        SeleniumWait.waitForPageLoaded(driver);
        SeleniumWait.waitUntilVisibilityOfElement(driver, pageElement);
        SeleniumUtils.scrollTillElementWithJS(driver, pageElement);
        Assertions.assertTrue(pageElement.isDisplayed());
    }

   /* public static boolean checkElementIsClickable(WebDriver driver, WebElement pageElement) {
        SeleniumWait.waitForPageLoaded(driver);
        SeleniumWait.waitUntilVisibilityOfElement(driver, pageElement);
        SeleniumUtils.scrollTillElementWithJS(driver, pageElement);
        Assertions.assertTrue(pageElement.isDisplayed());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        if(wait.until(ExpectedConditions.elementToBeClickable())){

            return true;
        }
        return false;
    } */


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

    public static void selectValueFromDropDownUsingIndex(WebDriver driver, int index, WebElement elementDD) {
        SeleniumWait.pauseExecution(3);
        if (!(elementDD.isDisplayed() && elementDD.isEnabled())) {
            SeleniumUtils.scrollTillElementWithJS(driver, elementDD);
        }

        try {
            elementDD.click();
        } catch (Exception e) {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", elementDD);
        }

        SeleniumWait.pauseExecution(3);
        SeleniumUtils.selectValueFromDropDownUsingIndex(elementDD, index);
        SeleniumWait.pauseExecution(3);
    }

    public static void scrollPageHorizontallyTillEnd(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView()", element);

    }

    public static void scrollPageRightToLeft(WebDriver driver, WebElement element) {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollBy(-1000, 0);", element);

    }

    public static void removeByClass(WebDriver driver, WebElement e, String attribute) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].classList.remove(" + attribute + ");", e);
    }


    public static void setAttribute(WebDriver driver, WebElement e, String attribute) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + e + "').setAttribute('disabled','enabled:')");
    }

    public static double roundOffInteger (double number){
                return Math.round(number);
    }

    public static String getDoubleNumInStringWithoutScientificNotation(double num){
        BigDecimal bd = new BigDecimal(num);
        return bd.toPlainString();
    }

}












