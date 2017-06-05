package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

	public static String getValue(String key) {
        Properties properties = new Properties();
        try {
        	properties.load(PropertyReader.class.getClassLoader().getResourceAsStream("crawling.properties"));
        	properties.load(PropertyReader.class.getClassLoader().getResourceAsStream("log4j.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return properties.getProperty(key);
    }
}
