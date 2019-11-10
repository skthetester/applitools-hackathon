import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.applitools.eyes.selenium.Eyes;

/**
 * Visual AI Tests using Applitools
 * Author: Sivakumar Ganesan
 * Created: 11/09/2019
 */
public class VisualAITests {
    private WebDriver driver;
    //Choose the ACME app version. It can either be 1.0 or 2.0.
    private String ACMEAppVersion = "2.0";
    private String baseURL = "https://demo.applitools.com/";
    private String batchName = "Hackathon Demo App - Ver " + ACMEAppVersion + " - Regression";

    // Applitools
    private Eyes eyes;

    @BeforeClass
    public void setupDriver() {
        //Setup Chrome driver
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();

        //Setup the base URL based on the given version
        if (ACMEAppVersion.equals("1.0")) baseURL = baseURL + "hackathon.html";
        else if (ACMEAppVersion.equals("2.0")) baseURL = baseURL + "hackathonV2.html";
        else Assert.fail("Incorrect app version. It can either be 1.0 or 2.0.");
    }

    @BeforeClass
    private void initiateEyes() {
        eyes = new Eyes();
        eyes.setApiKey("97OYHwvhyiMZKbgtOuz7101YlHj106URaWrrl6PCBO104R9ERo110");
        eyes.setBatch(new BatchInfo(batchName));
    }

    @Test(priority = 1)
    public void Verify_Login_Page_Elements() {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the UI elements in login page
        validateWindow("Login Page");
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"", "", "Both Username and Password must be present"},
                {"only username", "", "Password must be present"},
                {"", "only password", "Username must be present"},
                {"test", "password", ""}};
    }

    @Test(dataProvider = "data-provider", priority = 2)
    public void Data_Driven_Test(String userNameText, String passwordText, String errorText) {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the login feature
        login(userNameText, passwordText);

        if (!errorText.equals("")) {
            WebElement errorAlert = driver.findElement(By.xpath("/html/body/div/div/div[3]"));

            //Validate the UI elements in login page
            validateWindow(errorText);
            Assert.assertEquals(errorAlert.getText(), errorText);
        } else {
            //Validate the UI elements in login page
            validateWindow("Successful Login");
        }
    }

    @Test(priority = 3)
    public void Table_Sort_Test() throws InterruptedException {
        //Open the ACME app
        driver.get(baseURL);

        //Login
        login("username", "password");

        //Get table info before sort
        Thread.sleep(5000);

        //Validate the UI elements in home screen - before sort
        validateWindow("Home Screen Before Sort");

        //Sort Table
        WebElement amountHeader = driver.findElement(By.id("amount"));
        amountHeader.click();
        Thread.sleep(5000);

        //Validate the UI elements in home screen - after sort
        validateWindow("Home Screen After Sort");
    }

    @Test(priority = 4)
    public void Canvas_Chart_Test() {
        //Open the ACME app
        driver.get(baseURL);

        //Login
        login("username", "password");

        //Compare Expenses
        WebElement compareExp = driver.findElement(By.id("showExpensesChart"));
        compareExp.click();

        //Validate the Canvas
        validateElement(By.id("canvas"), "Canvas - 2017 & 2018");

    }

    @Test(priority = 5)
    public void Canvas_Chart_2019_Test() {
        //Open the ACME app
        driver.get(baseURL);

        //Login
        login("username", "password");

        //Compare Expenses
        WebElement compareExp = driver.findElement(By.id("showExpensesChart"));
        compareExp.click();

        //Show data for next year
        WebElement nxtYearData = driver.findElement(By.id("addDataset"));
        nxtYearData.click();

        //Validate the Canvas
        validateElement(By.id("canvas"), "Canvas - 2019");
    }

    @Test(priority = 6, enabled = true)
    public void Dynamic_Content_Test() {
        //Open the ACME app
        driver.get(baseURL.replace(".html", ".html?showAd=true"));

        //Login
        login("username", "password");

        //Validate the flash sale images
        eyes.setMatchLevel(MatchLevel.LAYOUT);
        validateWindow("Flash Sale");

    }

    /**
     * Common method for eyes to check window
     */
    private void validateWindow(String testName) {
        eyes.open(driver, "Hackathon Demo App", Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + testName, new RectangleSize(1200, 800));
        eyes.setForceFullPageScreenshot(true);
        eyes.checkWindow();
        eyes.close();
    }


    /**
     * Common method for eyes to check specific element
     */
    private void validateElement(By locator, String testName) {
        eyes.open(driver, "Hackathon Demo App", Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + testName, new RectangleSize(1200, 800));
        eyes.checkElement(locator);
        eyes.close();
    }

    /**
     * Common method to login
     */
    private void login(String userNameText, String passwordText) {
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys(userNameText);
        pwd.sendKeys(passwordText);
        loginBtn.click();
    }

    @AfterTest
    private void closeEyes() {
        eyes.abortIfNotClosed();
    }

    @AfterClass
    public void closeDriver() {
        driver.close();
        eyes.abortIfNotClosed();
    }
}
