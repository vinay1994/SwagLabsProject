package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Satya Prakash Solanki
 */
public class LoadProperty {
    public String getValueFromPropertyFile(String filepath, String keyForValue) {
        Properties properties = new Properties();
        try (FileInputStream fileInput = new FileInputStream(filepath)) {
            properties.load(fileInput);
            return properties.getProperty(keyForValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueFromProperties(String key) {
        System.out.printf("Property Path is ---" + GlobalVar.propertyPath);
        if (!key.isEmpty()) {
            return getValueFromPropertyFile(GlobalVar.propertyPath, key);
        }
        return "";
    }
}