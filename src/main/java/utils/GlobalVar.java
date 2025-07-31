package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GlobalVar {
    public static String baseURL;
    public static String currentEnvironment;
    public static String platformType;
    public static String slash;
    public static String propertyPath;
    public static boolean highlight;
    public static boolean headless;
    public static boolean incognito;
    public static String browserName;
    public static String workingDirectory;
    public static String executionMode;
    public static String lineSeparator;
    public static String pathSeparator;
    public static String remoteUrl;
    public static String hostName;
    public static String hostIpAddress;

    static {
        currentEnvironment = "demo1";
        highlight = true;
        headless = false;
        incognito = false;
        browserName = "chrome";
        workingDirectory = System.getProperty("user.dir");
        slash = System.getProperty("file.separator");
        lineSeparator = System.getProperty("line.separator");
        pathSeparator = System.getProperty("path.separator");
        platformType = System.getProperty("os.name");
        remoteUrl = "http://selenium-hub:4444/wd/hub";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            hostIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        }
}
