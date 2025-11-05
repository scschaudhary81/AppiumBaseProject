package generic;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();
    ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private final Properties properties = new Properties();


    public Logger getLogger(){
        return this.logger;
    }

    public AppiumDriver getDriver(){
        return driver.get();
    }


    @BeforeTest
    public void startAppiumServer(){
        try{
            properties.load(new FileInputStream("Config.properties"));
            int appium_port = Integer.parseInt(properties.getProperty("APPIUM_PORT"));
            AppiumDriverLocalService localService = new AppiumServiceBuilder().usingPort(appium_port).withLogFile(new File("Appium.log")).build();
            service.set(localService);
            service.get().start();
            this.getLogger().info("Started Appium Server");

        } catch (Exception e) {
            this.getLogger().info("Error Stopping Appium Server");
            throw new RuntimeException(e);
        }
    }


    @BeforeMethod
    public void startAppiumDriver(){
        UiAutomator2Options options = new UiAutomator2Options();
       // driver.set(new AppiumDriver(service.get().getUrl(),options));
    }


    @Test
    public void test(){

    }



    @AfterMethod
    public void stopAppiumDriver(){
        if(getDriver()!=null) this.getDriver().close();
    }


    @AfterTest
    public void stopAppiumServer(){
       if(service != null && service.get()!=null) service.get().stop();
    }


}
