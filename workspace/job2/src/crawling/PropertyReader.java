package crawling;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

	public static String getValue(String key) {
        Properties properties = new Properties();
        try {
            //properties.load(new FileInputStream("C:/workspace/kcmi_moto/src/main/resources/config.properties"));
        	//properties.load(new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 7.0/webapps/kcmi/WEB-INF/classes/config.properties"));
        	//properties.load(new FileInputStream("config.properties"));
        	properties.load(PropertyReader.class.getClassLoader().getResourceAsStream("config.properties"));
        	properties.load(PropertyReader.class.getClassLoader().getResourceAsStream("log4j.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return properties.getProperty(key);
    }
}
