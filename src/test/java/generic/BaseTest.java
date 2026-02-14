package generic;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
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
        driver.set(new AppiumDriver(service.get().getUrl(),options));
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



    public void longClick(WebElement element,int duration){
        ((JavascriptExecutor) this.getDriver()).executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId",((RemoteWebElement) element).getId(),
                "duration",duration
        ));
    }

    public void doubleClick(WebElement element,int duration){
        ((JavascriptExecutor) this.getDriver()).executeScript("mobile: doubleClickGesture", ImmutableMap.of(
                "elementId",((RemoteWebElement) element).getId()
        ));
    }

    public void dragAndDrop(WebElement from,WebElement to){
        ((JavascriptExecutor) this.getDriver()).executeScript("mobile: dragGesture",ImmutableMap.of(
                "elementId",((RemoteWebElement) from).getId(),
                "endX", to.getLocation().getX(),
                "endY", to.getLocation().getY()
        ));
    }

    public void swipe(WebElement element,String direction,double percent){
        ((JavascriptExecutor) this.getDriver()).executeScript("mobile: swipeGesture",ImmutableMap.of(
                "elementId",((RemoteWebElement) element).getId(),
                "direction",direction,
                "percent",percent
        ));
    }

    public String switchToWebView(){
        String current_context = ((AndroidDriver)this.getDriver()).getContext();
        for(String content : ((AndroidDriver) this.getDriver()).getContextHandles()){
            ((AndroidDriver) this.getDriver()).context(content);
        }
        return current_context;
    }

    public void switchToContext(String context){
        ((AndroidDriver) this.getDriver()).context(context);
    }


}
