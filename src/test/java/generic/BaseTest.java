package generic;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.*;

public class BaseTest {

    AppiumDriverLocalService service;
    AppiumDriver driver;

    @BeforeTest
    public void startAppiumServer(){
        service = new AppiumServiceBuilder().usingPort(7005).build();
        service.start();
    }


    @BeforeMethod
    public void startAppiumDriver(){
        UiAutomator2Options options = new UiAutomator2Options();
        driver = new AppiumDriver(service.getUrl(),options);
    }


    @Test
    public void test(){

    }



    @AfterMethod
    public void stopAppiumDriver(){
        driver.close();
    }


    @AfterTest
    public void stopAppiumServer(){
       if(service != null) service.stop();
    }


}
