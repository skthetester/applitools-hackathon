import com.applitools.eyes.RectangleSize;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import org.testng.asserts.SoftAssert;
import com.applitools.eyes.selenium.Eyes;

/**
 * Visual AI Tests using Applitools
 * Author: Sivakumar Ganesan
 * Created: 11/08/2019
 */
public class VisualAITests {
    private WebDriver driver;
    //Choose the ACME app version. It can either be 1.0 or 2.0.
    private String ACMEAppVersion = "1.0";
    private String baseURL = "https://demo.applitools.com/";

    // Applitools
    private Eyes eyes;

    @BeforeClass
    public void setupDriver() {
        //Setup Chrome driver
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--start-maximized");
//        options.addArguments("--disable-notifications");
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
    }

    private void validateWindow() {
        eyes.open(driver, "Hackathon App", "Sivakumar Ganesan Test ", new RectangleSize(1200, 800)); //+ Thread.currentThread().getStackTrace()[2].getMethodName());
        eyes.checkWindow();
        eyes.close();
    }

    @AfterTest
    private void closeEyes() {
        eyes.abortIfNotClosed();
    }

    @Test
    public void Verify_Login_Page_Elements() {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the UI elements in login page
        validateWindow();
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"", "", "Both Username and Password must be present"},
                {"only username", "", "Password must be present"},
                /* ERROR: Failed to detect the error message in Version 2 for this data as XPATH changed */
                {"", "only password", "Username must be present"},
                {"test", "password", ""}};
    }

    @Test(dataProvider = "data-provider", priority = 2, enabled = false)
    public void Data_Driven_Test(String userNameText, String passwordText, String errorText) {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the login feature
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));

        userName.sendKeys(userNameText);
        pwd.sendKeys(passwordText);
        loginBtn.click();

        if (!errorText.equals("")) {
            WebElement errorAlert = driver.findElement(By.xpath("/html/body/div/div/div[3]"));
            Assert.assertEquals(errorAlert.getText(), errorText);
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("hackathonApp"), "Login unsuccessful >>");
        }
    }

    @Test(priority = 3, enabled = false)
    public void Table_Sort_Test() throws InterruptedException {
        //Open the ACME app
        driver.get(baseURL);
        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        //Get table info before sort
        Thread.sleep(5000);


        //Sort Table
        WebElement amountHeader = driver.findElement(By.id("amount"));
        amountHeader.click();
        Thread.sleep(5000);

        //Get table info after sort


        //Validate if rows are sorted by "Amount"
//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertTrue(manualSortedKeys.equals(appSortedKeys), "Sorting by Amount failed >>");
//
//        //Validate if all rows are sorted and matching
//        for (int k = 0; k < appSortedKeys.size(); k++) {
//            double amountKey = appSortedKeys.get(0);
//            softAssert.assertEquals(mapBefore.get(amountKey), mapAfter.get(amountKey), "Mismatch in data row after sorting >>");
//        }
//        softAssert.assertAll();
    }

    @Test(priority = 4, enabled = false)
    public void Canvas_Chart_Test() {
        //Open the ACME app
        driver.get(baseURL);

        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        //Compare Expenses
        WebElement compareExp = driver.findElement(By.id("showExpensesChart"));
        compareExp.click();
        /* ERROR: Unable to validate the canvas */
        Assert.fail("Unable to validate!");

        //Show data for next year
        WebElement nxtYearData = driver.findElement(By.id("addDataset"));
        nxtYearData.click();
        /* ERROR: Unable to validate the canvas with new data */
        Assert.fail("Unable to validate!");
    }

    @Test(priority = 5, enabled = false)
    public void Dynamic_Content_Test() {
        //Open the ACME app
        driver.get(baseURL.replace(".html", ".html?showAd=true"));

        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        int flashSale1 = driver.findElements(By.xpath("//*[@id=\"flashSale\"]/img")).size();
        int flashSale2 = driver.findElements(By.xpath("//*[@id=\"flashSale2\"]/img")).size();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(flashSale1, 1, "Flash sale 1 not found >>");
        softAssert.assertEquals(flashSale2, 1, "Flash sale 2 not found >>");
        /*ERROR: Presence of Image is validated but not the image itself. */
        softAssert.assertAll();
    }

    @AfterClass
    public void closeDriver() {
        driver.close();
        eyes.abortIfNotClosed();
    }
}
