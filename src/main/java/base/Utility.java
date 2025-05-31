package base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class Utility {

    public static final String envConfig_Property_File_Path = "PropertyFiles/envConfig.properties";

    // Fetch a value from env config properties file
    public static String fetchConfigPropertyValue(String key) throws IOException {
        try (InputStream is = Utility.class.getClassLoader().getResourceAsStream(envConfig_Property_File_Path)) {
            if (is == null) throw new IOException("envConfig.properties not found.");
            Properties property = new Properties();
            property.load(is);
            return property.getProperty(key);
        }
    }

    // Capture screenshot and return saved file path
    public static String capture(WebDriver driver, String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/Report/screenshots/" + screenShotName + ".png";
        FileHandler.copy(source, new File(destPath));
        return destPath;
    }

    // Check if a string is not empty or "null"
    public static boolean isNotEmpty(String value) {
        return value != null && !value.equalsIgnoreCase("null") && !value.trim().isEmpty();
    }

    // Check if a string is empty or "null"
    public static boolean isEmpty(String value) {
        return !isNotEmpty(value);
    }

    // Append a line to a log file
    public static boolean WriteLog(String content) {
        String filePath = System.getProperty("user.dir") + "/Report/AuditTrax.log";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content + System.lineSeparator());
            return true;
        } catch (IOException ex) {
            System.err.println("Failed to write to log: " + ex.getMessage());
            return false;
        }
    }

    // Convert String to Integer with error handling
    public static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            System.err.println("Failed to parse integer from: " + value);
            return null;
        }
    }
}
