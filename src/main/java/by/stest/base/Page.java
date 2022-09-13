package by.stest.base;

import by.stest.listeners.CustomListeners;
import by.stest.utilities.ExcelReader;
import by.stest.utilities.ExtentManager;
import by.stest.utilities.TestUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class Page {

    public static WebDriver driver;
    public static WebDriverWait wait;
    public static Properties config = new Properties();
    public static Properties or = new Properties();
    public static FileInputStream fis;
    public static Logger log2 = LogManager.getLogger(Page.class.getName());
    public static ExcelReader excel = new ExcelReader("src\\test\\resources\\excel\\Testdata.xlsx");
    public static ExtentReports extent = ExtentManager.createInstance();
    public static ThreadLocal<ExtentTest> testThread = new ThreadLocal<ExtentTest>();
    public String browser;
    public static TopMenu topMenu;
    public static Date d = new Date();

    public Page() {

        if (driver == null) {
            String currentDate = d.toString().replace(":", "_").replace(" ", "_");
            TestUtil.initializeYourLogger("src/test/resources/logs/v2_app_" + currentDate
                    + ".log","%d %p %c [%t] %m%n");
            try {
                fis = new FileInputStream("src\\test\\resources\\properties\\Config.properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                config.load(fis);
                log2.info("Config file loaded!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis = new FileInputStream("src\\test\\resources\\properties\\OR.properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                or.load(fis);
                log2.info("OR file loaded!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Jenkins Browser filter configuration
            if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {
                browser = System.getenv("browser");
            } else {
                browser = config.getProperty("browser");
            }

            config.setProperty("browser", browser);

            if (config.getProperty("browser").equals("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if (config.getProperty("browser").equals("chrome")) {
                WebDriverManager.chromedriver().setup();
                HashMap<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("intl.accept_languages", "en-EN,en");
                prefs.put("credentials_enable_service", false);
                //prefs.put("profile.password_manager_enabled", false);
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", prefs);
                options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-notifications");
                driver = new ChromeDriver(options);
                log2.info("Chrome Launched!!!");
            } else if (config.getProperty("browser").equals("edge")) {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                HashMap<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("intl.accept_languages", "en-EN,en");
                options.setExperimentalOption("prefs", prefs);
                driver = new EdgeDriver(options);
            }

            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            topMenu = new TopMenu(driver);
            driver.manage().window().maximize();
            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(Long.parseLong(config.getProperty("implicit.wait"))));
            driver.get(config.getProperty("testsiteurl"));
            log2.info("Navigated to: " + config.getProperty("testsiteurl"));
        }
    }

    public static void click(String locator) {
        if (locator.endsWith("_CSS")) {
            driver.findElement(By.cssSelector(or.getProperty(locator))).click();
        } else if (locator.endsWith("_XPATH")) {
            driver.findElement(By.xpath(or.getProperty(locator))).click();
        } else if (locator.endsWith("_ID")) {
            driver.findElement(By.id(or.getProperty(locator))).click();
        }

        testThread.get().log(Status.INFO, "Clicking on: " + locator);
        log2.info("Clicking on element: " + locator);
    }

    public static void type(String locator, String value) {
        if (locator.endsWith("_CSS")) {
            driver.findElement(By.cssSelector(or.getProperty(locator))).sendKeys(value);
        } else if (locator.endsWith("_XPATH")) {
            driver.findElement(By.xpath(or.getProperty(locator))).sendKeys(value);
        } else if (locator.endsWith("_ID")) {
            driver.findElement(By.id(or.getProperty(locator))).sendKeys(value);
        }

        CustomListeners.testThread.get()
                .log(Status.INFO, "Typing in field: " + locator + " entered value as: " + value);
        log2.info("Typing in: " + locator + " entered value as: " + value);
    }

    static WebElement dropdown;

    public void select(String locator, String value) {
        if (locator.endsWith("_CSS")) {
            dropdown = driver.findElement(By.cssSelector(or.getProperty(locator)));
        } else if (locator.endsWith("_XPATH")) {
            dropdown = driver.findElement(By.xpath(or.getProperty(locator)));
        } else if (locator.endsWith("_ID")) {
            dropdown = driver.findElement(By.id(or.getProperty(locator)));
        }

        Select select = new Select(dropdown);
        select.selectByVisibleText(value);

        CustomListeners.testThread.get()
                .log(Status.INFO, "Selecting from dropdown: " + locator + " value as " + value);
        log2.info("Selecting from dropdown: " + locator + " value as " + value);
    }

    public static void verifyEquals(String expected, String actual) throws Throwable {
        try {
            Assert.assertEquals(actual, expected);
        } catch (Throwable t) {

            TestUtil.captureScreenshot();

            // ReportNG
            Reporter.log("<br>" + "Verification failure: " + t.getMessage() + "<br>");
            Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" + TestUtil.screenshotName
                    + " height=200 width=240></img></a>");
            Reporter.log("<br>");
            Reporter.log("<br>");

            // Extent Reports
            testThread.get().log(Status.FAIL, "Verification failed with exception: " + t.getMessage());
            testThread.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>"
                    + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName).build());

            throw t;
        }
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch(NoSuchElementException e) {
            return false;
        }
    }

    public static void quit() {
        driver.quit();
    }
}
